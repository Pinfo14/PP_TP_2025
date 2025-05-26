package event;

import com.ppstudios.footballmanager.api.contracts.data.IExporter;
import com.ppstudios.footballmanager.api.contracts.event.IEvent;
import com.ppstudios.footballmanager.api.contracts.event.IEventManager;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import player.Player;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;


public class EventManager implements IEventManager, IExporter {

    private static final int INIT_CAP=10;
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
        IEvent[] eventsTemp = new IEvent[this.eventCount];
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
        String s = "\n";
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
        IEvent[] temp = new IEvent[this.events.length * INCREMENT];
        for(int i = 0; i < this.eventCount; i++) {
            temp[i] = this.events[i];
        }
        this.events = temp;
    }

    private JSONArray getAllEventsJson(){
        JSONArray jsonArray = new JSONArray();

        for (int i = 0; i < this.eventCount; i++) {
            if(this.events[i] != null && this.events[i] instanceof Event) {
                jsonArray.add(((Event)this.events[i]).getEventJson());
            }
        }
        return jsonArray;
    }

    public JSONObject getEventJson(){
        JSONObject object = new JSONObject();
        object.put("eventCount", this.eventCount);
        object.put("events", getAllEventsJson());
        return object;
    }


    @Override
    public void exportToJson() throws IOException {
        String fileName = "src/Files/saves/events/EventManager_"+LocalDate.now()+".json";
        File file = new File(fileName);
    file.createNewFile();
        JSONObject object =  getEventJson();

        FileWriter fileWriter = new FileWriter(file);
        try {
            fileWriter.write(object.toJSONString());
            System.out.println("All Event exportado com sucesso para: " + fileName);
        }catch (IOException e){
            System.out.println("Erro ao exportar o All event para o arquivo: " + fileName);
        }finally {
            fileWriter.close();
        }

    }
}
