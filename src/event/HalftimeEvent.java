package event;

import org.json.simple.JSONObject;

import java.io.IOException;

public class HalftimeEvent extends Event {
    private static final String DESCRIPTION = "Intervalo ";
    public HalftimeEvent( int minute) {
        super(DESCRIPTION, minute);
    }

    @Override
    public JSONObject getEventJson() {
        JSONObject object = new JSONObject();
        object.put("type",getEventName() );
        object.put("minute", this.getMinute());
        object.put("description", this.getDescription());
        return object;
    }

    @Override
    public String getEventName() {
        return "HalftimeEvent";
    }

    @Override
    public void exportToJson() throws IOException {

    }
}
