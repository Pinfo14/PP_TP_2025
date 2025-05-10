package simulation;

import com.ppstudios.footballmanager.api.contracts.event.IEvent;
import com.ppstudios.footballmanager.api.contracts.event.IEventManager;
import com.ppstudios.footballmanager.api.contracts.match.IMatch;
import com.ppstudios.footballmanager.api.contracts.player.IPlayer;
import com.ppstudios.footballmanager.api.contracts.simulation.MatchSimulatorStrategy;
import event.*;

public class MatchSimulator implements MatchSimulatorStrategy {

    private static final double PROB_GOAL = 0.05;
    private static final double PROB_SHOT = 0.10;
    private static final double PROB_FOUL = 0.10;
    private static final double PROB_PASS = 0.20;
    private static final double PROB_BAD_PASS_RATE = 0.30; // 30% dos passes são falhados
    private static final double PROB_GOALKICK = 0.05;

    private static final double LIMIT_GOAL       = PROB_GOAL;
    private static final double LIMIT_SHOT       = LIMIT_GOAL + PROB_SHOT;
    private static final double LIMIT_FOUL       = LIMIT_SHOT + PROB_FOUL;
    private static final double LIMIT_PASS       = LIMIT_FOUL + PROB_PASS;
    private static final double LIMIT_GOALKICK   = LIMIT_PASS + PROB_GOALKICK;

    private IEventManager eventManager;

    public MatchSimulator() {
        this.eventManager = new EventManager();
    }

    @Override
    public void simulate(IMatch iMatch) {
        verifyNullMatch(iMatch);
        verifyPlayed(iMatch);
        verifyValidMatch(iMatch);

        IPlayer[] homePlayers = iMatch.getHomeTeam().getPlayers();
        IPlayer[] awayPlayers = iMatch.getAwayTeam().getPlayers();

        for (int min = 1; min <= 90; min=min+2) {
            double rand = Math.random();
            IPlayer jogador;
            IEvent evento = null;

            if(min == 45){
                evento = new HalftimeEvent(min);
                min=min+3;
            }

            if (rand < LIMIT_GOAL) {
                jogador = getRandomPlayer(homePlayers);
                evento = new GoalEvent(jogador, min, "Golo de ");

            } else if (rand < LIMIT_SHOT) {
                jogador = getRandomPlayer(awayPlayers);
                evento = new ShotEvent("Remate falhado por ", jogador, min);

            } else if (rand < LIMIT_FOUL) {
                jogador = getRandomPlayer(Math.random() < 0.5 ? homePlayers : awayPlayers);
                evento = new FoulEvent(jogador, min);

            } else if (rand < LIMIT_PASS) {
                jogador = getRandomPlayer(Math.random() < 0.5 ? homePlayers : awayPlayers);

                evento = new PassEvent("passe feito por: ",jogador, min);

            } else if (rand < LIMIT_GOALKICK) {
                evento = new GoalKickEvent(iMatch.getHomeClub(), min);
            }

            if (evento != null) {
                iMatch.addEvent(evento);
                eventManager.addEvent(evento);
            }
        }

        iMatch.setPlayed();
    }

    private IPlayer getRandomPlayer(IPlayer[] players) {
        int total = players.length;
        int index = (int) (Math.random() * total);
        return players[index];
    }

    private void verifyPlayed(IMatch iMatch) {
        if (iMatch.isPlayed()) {
            throw new IllegalStateException("O jogo já foi jogado.");
        }
    }

    private void verifyNullMatch(IMatch iMatch) {
        if (iMatch == null) {
            throw new IllegalArgumentException("match não pode ser null");
        }
    }

    private void verifyValidMatch(IMatch iMatch) {
        if (!iMatch.isValid()) {
            throw new IllegalStateException("O jogo não é válido.");
        }
    }
}
