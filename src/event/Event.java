package event;

import com.ppstudios.footballmanager.api.contracts.event.IEvent;

import java.io.IOException;

public class Event implements IEvent {

    @Override
    public String getDescription() {
        return "";
    }

    @Override
    public int getMinute() {
        return 0;
    }

    @Override
    public void exportToJson() throws IOException {

    }
}
