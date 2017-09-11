package ah.cryptocurrency.ui;

import ah.cryptocoin.domain.Rate;

/**
 * Created by arhariha on 9/10/17.
 */
public interface CoinAgent {
    void updateRate(Rate rate);
}
