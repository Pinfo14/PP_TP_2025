package simulation;

import com.ppstudios.footballmanager.api.contracts.event.IEvent;

import com.ppstudios.footballmanager.api.contracts.match.IMatch;
import com.ppstudios.footballmanager.api.contracts.player.IPlayer;
import com.ppstudios.footballmanager.api.contracts.player.IPlayerPosition;
import com.ppstudios.footballmanager.api.contracts.simulation.MatchSimulatorStrategy;

import com.ppstudios.footballmanager.api.contracts.team.ITeam;
import event.*;
import player.PlayerPositionManage;


public class MatchSimulator implements MatchSimulatorStrategy {

    private static final double EVENT_PROB = 0.1;
    private static final double HOME_OR_AWAY_PROB = 0.5;
    private static final double PASS_EVENT_PROB = 0.5;
    private static final double SHOT_EVENT_PROB = 0.8;



    private EventFactory factory;
    private PlayerPositionManage positionManager;

    public MatchSimulator() {
        this.factory = new EventFactory();
        this.positionManager = new PlayerPositionManage();
    }

    @Override
    public void simulate(IMatch iMatch) {

        ITeam homeTeam       = iMatch.getHomeTeam();
        ITeam awayTeam       = iMatch.getAwayTeam();

        for (int minuto = 1; minuto <= 90; minuto++) {
            IEvent ev;
            if(minuto == 45) {
                ev = new HalftimeEvent(minuto);
                iMatch.addEvent(ev);
            }

            if (Math.random() < EVENT_PROB) {
                boolean isHome = Math.random() < HOME_OR_AWAY_PROB;
                ITeam atacantes = isHome ? homeTeam : awayTeam;
                ITeam defensores = isHome ? awayTeam : homeTeam;

                IPlayer autor = getPlayer(atacantes);
                IPlayer alvo  = getPlayer(defensores);
                IPlayer gk  = getPlayer(defensores,this.positionManager.getPositionByDescription("Goalkeeper"));

                double tipo = Math.random();

                if (tipo < PASS_EVENT_PROB) {
                    ev = factory.generatePassEvent(autor, alvo, minuto, isHome);
                } else if (tipo > PASS_EVENT_PROB && tipo < SHOT_EVENT_PROB) {
                    ev = factory.generateShotEvent(autor, gk, minuto, isHome);
                } else {
                    ev = factory.generateFoulEvent(autor, alvo, minuto, isHome);
                }

                // Só regista se o evento for não-nulo (bem-sucedido)
                if (ev != null) {
                    iMatch.addEvent(ev);
                }
            }
        }
    }


    /**
     * Escolhe um jogador aleatoriamente do array devolvido por getPlayers() de uma posicao especifica.
     */

    private IPlayer getPlayer(ITeam team, IPlayerPosition position) {
        IPlayer[] v = team.getPlayers();
        for (IPlayer p : v) {
            if (p.getPosition().equals(position)) {
                return p;
            }
        }
        return null;
    }

    /**
     * Escolhe um jogador aleatoriamente do array devolvido por getPlayers().
     */
    private IPlayer getPlayer(ITeam team) {
        IPlayer[] v = team.getPlayers();
        int idx = (int) (Math.random() * v.length);
        return v[idx];
    }

}
