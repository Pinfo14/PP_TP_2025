package event;

import com.ppstudios.footballmanager.api.contracts.event.IGoalEvent;
import com.ppstudios.footballmanager.api.contracts.player.IPlayer;

import java.io.IOException;


public class GoalEvent implements IGoalEvent {

    private IPlayer player;
    private int minute;
    private String description;

    public GoalEvent(IPlayer player, int minute, String description) {
        this.player = player;
        this.minute = minute;
        this.description = description;
    }

    @Override
    public IPlayer getPlayer() {
        return this.player;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public int getMinute() {
        return this.minute;
    }

    @Override
    public void exportToJson() throws IOException {

    }

    @Override
    public String toString() {
        return  " " + this.getDescription() + " " + this.getMinute() +" "+this.player.getName() +"\n";
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof GoalEvent goalEvent)) {
            return false;
        }
        GoalEvent goalEv = (GoalEvent) o;
        return this.minute == goalEv.getMinute()
                && this.description.equals(goalEv.getDescription())
                && this.player.equals(goalEv.getPlayer());
    }

}
