package simulation;

import com.ppstudios.footballmanager.api.contracts.event.IEventManager;
import com.ppstudios.footballmanager.api.contracts.match.IMatch;
import com.ppstudios.footballmanager.api.contracts.simulation.MatchSimulatorStrategy;
import event.EventManager;

public class MatchSimulator implements MatchSimulatorStrategy {


    @Override
    public void simulate(IMatch iMatch) {
        if (iMatch == null) {
            throw new IllegalArgumentException("match não pode ser null");
        }



    }
}
