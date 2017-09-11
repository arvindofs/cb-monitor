package ah.cryptocoin.domain;

import ah.cryptocurrency.ui.CoinAgent;

import java.util.List;

/**
 * Created by arhariha on 9/10/17.
 */
public class RateHistory implements CoinAgent {
    public List<Rate> historicalData;

    public RateHistory(List<Rate> historicalData) {
        this.historicalData = historicalData;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RateHistory that = (RateHistory) o;

        return historicalData != null ? historicalData.equals(that.historicalData) : that.historicalData == null;

    }

    @Override
    public int hashCode() {
        return historicalData != null ? historicalData.hashCode() : 0;
    }

    public List<Rate> getHistoricalData() {
        return historicalData;
    }

    public void setHistoricalData(List<Rate> historicalData) {
        this.historicalData = historicalData;
    }

    public void updateRate(Rate rate) {
        historicalData.add(rate);
    }
}
