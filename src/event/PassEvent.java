package event;

import com.ppstudios.footballmanager.api.contracts.player.IPlayer;

public class PassEvent extends Event {

    public PassEvent(String description ,IPlayer player, int minute) {
        super(description+player.getName(), minute);
    }
}
