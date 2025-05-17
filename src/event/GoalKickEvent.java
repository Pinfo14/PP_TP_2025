package event;

import com.ppstudios.footballmanager.api.contracts.player.IPlayer;
import com.ppstudios.footballmanager.api.contracts.team.IClub;

public class GoalKickEvent extends Event {




    public GoalKickEvent( int minute,String description) {
        super(description, minute);
    }
}
