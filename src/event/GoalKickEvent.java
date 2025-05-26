package event;


import com.ppstudios.footballmanager.api.contracts.player.IPlayer;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class GoalKickEvent extends Event {

    private IPlayer player;


    public GoalKickEvent( int minute,String description,IPlayer player) {
        super(description, minute);
        this.player = player;
    }



    @Override
    public JSONObject getEventJson() {
        JSONObject object = new JSONObject();
        object.put("type",getEventName() );
        object.put("minute", this.getMinute());
        object.put("description", this.getDescription());
        object.put("autor", this.player.getName());
        return object;
    }

    @Override
    public String getEventName() {
        return "GoalKick";
    }

    @Override
    public void exportToJson() throws IOException {
        String fileName = "src/Files/saves/events/GoalKick_"+this.player.getName()+".json";
        File file = new File(fileName);

        JSONObject object =  getEventJson();

        FileWriter fileWriter = new FileWriter(file);
        try {
            fileWriter.write(object.toJSONString());
            System.out.println("GoalKick Event exportado com sucesso para: " + fileName);
        }catch (IOException e){
            System.out.println("Erro ao exportar o goal kick event para o arquivo: " + fileName);
        }finally {
            fileWriter.close();
        }
    }
}
