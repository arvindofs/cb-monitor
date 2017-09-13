package ah.cryptocoin.domain;

import ah.cryptocoin.util.Agent;
import ah.cryptocurrency.ui.CoinAgent;
import com.google.gson.Gson;

import java.awt.*;
import java.io.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by arhariha on 9/10/17.
 */
public class ApplicationConf implements CoinAgent {

    public static final Color GOOD    = Color.GREEN;
    public static final Color BAD     = Color.RED;
    public static final Color NEUTRAL = Color.WHITE;

    final long delayPeriod = 15;
    final TimeUnit delayUnit = TimeUnit.SECONDS;

    Rate previousRate;
    Rate currentRate;
    Rate rateToCompare;

    private final String CbMonitorHome = System.getProperty("user.home") + File.separator + ".cbmonitor" + File.separator;

    private final File rateHistoryFile   = new File(CbMonitorHome + "coinbase-rh.json");
    private final File thresholdRateFile =  new File(CbMonitorHome + "coinbase-tr.json");

    private Rate thresholdRate;
    private RateHistory historicalRate;

    public enum PriceComparison {PREVIOUS, TODAY, LAST24HOUR, LAST1HOUR, YESTERDAY, THRESHOLD};



    public PriceComparison getCompareMode() {
        return compareMode;
    }

    private PriceComparison compareMode;

    private static ApplicationConf conf;

    public static ApplicationConf getApplicationConf() {
        if (conf != null)  { return conf;}
        return new ApplicationConf();
    }

    private ApplicationConf() {
        conf = this;
        compareMode = PriceComparison.THRESHOLD;
        rateToCompare = new Rate(-1);
        loadCBMonitorData();
        Runtime.getRuntime().addShutdownHook(new Thread(){
            @Override
            public void run() {
                shutdown();
            }
        });
    }

    private void checkAndCreateAppDataDir() {
        File f = new File(CbMonitorHome);
        if (!f.exists()) {f.mkdir();}
    }
    private void loadCBMonitorData() {
        checkAndCreateAppDataDir();
        loadRateHistory();
        loadThresholdRate();
        Agent.register(historicalRate);
    }

    private void loadRateHistory() {
        try {
            historicalRate = loadData(RateHistory.class, rateHistoryFile);
        } catch (Exception e) {
            historicalRate = new RateHistory();
        }
    }

    private void loadThresholdRate() {
        try {
            thresholdRate = loadData(Rate.class, thresholdRateFile);
        } catch (Exception e) {
            thresholdRate = new Rate(System.currentTimeMillis());
            thresholdRate.setBuy(createCoin(4000f, Coin.CoinBase.BTC, Coin.Currency.USD),
                    createCoin(290f, Coin.CoinBase.ETH, Coin.Currency.USD),
                    createCoin(65.5f, Coin.CoinBase.LTC, Coin.Currency.USD));

            thresholdRate.setSell(createCoin(4600f, Coin.CoinBase.BTC, Coin.Currency.USD),
                    createCoin(360f, Coin.CoinBase.ETH, Coin.Currency.USD),
                    createCoin(75f, Coin.CoinBase.LTC, Coin.Currency.USD));

            thresholdRate.setSell(createCoin(4300f, Coin.CoinBase.BTC, Coin.Currency.USD),
                    createCoin(330f, Coin.CoinBase.ETH, Coin.Currency.USD),
                    createCoin(70f, Coin.CoinBase.LTC, Coin.Currency.USD));

            writeThresholdRate();
        }
    }

    private Coin createCoin(float amount, Coin.CoinBase base, Coin.Currency currency) {
        Coin coin = new Coin();
        coin.setAmount(amount);
        coin.setBase(base);
        coin.setCurrency(currency);

        return coin;
    }

    private <T>T loadData(Class<T> type, File file) throws InstantiationException, IllegalAccessException, FileNotFoundException {
        T returnClass = null;

        if (!file.exists()) throw new FileNotFoundException();

        try {
            InputStreamReader isr = new InputStreamReader(new FileInputStream(file));
            Gson gson = new Gson();
            returnClass = gson.fromJson(isr, type);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        return returnClass;
    }

    public long getDelayPeriod () {
        return delayPeriod;
    }

    public TimeUnit getDelayUnit() {
        return delayUnit;
    }

    public Rate getRateToCompare() {
        return rateToCompare;
    }

    public void updateRate(Rate rate) {

        if( compareMode.equals(PriceComparison.PREVIOUS)) {

            if (previousRate == null) {
                currentRate = rate;
            } else {
                previousRate = currentRate;
                currentRate = rate;
            }

            rateToCompare = previousRate;
            if (rateToCompare == null) {
                rateToCompare = new Rate(-1);
            }
            previousRate = rateToCompare;
        } else if (compareMode.equals(PriceComparison.THRESHOLD)) {
            loadThresholdRate();
            previousRate = rateToCompare = thresholdRate;
        }

        ConditionalFormat.getInstance().setCompareWith(rateToCompare);
        historicalRate.historicalData.add(rate);
    }

    public RateHistory getHistoricalRate() {
        return historicalRate;
    }

    private void writeHistoricalData() {
        writeData(rateHistoryFile, historicalRate);
    }

    private void writeThresholdRate() {
        writeData(thresholdRateFile, thresholdRate);
    }

    private void writeData(File writeTo, Object object) {

        Gson gson = new Gson();
        String json = gson.toJson(object);
        try {
            checkAndCreateAppDataDir();
            FileOutputStream fos = new FileOutputStream(writeTo);
            fos.write(json.getBytes());
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void shutdown() {
        writeHistoricalData();
        writeThresholdRate();
    }
}
