package event;

import com.ppstudios.footballmanager.api.contracts.player.IPlayer;

public class FoulEvent extends Event {

    private static final String DESCRIPTION = "Falta cometida por ";

    public FoulEvent(IPlayer player, int minute) {
        super(DESCRIPTION+player.getName(), minute);
    }
}
