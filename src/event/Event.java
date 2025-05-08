package event;

import com.ppstudios.footballmanager.api.contracts.event.IEvent;

import java.io.IOException;

public class Event implements IEvent {

    private String description;
    private int minute;

    public Event(String description, int minute) {
        this.description = description;
        this.minute = minute;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public int getMinute() {
        return this.minute;
    }

    @Override
    public void exportToJson() throws IOException {

    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Event)){
            return false;
        }
        Event ev = (Event) o;
        return this.minute==ev.getMinute() && this.description.equals(ev.getDescription());
    }

    @Override
    public String toString() {
     String s = "Event: " + this.description + " Minuto: " + this.minute + "\n";
     return s;
    }
}
