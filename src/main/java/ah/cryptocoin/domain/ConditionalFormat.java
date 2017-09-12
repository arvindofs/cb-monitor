package ah.cryptocoin.domain;

/**
 * Created by arhariha on 9/11/17.
 */
public class ConditionalFormat {
    private static ConditionalFormat singleton;

    Rate compareWith;

    public static ConditionalFormat getInstance() {
        if (singleton == null) {
            new ConditionalFormat();
        }
        return singleton;
    }

    private ConditionalFormat(){
        singleton = this;
    }

    public void setCompareWith(Rate compareWith) {
        this.compareWith = compareWith;
    }

    public Rate getCompareWith() {
        return compareWith;
    }

    /**
     * return -1 if currentRate is bad
     * return 0 if currentRate is neutral
     * return 1 if currentRate is good
     * @param currentRate
     * @return
     */
    public int compare(Rate currentRate, Coin.RateType rateType, Coin.CoinBase coinBase) {
        return compare(currentRate.getValue(coinBase, rateType), rateType, coinBase);
    }

    /**
     * return -1 if currentRate is bad
     * return 0 if currentRate is neutral
     * return 1 if currentRate is good
     * @param currentAmount
     * @return
     */
    public int compare(float currentAmount, Coin.RateType rateType, Coin.CoinBase coinBase) {

        float compareTo = compareWith.getValue(coinBase, rateType);

        int code[] = rateType.equals(Coin.RateType.BUY) ? new int[]{1, 0, -1} : new int[]{-1, 0, 1};
        int returnCode = code[1];

        if (compareTo == -1) return code[1];

        if (compareTo > currentAmount) {
            returnCode = code[0];
        } else if (compareTo < currentAmount) {
            returnCode = code[2];
        }

        return returnCode;
    }

}
