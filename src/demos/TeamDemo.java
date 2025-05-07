package demos;

import com.ppstudios.footballmanager.api.contracts.player.IPlayer;
import com.ppstudios.footballmanager.api.contracts.player.IPlayerPosition;
import com.ppstudios.footballmanager.api.contracts.team.IFormation;
import com.ppstudios.footballmanager.api.contracts.team.IPlayerSelector;
import com.ppstudios.footballmanager.api.contracts.team.ITeam;
import imports.Imports;
import player.Player;
import player.PlayerPosition;
import team.Club;
import team.Formation;
import team.RandomPlayerSelector;
import team.Team;

import java.time.LocalDate;

public class TeamDemo {
    public static void main(String[] args) {
        Imports importClubs = new Imports();

        Club[] club = importClubs.importClubs();
        for (Club c : club) {
            System.out.println(c.getName());
        }

        System.out.println("---------------------------------------");

        LocalDate date = LocalDate.of(2003, 1, 1);
        IPlayerPosition gk = new PlayerPosition("Goalkeeper");
        IPlayerPosition def = new PlayerPosition("Defender");
        IPlayerPosition striker = new PlayerPosition("Striker");

        Player player = new Player("Emanuel",date,"Portugues", gk,"sdfg",1 );
        Player player2 = new Player("Info",date,"Portugues", def,"sdfg",2 );
        Player player3 = new Player("wfas",date,"Portugues", def,"sdfg",5 );
        Player player4 = new Player("ffgmb",date,"Portugues", def,"sdfg",8 );
        Player player5 = new Player("nthgfvb",date,"Portugues", def,"sdfg",85);
        Player player6 = new Player("das",date,"Portugues", striker,"sdfg",86 );

        club[0].addPlayer(player2);
        club[0].addPlayer(player);
        club[0].addPlayer(player3);
        club[0].addPlayer(player4);
        club[0].addPlayer(player5);
        club[0].addPlayer(player6);
        System.out.println(club[0].toString());

        //club[0].removePlayer(player2);
      //  System.out.println(club[0]);


        IPlayerSelector selector = new RandomPlayerSelector();


        try{
           IPlayer play = club[0].selectPlayer(selector, def);
           System.out.println(play.toString());
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

        IFormation formation = new Formation("4-3-2-1", 4, 3, 2,1);

        System.out.println("\n\n TEAM \n\n");

        ITeam team = new Team(club[0]);
        team.setFormation(formation);

        try{
            team.addPlayer(player);
            team.addPlayer(player2);
            team.addPlayer(player3);
            team.addPlayer(player4);
            team.addPlayer(player5);
            team.addPlayer(player6);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

        System.out.println(team.toString());
    }
}
