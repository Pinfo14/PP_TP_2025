package menus;

import com.ppstudios.footballmanager.api.contracts.player.IPlayer;

public class ListAllPlayers {

    public static void list(IPlayer[] players) {

        for(int i = 0; i < players.length; i++) {
            if(players[i] != null) {
                System.out.println((i+1) + " -> " + players[i].toString());
            }

        }


    }
}
