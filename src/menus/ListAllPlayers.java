package menus;

import com.ppstudios.footballmanager.api.contracts.player.IPlayer;
import reader.Reader;



public class ListAllPlayers {

    private static void  header() {
        System.out.println("\n----+-------------------------------------+-------------+-----------");
        System.out.println(String.format("%-3s | %-20s | %-11s | %-3s | %-105s", "ID", "NOME", "POSITION", "AGE", "Atributos"));
        System.out.println("----+-------------------------------------+-------------+-----------");
    }

    public static void indexPlayer(IPlayer[] players) {

        if (players.length == 0 || players == null) {
            System.out.println("Não há jogadores.");
        }

        header();
        for(int i = 0; i < players.length; i++) {
            if(players[i] != null) {


                System.out.println(String.format("%-3s | %-20s | %-11s | %-3s | %-105s", ""+(i+1), players[i].getName(), players[i].getPosition(),players[i].getAge(), players[i].toString()));
                /*
                System.out.println(String.format("%-3s | %-35s | %-3s | %-3s | %-105s", i+1, "", "","aaa", ""));
                System.out.println("----+-------------------------------------+----+----+-----------");

                System.out.println((i+1) + " -> " + players[i].toString());*/
            }

        }

    }


}
