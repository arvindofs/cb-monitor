package ah.cryptocoin.domain;

import ah.cryptocoin.util.Agent;
import ah.cryptocurrency.ui.CoinAgent;
import com.google.gson.Gson;

import java.io.*;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * Created by arhariha on 9/10/17.
 */
public class ApplicationConf implements CoinAgent {

    final long delayPeriod = 1;
    final TimeUnit delayUnit = TimeUnit.SECONDS;



    Rate previousRate;
    Rate currentRate;
    Rate rateToCompare;

    RateHistory historicalRate;


    public static enum PriceComparison {PREVIOUS, TODAY, LAST24HOUR, LAST1HOUR, YESTERDAY};

    private PriceComparison compareMode;

    private static ApplicationConf conf;

    public static ApplicationConf getApplicationConf() {
        if (conf != null)  { return conf;}
        return new ApplicationConf();
    }

    private ApplicationConf() {
        conf = this;
        compareMode = PriceComparison.PREVIOUS;
        rateToCompare = new Rate(-1);
        loadHistoricalRate();
        Runtime.getRuntime().addShutdownHook(new Thread(){
            @Override
            public void run() {
                shutdown();
            }
        });

    }

    private void loadHistoricalRate() {
        File f = new File(System.getProperty("user.home")+File.separator+"coinbase-rh.json");
        if(!f.exists()) {
            historicalRate = new RateHistory(new ArrayList<Rate>());
        } else {
            try {
                InputStreamReader isr = new InputStreamReader(new FileInputStream(f));
                Gson gson = new Gson();
                historicalRate = gson.fromJson(isr, RateHistory.class);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }
        Agent.register(historicalRate);
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

        if (previousRate == null) {
            currentRate = rate;
        } else {
            previousRate = currentRate;
            currentRate = rate;
        }

        if( compareMode.equals(PriceComparison.PREVIOUS)) {
            rateToCompare = previousRate;
            if (rateToCompare == null) {
                rateToCompare = new Rate(-1);
            }
            previousRate = rateToCompare;
        }

        historicalRate.historicalData.add(rate);
    }

    public RateHistory getHistoricalRate() {
        return historicalRate;
    }

    private void shutdown() {
        Gson gson = new Gson();
        String json = gson.toJson(getHistoricalRate());
        try {
            FileOutputStream fos = new FileOutputStream(new File(System.getProperty("user.home")+File.separator+"coinbase-rh.json"));
            fos.write(json.getBytes());
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
