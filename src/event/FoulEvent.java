package event;

import com.ppstudios.footballmanager.api.contracts.player.IPlayer;

public class FoulEvent extends Event {



    public FoulEvent(String desciption, int minute) {
        super(desciption, minute);
    }
    //fazer subclass para o cartao aamarelo e vermelho
}
