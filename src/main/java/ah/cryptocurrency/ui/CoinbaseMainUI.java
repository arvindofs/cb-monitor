package ah.cryptocurrency.ui;

import ah.cryptocoin.domain.ApplicationConf;
import ah.cryptocoin.domain.Coin;
import ah.cryptocoin.domain.CoinApiResponse;
import ah.cryptocoin.domain.Rate;
import ah.cryptocoin.util.Agent;
import ah.cryptocoin.util.RateReader;
import javafx.application.Application;
import layout.SpringUtilities;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by arhariha on 9/8/17.
 */
public class CoinbaseMainUI extends JFrame {


    public CoinbaseMainUI() throws HeadlessException {
        Agent.register(ApplicationConf.getApplicationConf());
        initialize();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void addPanel(JPanel panel, int position) {
        getContentPane().add(panel,position);
    }

    private void initialize() {
        this.setLayout(new BorderLayout());
        setTitle("Coinbase Rate Monitor");
        getContentPane().add((JPanel) Agent.register(new HeaderUI()), BorderLayout.NORTH);
        getContentPane().add((JPanel) Agent.register(new ContentUI()), BorderLayout.CENTER);
        getContentPane().add((JPanel) Agent.register(new FooterUI()), BorderLayout.SOUTH);

//        this.setLayout(new SpringLayout());
//        getContentPane().add((JPanel) Agent.register(new HeaderUI()));
//        getContentPane().add((JPanel) Agent.register(new ContentUI()));
//        getContentPane().add((JPanel) Agent.register(new FooterUI()));
//        SpringUtilities.makeCompactGrid(this.getContentPane(), 3,1,5,5,5,5);

        setSize(1200,800);
        setLocationRelativeTo(null);
        setVisible(true);
        currentRateLoader();
    }

    public static void main(String[] args) {
        ApplicationConf conf = ApplicationConf.getApplicationConf();
        int seconds = Integer.valueOf(new SimpleDateFormat("ss").format(new Date()));
        seconds = (15 - ((int)(seconds % 15)) * 15);


        ScheduledExecutorService e= Executors.newSingleThreadScheduledExecutor();
        e.scheduleAtFixedRate(new Runnable() {
            public void run() {
                currentRateLoader();
            }
        }, seconds, conf.getDelayPeriod(), conf.getDelayUnit());

        new CoinbaseMainUI();

    }

    private static void currentRateLoader() {
        SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {
            Rate rate = null;

            protected Boolean doInBackground() throws Exception {
                try {
                    long now = System.currentTimeMillis();

                    CoinApiResponse btcSell = RateReader.getSellRate(Coin.CoinBase.BTC, Coin.Currency.USD);
                    CoinApiResponse ethSell = RateReader.getSellRate(Coin.CoinBase.ETH, Coin.Currency.USD);
                    CoinApiResponse ltcSell = RateReader.getSellRate(Coin.CoinBase.LTC, Coin.Currency.USD);

                    CoinApiResponse btcBuy = RateReader.getBuyRate(Coin.CoinBase.BTC, Coin.Currency.USD);
                    CoinApiResponse ethBuy = RateReader.getBuyRate(Coin.CoinBase.ETH, Coin.Currency.USD);
                    CoinApiResponse ltcBuy = RateReader.getBuyRate(Coin.CoinBase.LTC, Coin.Currency.USD);

                    CoinApiResponse btcSpot = RateReader.getSpotRate(Coin.CoinBase.BTC, Coin.Currency.USD);
                    CoinApiResponse ethSpot = RateReader.getSpotRate(Coin.CoinBase.ETH, Coin.Currency.USD);
                    CoinApiResponse ltcSpot = RateReader.getSpotRate(Coin.CoinBase.LTC, Coin.Currency.USD);

                    rate = new Rate(now);
                    rate.setBuy(btcBuy.getData(), ethBuy.getData(), ltcBuy.getData());
                    rate.setSell(btcSell.getData(), ethSell.getData(), ltcSell.getData());
                    rate.setSpot(btcSpot.getData(), ethSpot.getData(), ltcSpot.getData());


                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }

                return true;
            }

            protected void done() {
                try {
                    boolean status = get();
                    if (status) {
                        Agent.broadcast(rate);
                    }
                } catch (
                        InterruptedException e) {
                    // Do Nothing
                } catch (
                        ExecutionException e) {
                    // Do Nothing
                }
            }
        };
        worker.execute();
    }
}
