package demos;

import event.Event;
import event.EventManager;

public class EventsDemo {
    public static void main(String[] args) {

        Event ev = new Event("Falta",25);

        Event ev2 = new Event("Golo",26);
        Event ev3 = null;
        EventManager manager = new EventManager();

        try{
            manager.addEvent(ev);
           // manager.addEvent(ev);
            manager.addEvent(ev2);
           // manager.addEvent(ev3);
            System.out.println(manager);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }
}
