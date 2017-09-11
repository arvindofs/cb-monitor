package ah.cryptocoin.domain;

/**
 * Created by arhariha on 9/10/17.
 */
public class CoinApiResponse {
    private Coin data;

    public CoinApiResponse(Coin data) {
        this.data = data;
    }

    public Coin getData() {
        return data;
    }

    public void setData(Coin data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ah.cryptocoin.domain.CoinApiResponse{" +
                "data=" + data +
                '}';
    }
}
