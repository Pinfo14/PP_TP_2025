package demos;

import com.ppstudios.footballmanager.api.contracts.event.IEvent;
import com.ppstudios.footballmanager.api.contracts.player.IPlayer;
import com.ppstudios.footballmanager.api.contracts.player.IPlayerPosition;
import com.ppstudios.footballmanager.api.contracts.team.IClub;
import event.*;
import player.Player;
import player.PlayerAttributes;
import player.PlayerPosition;
import team.Club;

import java.time.LocalDate;

public class EventsDemo {
    public static void main(String[] args) {

        LocalDate date = LocalDate.of(2003, 1, 1);
        IPlayerPosition st = new PlayerPosition("Striker");

        PlayerAttributes attributes = new PlayerAttributes();

        PlayerAttributes strikerAttributes =   attributes.generateAttributes(st.getDescription());

        IPlayer player = new Player("Emanuel",date,"Portugues", st,"sdfg",1 ,strikerAttributes);
        IClub club = new Club("Porto");

        IEvent Foul = new FoulEvent("player",25);
        IEvent shot = new ShotEvent("Remate de ",player,26);
        IEvent goal = new GoalEvent(player,26,"Goloooo");
        IEvent pass = new PassEvent("Passe",player,28);
        IEvent missShot = new ShotEvent("Remate falhado de ",player,29);
        IEvent goalKick = new GoalKickEvent(29,"pontape de baliza");


        IEvent ev3 = null;

        EventManager manager = new EventManager();
         IEvent[] events = {Foul,shot,goal,pass,missShot,goalKick};
         for(IEvent event : events) {
             try {
                 manager.addEvent(event);
             }catch (Exception e) {
                 System.out.println(e.getMessage());
             }

         }
            System.out.println(manager);
    }
}
