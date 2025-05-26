package event;

import com.ppstudios.footballmanager.api.contracts.player.IPlayer;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FoulEvent extends Event {

    private IPlayer autor;
    private IPlayer victim;

    public FoulEvent(String desciption, int minute, IPlayer player1, IPlayer player2) {
        super(desciption, minute);
        this.autor = player1;
        this.victim = player2;
    }

    @Override
    public JSONObject getEventJson() {
        JSONObject object = new JSONObject();
        object.put("type", getEventName());
        object.put("minute", this.getMinute());
        object.put("description", this.getDescription());
        object.put("autor", this.autor.getName());
        object.put("victim", this.victim.getName());
        return object;
    }

    @Override
    public String getEventName() {
        return "Foul";
    }

    @Override
    public void exportToJson() throws IOException {
        String fileName = "src/Files/saves/events/FoulEvent_" + this.autor.getName() + ".json";
        File file = new File(fileName);
        file.createNewFile();
        JSONObject object = getEventJson();

        FileWriter fileWriter = new FileWriter(file);
        try {
            fileWriter.write(object.toJSONString());
            System.out.println("Foul Event exportado com sucesso para: " + fileName);
        } catch (IOException e) {
            System.out.println("Erro ao exportar o foul event para o arquivo: " + fileName);
        } finally {
            fileWriter.close();
        }
    }

    //fazer subclass para o cartao aamarelo e vermelho


}
