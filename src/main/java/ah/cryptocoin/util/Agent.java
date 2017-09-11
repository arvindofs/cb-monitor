package ah.cryptocoin.util;

import ah.cryptocoin.domain.Rate;
import ah.cryptocurrency.ui.CoinAgent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by arhariha on 9/10/17.
 */
public class Agent {
    private static List<CoinAgent> agents = new ArrayList<CoinAgent>();

    public static CoinAgent register(CoinAgent agent) {
        agents.add(agent);
        return agent;
    }

    public static CoinAgent release(CoinAgent agent) {
        agents.remove(agent);
        return agent;
    }


    public static void broadcast(Rate rate) {
        for (CoinAgent agent : agents) {
            agent.updateRate(rate);
        }
    }
}
