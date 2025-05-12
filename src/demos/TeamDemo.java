package demos;

import com.ppstudios.footballmanager.api.contracts.player.IPlayer;
import com.ppstudios.footballmanager.api.contracts.player.IPlayerPosition;
import com.ppstudios.footballmanager.api.contracts.team.IFormation;
import com.ppstudios.footballmanager.api.contracts.team.IPlayerSelector;
import com.ppstudios.footballmanager.api.contracts.team.ITeam;
import imports.Imports;
import player.Player;
import player.PlayerAttributes;
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

        PlayerAttributes attributes = new PlayerAttributes();

        PlayerAttributes defAttributes =   attributes.generateAttributes(def.getDescription());
        PlayerAttributes strikerAttributes =   attributes.generateAttributes(striker.getDescription());
        PlayerAttributes gkAttributes =   attributes.generateAttributes(gk.getDescription());



        IPlayer player = new Player("Emanuel",date,"Portugues", gk,"sdfg",1 ,gkAttributes);
        IPlayer NotInteam = new Player("Alfredo",date,"Portugues", gk,"sdfg",10 ,gkAttributes);
        IPlayer player2 = new Player("Roberto",date,"Portugues", def,"sdfg",2 ,defAttributes);
        IPlayer player3 = new Player("wfas",date,"Portugues", def,"sdfg",5 ,defAttributes);
        IPlayer player4 = new Player("ffgmb",date,"Portugues", def,"sdfg",8 ,defAttributes);
        IPlayer player5 = new Player("nthgfvb",date,"Portugues", def,"sdfg",85,defAttributes);
        IPlayer player6 = new Player("das",date,"Portugues", striker,"sdfg",86 ,strikerAttributes);

        club[0].addPlayer(player2);
        club[0].addPlayer(player);
        club[0].addPlayer(player3);
        club[0].addPlayer(player4);
        club[0].addPlayer(player5);
        club[0].addPlayer(player6);

        IPlayer[] players ={NotInteam,player,player2,player3,player4,player5,player6};



        IPlayerSelector selector = new RandomPlayerSelector();


        try{
            IPlayer play = club[0].selectPlayer(selector, def);
            System.out.println("Random defesa: \n"+play.toString());
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

        IFormation formation4321 = new Formation("4-3-2-1", 4, 3, 2,1);

        System.out.println("\n\n TEAM \n\n");
        System.out.println(formation4321.getDisplayName());
        ITeam team = new Team(club[0]);
        team.setFormation(formation4321);


        for(IPlayer p : players){
            try{
                team.addPlayer(p);
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
        }



        System.out.println(team);
    }
}