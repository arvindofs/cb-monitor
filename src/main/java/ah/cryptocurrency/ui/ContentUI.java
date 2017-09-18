package ah.cryptocurrency.ui;

import ah.cryptocoin.domain.ApplicationConf;
import ah.cryptocoin.domain.Coin;
import ah.cryptocoin.domain.Rate;
import ah.cryptocoin.domain.RateHistory;
import layout.SpringUtilities;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.TimeSeriesDataItem;

import javax.swing.*;
import java.awt.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Created by arhariha on 9/10/17.
 */
public class ContentUI extends JPanel implements CoinAgent {
    boolean initialized = false;

    Map<Coin.CoinBase,TimeSeriesCollection> dataset = new HashMap<Coin.CoinBase, TimeSeriesCollection>();

    final TimeSeries btcSellSeries = new TimeSeries("BTC Sell", null, null);
    final TimeSeries ethSellSeries = new TimeSeries("ETH Sell", null, null);
    final TimeSeries ltcSellSeries = new TimeSeries("LTC Sell", null, null);

    final TimeSeries btcBuySeries = new TimeSeries("BTC Buy", null, null);
    final TimeSeries ethBuySeries = new TimeSeries("ETH Buy", null, null);
    final TimeSeries ltcBuySeries = new TimeSeries("LTC Buy", null, null);


    final TimeSeries btcSpotSeries = new TimeSeries("BTC Spot", null, null);
    final TimeSeries ethSpotSeries = new TimeSeries("ETH Spot", null, null);
    final TimeSeries ltcSpotSeries = new TimeSeries("LTC Spot", null, null);


    public void updateRate(Rate rate) {
        if(!initialized) return;
        addTimeSeriesDataItem(rate);
    }

    public ContentUI() {

        dataset = createDataset();
        ChartPanel btcChartPanel = createBTCChartPanel(dataset.get(Coin.CoinBase.BTC));
        ChartPanel ethChartPanel = createETHChartPanel(dataset.get(Coin.CoinBase.ETH));
        ChartPanel ltcChartPanel = createLTCChartPanel(dataset.get(Coin.CoinBase.LTC));

        setLayout(new SpringLayout());

        add(btcChartPanel);
        add(ethChartPanel);
        add(ltcChartPanel);

        SpringUtilities.makeCompactGrid(this, 3,1,5,5,5,5);
    }

    private ChartPanel createBTCChartPanel(TimeSeriesCollection btcDataset) {

        JFreeChart btcChart = createChart(btcDataset, "BTC Trend");
        final ChartPanel btcChartPanel = new ChartPanel(btcChart);
        btcChartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        return btcChartPanel;

    }

    private ChartPanel createETHChartPanel(TimeSeriesCollection btcDataset) {

        JFreeChart btcChart = createChart(btcDataset, "ETH Trend");
        final ChartPanel btcChartPanel = new ChartPanel(btcChart);
        btcChartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        return btcChartPanel;

    }

    private ChartPanel createLTCChartPanel(TimeSeriesCollection btcDataset) {

        JFreeChart btcChart = createChart(btcDataset, "LTC Trend");
        final ChartPanel btcChartPanel = new ChartPanel(btcChart);
        btcChartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        return btcChartPanel;
    }


    private void addTimeSeriesDataItem(Rate rate) {

        addBTCSeriesData(rate);
        addETHSeriesData(rate);
        addLTCSeriesData(rate);

    }

    private void addBTCSeriesData(Rate rate) {
        TimeSeriesDataItem item = new TimeSeriesDataItem(new Millisecond(new Date(rate.getTime())), rate.getBTCSell().getAmount());
        btcSellSeries.addOrUpdate(item);

        item = new TimeSeriesDataItem(new Millisecond(new Date(rate.getTime())), rate.getBTCSpot().getAmount());
        btcSpotSeries.addOrUpdate(item);

        item = new TimeSeriesDataItem(new Millisecond(new Date(rate.getTime())), rate.getBTCBuy().getAmount());
        btcBuySeries.addOrUpdate(item);
    }

    private void addETHSeriesData(Rate rate) {
        TimeSeriesDataItem item = new TimeSeriesDataItem(new Millisecond(new Date(rate.getTime())), rate.getETHSell().getAmount());
        ethSellSeries.addOrUpdate(item);

        item = new TimeSeriesDataItem(new Millisecond(new Date(rate.getTime())), rate.getETHSpot().getAmount());
        ethSpotSeries.addOrUpdate(item);

        item = new TimeSeriesDataItem(new Millisecond(new Date(rate.getTime())), rate.getETHBuy().getAmount());
        ethBuySeries.addOrUpdate(item);
    }

    private void addLTCSeriesData(Rate rate) {
        TimeSeriesDataItem item = new TimeSeriesDataItem(new Millisecond(new Date(rate.getTime())), rate.getLTCSell().getAmount());
        ltcSellSeries.addOrUpdate(item);

        item = new TimeSeriesDataItem(new Millisecond(new Date(rate.getTime())), rate.getLTCSpot().getAmount());
        ltcSpotSeries.addOrUpdate(item);

        item = new TimeSeriesDataItem(new Millisecond(new Date(rate.getTime())), rate.getLTCBuy().getAmount());
        ltcBuySeries.addOrUpdate(item);
    }


    /**
     * Creates a sample dataset.
     *
     * @return A sample dataset.
     */
    public Map<Coin.CoinBase,TimeSeriesCollection> createDataset() {
        ApplicationConf conf = ApplicationConf.getApplicationConf();
        final RateHistory historicalRate = conf.getHistoricalRate();
        final TimeSeriesCollection btcDataSet = new TimeSeriesCollection();
        final TimeSeriesCollection ethDataSet = new TimeSeriesCollection();
        final TimeSeriesCollection ltcDataSet = new TimeSeriesCollection();

        SwingWorker<Boolean, Rate> worker = new SwingWorker<Boolean, Rate>() {
            @Override
            protected Boolean doInBackground() throws Exception {
                synchronized (historicalRate) {
                    for (Rate rate : historicalRate.historicalData) {
                        addTimeSeriesDataItem(rate);
                    }
                }

                initialized = true;
                return true;
            }


            @Override
            protected void done() {
                boolean status = false;
                try {
                    status = get();
                    if (status) {

                        btcDataSet.addSeries(btcBuySeries);
                        btcDataSet.addSeries(btcSpotSeries);
                        btcDataSet.addSeries(btcSellSeries);

                        ethDataSet.addSeries(ethBuySeries);
                        ethDataSet.addSeries(ethSpotSeries);
                        ethDataSet.addSeries(ethSellSeries);

                        ltcDataSet.addSeries(ltcBuySeries);
                        ltcDataSet.addSeries(ltcSpotSeries);
                        ltcDataSet.addSeries(ltcSellSeries);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
//
            }
        };

        worker.execute();
//        dataset.addSeries(ltcSellSeries);
//        dataset.addSeries(ethSellSeries);


        dataset.put(Coin.CoinBase.BTC, btcDataSet);
        dataset.put(Coin.CoinBase.ETH, ethDataSet);
        dataset.put(Coin.CoinBase.LTC, ltcDataSet);

        return dataset;
    }

    /**
     * Creates a sample chart.
     *
     * @param dataset  the dataset.
     *
     * @return A sample chart.
     */
    public JFreeChart createChart(TimeSeriesCollection dataset, String title) {

        final JFreeChart chart = ChartFactory.createTimeSeriesChart(
                title,
                "Time",
                "$ Value",
                dataset,
                true,
                true,
                false
        );


//        final CategoryPlot plot = (CategoryPlot) chart.getPlot();
//        plot.setForegroundAlpha(0.5f);
//        plot.setBackgroundPaint(Color.lightGray);
//        plot.setDomainGridlinePaint(Color.white);
//        plot.setRangeGridlinePaint(Color.white);
//
//        final CategoryAxis domainAxis = plot.getDomainAxis();
//        domainAxis.setLowerMargin(0.0);
//        domainAxis.setUpperMargin(0.0);
//
////         change the auto tick unit selection to integer units only...
//        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
//        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
//
//        final CategoryItemRenderer renderer = plot.getRenderer();
//        renderer.setItemLabelsVisible(true);

        return chart;

    }
}
