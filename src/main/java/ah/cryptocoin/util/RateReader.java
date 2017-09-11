package ah.cryptocoin.util;

import ah.cryptocoin.domain.Coin;
import ah.cryptocoin.domain.CoinApiResponse;
import com.google.api.client.http.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.gson.Gson;

import java.io.IOException;

/**
 * Created by arhariha on 9/10/17.
 */
public class RateReader {

    public static CoinApiResponse getSellRate(Coin.CoinBase base, Coin.Currency currency)
            throws Exception {
        return getRate(base, currency, Coin.RateType.SELL);
    }

    public static CoinApiResponse getBuyRate(Coin.CoinBase base, Coin.Currency currency)
            throws Exception {
        return getRate(base, currency, Coin.RateType.BUY);
    }

    public static CoinApiResponse getSpotRate(Coin.CoinBase base, Coin.Currency currency)
            throws Exception {
        return getRate(base, currency, Coin.RateType.SPOT);
    }

    private static CoinApiResponse getRate(Coin.CoinBase base, Coin.Currency currency, Coin.RateType rateType)
            throws Exception {

        HttpTransport transport = new NetHttpTransport();
        HttpRequestFactory requestFactory = transport.createRequestFactory();
        String url = String.format("https://api.coinbase.com/v2/prices/%s-%s/%s",base, currency, rateType.toString().toLowerCase());
        GenericUrl genericUrl = new GenericUrl(url);
        try {
            HttpRequest request = requestFactory.buildGetRequest(genericUrl);
            request.getHeaders().set("CB-VERSION", "2017-09-08");
            HttpResponse response = request.execute();
            String content = response.parseAsString();
            Gson gson = new Gson();
            CoinApiResponse coinApiResponse = gson.fromJson(content, CoinApiResponse.class);
            return coinApiResponse;

        } catch (IOException e) {
            e.printStackTrace();
        }

        throw new Exception(String.format("Error retrieving %s-%s rate for %s", base, currency, rateType));
    }

    private static void log(Object o) {
        System.out.println(o);
    }
}
