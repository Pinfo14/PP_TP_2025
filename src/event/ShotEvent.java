package event;

import com.ppstudios.footballmanager.api.contracts.player.IPlayer;

public class ShotEvent extends Event {
    public ShotEvent(String description, IPlayer player, int minute) {
        super(description+player.getName(), minute);
    }
}
