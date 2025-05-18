package event;

import com.ppstudios.footballmanager.api.contracts.player.IPlayer;
import com.ppstudios.footballmanager.api.contracts.team.IClub;

public class GoalKickEvent extends Event {


    private static final String DESCRIPTION = "Ponta pe de baliza para ";

    public GoalKickEvent(IClub club, int minute) {
        super(DESCRIPTION+club.getName(), minute);
    }
}
