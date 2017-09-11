package ah.cryptocurrency.ui;

import javax.swing.*;
import java.awt.*;

import ah.cryptocoin.domain.ApplicationConf;
import ah.cryptocoin.domain.Coin;
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

        ltcPanel.add(new JLabel("Sell"));
        ltcPanel.add(ltcSell);
        ltcPanel.add(diffLtcSell);
        ltcSell.setEditable(false);
        diffLtcSell.setEditable(false);

        ltcPanel.add(new JLabel("Spot"));
        ltcPanel.add(ltcSpot);
        ltcPanel.add(diffLtcSpot);
        ltcSpot.setEditable(false);
        diffLtcSpot.setEditable(false);
    }

    private void initializeETHPanel() {
        ethPanel.add(new JLabel("Buy"));
        ethPanel.add(ethBuy);
        ethPanel.add(diffEthBuy);
        ethBuy.setEditable(false);
        diffEthBuy.setEditable(false);

        ethPanel.add(new JLabel("Sell"));
        ethPanel.add(ethSell);
        ethPanel.add(diffEthSell);
        ethSell.setEditable(false);
        diffEthSell.setEditable(false);

        ethPanel.add(new JLabel("Spot"));
        ethPanel.add(ethSpot);
        ethPanel.add(diffEthSpot);
        ethSpot.setEditable(false);
        diffEthSpot.setEditable(false);
    }


    private void initializeBTCPanel() {
        btcPanel.add(new JLabel("Buy"));
        btcPanel.add(btcBuy);
        btcPanel.add(diffBtcBuy);
        btcBuy.setEditable(false);
        diffBtcBuy.setEditable(false);

        btcPanel.add(new JLabel("Sell"));
        btcPanel.add(btcSell);
        btcPanel.add(diffBtcSell);
        btcSell.setEditable(false);
        diffBtcSell.setEditable(false);

        btcPanel.add(new JLabel("Spot"));
        btcPanel.add(btcSpot);
        btcPanel.add(diffBtcSpot);
        btcSpot.setEditable(false);
        diffBtcSpot.setEditable(false);
    }

    public void updateRate(Rate rate) {

        Rate previousRate = ApplicationConf.getApplicationConf().getRateToCompare();

        fillText(btcBuy, rate.getBTCBuy(), previousRate.getBTCBuy(), Color.RED, Color.GREEN, diffBtcBuy);
        fillText(ethBuy, rate.getETHBuy(), previousRate.getETHBuy(), Color.RED, Color.GREEN, diffEthBuy);
        fillText(ltcBuy, rate.getLTCBuy(), previousRate.getLTCBuy(), Color.RED, Color.GREEN, diffLtcBuy);


        fillText(btcSell, rate.getBTCSell(), previousRate.getBTCSell(), Color.GREEN, Color.RED, diffBtcSell);
        fillText(ethSell, rate.getETHSell(), previousRate.getETHSell(), Color.GREEN, Color.RED, diffEthSell);
        fillText(ltcSell, rate.getLTCSell(), previousRate.getLTCSell(), Color.GREEN, Color.RED, diffLtcSell);

        fillText(btcSpot, rate.getBTCSpot(), previousRate.getBTCSpot(), Color.GREEN, Color.RED, diffBtcSpot);
        fillText(ethSpot, rate.getETHSpot(), previousRate.getETHSpot(), Color.GREEN, Color.RED, diffEthSpot);
        fillText(ltcSpot, rate.getLTCSpot(), previousRate.getLTCSpot(), Color.GREEN, Color.RED, diffLtcSpot);

    }

    private void fillText(JTextField textField, Coin coin, Coin coinToCompare, Color c1, Color c2, JTextField diffLabel) {

        Float compareTo = coinToCompare == null ? -1 : coinToCompare.getAmount();

        float previousAmount = (compareTo == null) ? -1 : compareTo.floatValue();

        float current = coin.getAmount();
        if (previousAmount != -1) {
            if (current > previousAmount) {
                textField.setBackground(c1);
                diffLabel.setText("+" + (current-previousAmount));
                diffLabel.setBackground(c1);
            } else if (current < previousAmount) {
                textField.setBackground(c2);
                diffLabel.setText("-" + (previousAmount-current));
                diffLabel.setBackground(c2);
            }
        }

        textField.setText(Float.toString(current));
    }
}

