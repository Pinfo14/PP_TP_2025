package demos;

import com.ppstudios.footballmanager.api.contracts.player.IPlayer;
import imports.Imports;
import league.League;

import java.io.IOException;

public class ExportDemos {
    public static void main(String[] args) {

        Imports imports = new Imports();

        IPlayer[] players = imports.importPlayers("Benfica.json");

        System.out.println(players[0].getName());

        try {
            players[0].exportToJson();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
