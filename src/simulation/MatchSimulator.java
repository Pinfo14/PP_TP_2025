package simulation;

import com.ppstudios.footballmanager.api.contracts.event.IEvent;
import com.ppstudios.footballmanager.api.contracts.event.IEventManager;
import com.ppstudios.footballmanager.api.contracts.match.IMatch;
import com.ppstudios.footballmanager.api.contracts.player.IPlayer;
import com.ppstudios.footballmanager.api.contracts.simulation.MatchSimulatorStrategy;
import com.ppstudios.footballmanager.api.contracts.team.ITeam;
import event.*;

public class MatchSimulator implements MatchSimulatorStrategy {

    private IEventManager eventManager;

    public MatchSimulator(IEventManager eventManager) {
        this.eventManager = eventManager;
    }


    @Override
    public void simulate(IMatch iMatch) {

        ITeam homeTeam = iMatch.getHomeTeam();
        ITeam awayTeam = iMatch.getAwayTeam();



    }
}
