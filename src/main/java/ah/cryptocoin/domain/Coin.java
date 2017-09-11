package ah.cryptocoin.domain;

/**
 * Created by arhariha on 9/10/17.
 */
public class Coin {
    public static enum CoinBase {BTC, ETH, LTC};
    public static enum Currency {USD};
    public static enum RateType {SELL, SPOT, BUY};

    public CoinBase base;
    public Currency currency;
    public float amount;

    public CoinBase getBase() {
        return base;
    }

    public void setBase(CoinBase base) {
        this.base = base;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "ah.cryptocoin.domain.Coin{" +
                "base=" + base +
                ", currency=" + currency +
                ", amount=" + amount +
                '}';
    }
}


