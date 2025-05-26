package demos;

import com.ppstudios.footballmanager.api.contracts.event.IEvent;
import com.ppstudios.footballmanager.api.contracts.player.IPlayer;
import com.ppstudios.footballmanager.api.contracts.player.IPlayerPosition;
import com.ppstudios.footballmanager.api.contracts.team.IClub;
import com.ppstudios.footballmanager.api.contracts.team.ITeam;
import event.*;
import imports.Imports;
import league.League;
import player.Player;
import player.PlayerAttributes;
import player.PlayerPosition;
import simulation.GenerateTeams;
import team.Club;

import java.io.IOException;
import java.time.LocalDate;

public class ExportDemos {
    public static void main(String[] args) {

        Imports imports = new Imports();

       /* IPlayer[] players = imports.importPlayers("Benfica.json");

        System.out.println(players[0].getName());

        try {
            players[0].exportToJson();
        } catch (IOException e) {
            System.out.println(e.getMessage());*/
/*
        IClub[] clubs = imports.importPlayersToClub();

        try{
            clubs[0].exportToJson();
        }catch (IOException e){
            System.out.println(e.getMessage());
        }*/

/*
        IClub[] clubs = imports.importPlayersToClub();

        GenerateTeams generateTeams = new GenerateTeams();


            ITeam team = generateTeams.randomTeam(clubs[0]);
            System.out.println(clubs[0].getName());



        try {
            team.exportToJson();
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
*/

        LocalDate date = LocalDate.of(2003, 1, 1);
        IPlayerPosition st = new PlayerPosition("Striker");

        PlayerAttributes attributes = new PlayerAttributes();

        PlayerAttributes strikerAttributes =   attributes.generateAttributes(st.getDescription());

        IPlayer player = new Player("Emanuel",date,"Portugues", st,"sdfg",1 ,strikerAttributes);

        IPlayer player2 = new Player("quim",date,"Portugues", st,"sdfg",1 ,strikerAttributes);

        IEvent Foul = new FoulEvent("player",25, player,player2);
        IEvent goal = new GoalEvent(player,26,"Goloooo");
        IEvent pass = new PassEvent("Passe",28,player2);
        IEvent goalKick = new GoalKickEvent(29,"pontape de baliza",player);

        try {
            Foul.exportToJson();
            goal.exportToJson();
            pass.exportToJson();
            goalKick.exportToJson();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }


        EventManager manager = new EventManager();
        IEvent[] events = {Foul,goal,pass,goalKick};
        for(IEvent event : events) {
            try {
                manager.addEvent(event);
            }catch (Exception e) {
                System.out.println(e.getMessage());
            }

        }

        try {
            Foul.exportToJson();
            goal.exportToJson();
            pass.exportToJson();
            goalKick.exportToJson();
            manager.exportToJson();
        } catch (IOException e) {
           System.out.println(e.getMessage());
        }
    }
}
