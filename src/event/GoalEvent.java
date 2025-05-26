package event;

import com.ppstudios.footballmanager.api.contracts.event.IGoalEvent;
import com.ppstudios.footballmanager.api.contracts.player.IPlayer;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class GoalEvent implements IGoalEvent {

    private IPlayer player;
    private int minute;
    private String description;

    public GoalEvent(IPlayer player, int minute, String description) {
        this.player = player;
        this.minute = minute;
        this.description = description;
    }

    @Override
    public IPlayer getPlayer() {
        return this.player;
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

        String fileName = "src/Files/saves/events/GoalEvent_"+this.player.getName()+".json";
        File file = new File(fileName);
file.createNewFile();
        JSONObject object =  getEventJson();

        FileWriter fileWriter = new FileWriter(file);
        try {
            fileWriter.write(object.toJSONString());
            System.out.println("Goal Event exportado com sucesso para: " + fileName);
        }catch (IOException e){
            System.out.println("Erro ao exportar o goal event para o arquivo: " + fileName);
        }finally {
            fileWriter.close();
        }

    }

    public JSONObject getEventJson(){
        JSONObject object = new JSONObject();
        object.put("type",getEventName() );
        object.put("autor", this.player.getName());
        object.put("minute", this.minute);
        object.put("description", this.description);
        return object;
    }


    @Override
    public String toString() {
        return  " " + this.getDescription() + " " + this.getMinute() +" "+this.player.getName() +"\n";
    }

    public String getEventName() {
        return "Goal";
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof GoalEvent goalEvent)) {
            return false;
        }
        GoalEvent goalEv = (GoalEvent) o;
        return this.minute == goalEv.getMinute()
                && this.description.equals(goalEv.getDescription())
                && this.player.equals(goalEv.getPlayer());
    }

}
