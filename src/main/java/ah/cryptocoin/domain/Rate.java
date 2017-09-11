package ah.cryptocoin.domain;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by arhariha on 9/10/17.
 */
public class Rate {
    public long time;

    private Map<Coin.CoinBase, Coin> spot;
    private Map<Coin.CoinBase, Coin> sell;
    private Map<Coin.CoinBase, Coin> buy;

    public Rate(long time) {

        this.time = time;
        spot = new HashMap<Coin.CoinBase, Coin>();
        buy  = new HashMap<Coin.CoinBase, Coin>();
        sell = new HashMap<Coin.CoinBase, Coin>();
    }

    public void setSpot(Coin btc, Coin eth, Coin ltc) {
        spot.put(Coin.CoinBase.BTC, btc);
        spot.put(Coin.CoinBase.ETH, eth);
        spot.put(Coin.CoinBase.LTC, ltc);
    }

    public void setBuy(Coin btc, Coin eth, Coin ltc) {
        buy.put(Coin.CoinBase.BTC, btc);
        buy.put(Coin.CoinBase.ETH, eth);
        buy.put(Coin.CoinBase.LTC, ltc);
    }

    public void setSell(Coin btc, Coin eth, Coin ltc) {
        sell.put(Coin.CoinBase.BTC, btc);
        sell.put(Coin.CoinBase.ETH, eth);
        sell.put(Coin.CoinBase.LTC, ltc);
    }

    public Coin getBTCSpot() {return spot.get(Coin.CoinBase.BTC);}
    public Coin getETHSpot() {return spot.get(Coin.CoinBase.ETH);}
    public Coin getLTCSpot() {return spot.get(Coin.CoinBase.LTC);}

    public Coin getBTCSell() {return sell.get(Coin.CoinBase.BTC);}
    public Coin getETHSell() {return sell.get(Coin.CoinBase.ETH);}
    public Coin getLTCSell() {return sell.get(Coin.CoinBase.LTC);}

    public Coin getBTCBuy() {return buy.get(Coin.CoinBase.BTC);}
    public Coin getETHBuy() {return buy.get(Coin.CoinBase.ETH);}
    public Coin getLTCBuy() {return buy.get(Coin.CoinBase.LTC);}

    public long getTime() {
        return time;
    }

    @Override
    public String toString() {
        return "ah.cryptocoin.domain.Rate{" +
                "time=" + time +
                ", spot=" + spot +
                ", sell=" + sell +
                ", buy=" + buy +
                '}';
    }

    @Override
    public int hashCode() {
        int result = (int) (time ^ (time >>> 32));
        result = 31 * result + (spot != null ? spot.hashCode() : 0);
        result = 31 * result + (sell != null ? sell.hashCode() : 0);
        result = 31 * result + (buy != null ? buy.hashCode() : 0);
        return result;
    }
}
