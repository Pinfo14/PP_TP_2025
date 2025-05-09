package demos;

import com.ppstudios.footballmanager.api.contracts.event.IEvent;
import com.ppstudios.footballmanager.api.contracts.player.IPlayer;
import com.ppstudios.footballmanager.api.contracts.player.IPlayerPosition;
import event.Event;
import event.EventManager;
import event.GoalEvent;
import player.Player;
import player.PlayerPosition;

import java.time.LocalDate;

public class EventsDemo {
    public static void main(String[] args) {

        LocalDate date = LocalDate.of(2003, 1, 1);
        IPlayerPosition st = new PlayerPosition("Striker");
        IPlayer player = new Player("Emanuel",date,"Portugues", st,"sdfg",1 );
        IEvent ev = new Event("Falta",25);
        IEvent ev2 = new Event("Passe",25);
        IEvent ev3 = null;
        IEvent ev4 = new GoalEvent(player,26,"Goloooo");
        EventManager manager = new EventManager();
         IEvent[] events = {ev, ev2, ev3,ev4};
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
