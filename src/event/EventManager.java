package event;

import com.ppstudios.footballmanager.api.contracts.event.IEvent;
import com.ppstudios.footballmanager.api.contracts.event.IEventManager;


public class EventManager implements IEventManager {

    private static final int INIT_CAP=20;
    private static final int INCREMENT=2;

    private IEvent[] events;
    private int eventCount;

    public EventManager() {
        this.events = new IEvent[INIT_CAP];
        this.eventCount = 0;
    }


    @Override
    public void addEvent(IEvent iEvent) {
        if(iEvent == null) {
            throw new IllegalArgumentException("Event nao pode ser null");
        }
        if(isInEvent(iEvent)) {
            throw new IllegalStateException("Event "+ iEvent.getDescription()+" ao minuto "+iEvent.getMinute()+" ja existe");
        }
        if(this.eventCount == this.events.length) {
            increaseCapacity();
        }
        this.events[this.eventCount++] = iEvent;
    }

    @Override
    public IEvent[] getEvents() {
        IEvent[] eventsTemp = new Event[this.eventCount];
        for(int i = 0; i < this.eventCount; i++) {
            eventsTemp[i] = this.events[i];
        }
        return eventsTemp;
    }

    @Override
    public int getEventCount() {
        return this.eventCount;
    }

    @Override
    public String toString() {
        String s = "|||||||||| Eventos ||||||||||||\n";
        s += "Numero de eventos: " + this.eventCount + "\n";
        s += eventToString();
        return s;
    }


    private String eventToString() {
     String s = "";
      for(int i = 0; i < this.eventCount; i++) {
          if(this.events[i] != null) {
              s += this.events[i].toString() + "\n";
          }
      }
      return s;
    }

    private boolean isInEvent(IEvent event) {
       for(int i = 0; i < this.eventCount; i++) {
           if(this.events[i].equals(event)) {
               return true;
           }
       }
       return false;
    }

    private void increaseCapacity() {
        IEvent[] temp = new Event[this.events.length * INCREMENT];
        for(int i = 0; i < this.eventCount; i++) {
            temp[i] = this.events[i];
        }
        this.events = temp;
    }
}
