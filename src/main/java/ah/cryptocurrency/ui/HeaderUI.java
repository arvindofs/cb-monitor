package ah.cryptocurrency.ui;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.locks.Condition;

import ah.cryptocoin.domain.ApplicationConf;
import ah.cryptocoin.domain.Coin;
import ah.cryptocoin.domain.ConditionalFormat;
import ah.cryptocoin.domain.Rate;
import layout.SpringUtilities;

/**
 * Created by arhariha on 9/10/17.
 */
public class HeaderUI extends JPanel implements CoinAgent {

    JTextField btcBuy = new JTextField();
    JTextField diffBtcBuy = new JTextField(0);
    JTextField ethBuy = new JTextField();
    JTextField diffEthBuy = new JTextField(0);
    JTextField ltcBuy = new JTextField();
    JTextField diffLtcBuy = new JTextField(0);

    JTextField btcSell = new JTextField();
    JTextField diffBtcSell = new JTextField(0);
    JTextField ethSell = new JTextField();
    JTextField diffEthSell = new JTextField(0);
    JTextField ltcSell = new JTextField();
    JTextField diffLtcSell = new JTextField(0);

    JTextField btcSpot = new JTextField();
    JTextField diffBtcSpot = new JTextField(0);
    JTextField ethSpot = new JTextField();
    JTextField diffEthSpot = new JTextField(0);
    JTextField ltcSpot = new JTextField();
    JTextField diffLtcSpot = new JTextField(0);

    JPanel btcPanel = new JPanel();
    JPanel ethPanel = new JPanel();
    JPanel ltcPanel = new JPanel();


    public HeaderUI() {
        super();
        initialize();
    }

    private void initialize() {
        setLayout(new BorderLayout());

        btcPanel.setBorder(BorderFactory.createTitledBorder("BitCoin - BTC"));
        ethPanel.setBorder(BorderFactory.createTitledBorder("Ethereum - ETH"));
        ltcPanel.setBorder(BorderFactory.createTitledBorder("LiteCoin - LTC"));

        setLayout(new SpringLayout());
        btcPanel.setLayout(new SpringLayout());
        ethPanel.setLayout(new SpringLayout());
        ltcPanel.setLayout(new SpringLayout());

        initializeBTCPanel();
        initializeETHPanel();
        initializeLTCPanel();

        add(btcPanel);
        add(ethPanel);
        add(ltcPanel);


        SpringUtilities.makeCompactGrid(btcPanel, 3, 3, 5,5,5,5);
        SpringUtilities.makeCompactGrid(ethPanel, 3, 3, 5,5,5,5);
        SpringUtilities.makeCompactGrid(ltcPanel, 3, 3, 5,5,5,5);


        SpringUtilities.makeCompactGrid(this, 1, 3, 5,5,5,5);

    }

    private void initializeLTCPanel() {
        ltcPanel.add(new JLabel("Buy"));
        ltcPanel.add(ltcBuy);
        ltcPanel.add(diffLtcBuy);
        ltcBuy.setEditable(false);
        diffLtcBuy.setEditable(false);

        ltcPanel.add(new JLabel("Spot"));
        ltcPanel.add(ltcSpot);
        ltcPanel.add(diffLtcSpot);
        ltcSpot.setEditable(false);
        diffLtcSpot.setEditable(false);

        ltcPanel.add(new JLabel("Sell"));
        ltcPanel.add(ltcSell);
        ltcPanel.add(diffLtcSell);
        ltcSell.setEditable(false);
        diffLtcSell.setEditable(false);


    }

    private void initializeETHPanel() {
        ethPanel.add(new JLabel("Buy"));
        ethPanel.add(ethBuy);
        ethPanel.add(diffEthBuy);
        ethBuy.setEditable(false);
        diffEthBuy.setEditable(false);

        ethPanel.add(new JLabel("Spot"));
        ethPanel.add(ethSpot);
        ethPanel.add(diffEthSpot);
        ethSpot.setEditable(false);
        diffEthSpot.setEditable(false);

        ethPanel.add(new JLabel("Sell"));
        ethPanel.add(ethSell);
        ethPanel.add(diffEthSell);
        ethSell.setEditable(false);
        diffEthSell.setEditable(false);
    }


    private void initializeBTCPanel() {
        btcPanel.add(new JLabel("Buy"));
        btcPanel.add(btcBuy);
        btcPanel.add(diffBtcBuy);
        btcBuy.setEditable(false);
        diffBtcBuy.setEditable(false);

        btcPanel.add(new JLabel("Spot"));
        btcPanel.add(btcSpot);
        btcPanel.add(diffBtcSpot);
        btcSpot.setEditable(false);
        diffBtcSpot.setEditable(false);

        btcPanel.add(new JLabel("Sell"));
        btcPanel.add(btcSell);
        btcPanel.add(diffBtcSell);
        btcSell.setEditable(false);
        diffBtcSell.setEditable(false);
    }

    public void updateRate(Rate rate) {

        fillTextAndFormat(btcBuy, diffBtcBuy, rate, Coin.RateType.BUY, Coin.CoinBase.BTC);
        fillTextAndFormat(ethBuy, diffEthBuy, rate, Coin.RateType.BUY, Coin.CoinBase.ETH);
        fillTextAndFormat(ltcBuy, diffLtcBuy, rate, Coin.RateType.BUY, Coin.CoinBase.LTC);


        fillTextAndFormat(btcSell, diffBtcSell, rate, Coin.RateType.SELL, Coin.CoinBase.BTC);
        fillTextAndFormat(ethSell, diffEthSell, rate, Coin.RateType.SELL, Coin.CoinBase.ETH);
        fillTextAndFormat(ltcSell, diffLtcSell, rate, Coin.RateType.SELL, Coin.CoinBase.LTC);

        fillTextAndFormat(btcSpot, diffBtcSpot, rate, Coin.RateType.SPOT, Coin.CoinBase.BTC);
        fillTextAndFormat(ethSpot, diffEthSpot, rate, Coin.RateType.SPOT, Coin.CoinBase.ETH);
        fillTextAndFormat(ltcSpot, diffLtcSpot, rate, Coin.RateType.SPOT, Coin.CoinBase.LTC);

    }

    private void fillTextAndFormat(JTextField textField, JTextField diffTextField, Rate current, Coin.RateType rateType, Coin.CoinBase coinBase) {
        int code = ConditionalFormat.getInstance().compare(current, rateType, coinBase);
        Color color = (code == 1) ? ApplicationConf.GOOD : (code == -1) ? ApplicationConf.BAD : ApplicationConf.NEUTRAL;
        textField.setText(Float.toString(current.getValue(coinBase, rateType)));

        if ((code == 0 && !ApplicationConf.getApplicationConf().getCompareMode().equals(ApplicationConf.PriceComparison.PREVIOUS)) || code != 0) {
            textField.setBackground(color);
        }

        diffTextField.setBackground(color);
        Rate compareWithRate = ConditionalFormat.getInstance().getCompareWith();
        float diff = current.getValue(coinBase, rateType) - compareWithRate.getValue(coinBase, rateType);

        diffTextField.setText(diff > 0 ? "+" : "" + Float.toString(diff));
    }
}

