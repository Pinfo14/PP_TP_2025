package demos;

import com.ppstudios.footballmanager.api.contracts.player.IPlayer;
import imports.Imports;
import team.Club;

public class Demo {
    public static void main(String[] args) {
        Imports importClubs = new Imports();

        Club[] club = importClubs.importClubs();
        for (Club c : club) {
            System.out.println(c.getName());
        }

        IPlayer[] playersPorto = importClubs.importPlayers("Porto.json");

        for(IPlayer p : playersPorto){
            try{
                club[1].addPlayer(p);
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
        }

        IPlayer[] playersBenfica = importClubs.importPlayers("Benfica.json");

        for(IPlayer p : playersBenfica){
            try{
                club[0].addPlayer(p);
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
        }

        System.out.println("---------------------------------------");
        for(IPlayer p : club[0].getPlayers()){
            System.out.println(p.getName());
        }

        System.out.println("---------------------------------------");
        for(IPlayer p : club[1].getPlayers()){
            System.out.println(p);
        }

    }
}
