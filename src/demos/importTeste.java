package demos;

import com.ppstudios.footballmanager.api.contracts.team.IClub;
import imports.Imports;


public class importTeste {
    public static void main(String[] args) {
        Imports importClubs = new Imports();
        IClub[] club = importClubs.importPlayersToClub();
        for (IClub c : club) {
            System.out.println(c.getName());
            System.out.println(c.getPlayerCount());
        }
    }
}
