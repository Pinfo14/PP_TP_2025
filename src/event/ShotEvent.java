package event;

import com.ppstudios.footballmanager.api.contracts.player.IPlayer;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ShotEvent extends Event {


    private IPlayer player;


    public ShotEvent(String description, int minute, IPlayer player) {
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
        return "ShotEvent";
    }

    @Override
    public void exportToJson() throws IOException {

        String fileName = "src/Files/saves/events/PassEvent_"+this.player.getName()+".json";
        File file = new File(fileName);
        file.createNewFile();
        JSONObject object =  getEventJson();

        FileWriter fileWriter = new FileWriter(file);
        try {
            fileWriter.write(object.toJSONString());
            System.out.println("Pass  Event exportado com sucesso para: " + fileName);
        }catch (IOException e){
            System.out.println("Erro ao exportar o goal Pass para o arquivo: " + fileName);
        }finally {
            fileWriter.close();
        }
    }
}
