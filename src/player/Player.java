package player;

import com.ppstudios.footballmanager.api.contracts.player.IPlayer;
import com.ppstudios.footballmanager.api.contracts.player.IPlayerPosition;
import com.ppstudios.footballmanager.api.contracts.player.PreferredFoot;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;


/**
 * Nome: Emanuel Jose Teixeira Pinto
 * Número: 8230371
 * Turma: LEI1T1
 * <p>
 * Nome: Roberto Cristiano Martins Faria
 * Número: 8230067
 * Turma: LEI1T2
 */
public class Player implements IPlayer, Cloneable {

    private String name;
    private LocalDate birthDate;
    private String nationality;
    private IPlayerPosition position;
    private String photo;
    private int number;
    private PlayerAttributes attributes;


    /**
     * Construtor completo para criar um jogador.
     */
    public Player(String name, LocalDate birthDate, String nationality, IPlayerPosition position, String photo, int number, PlayerAttributes attributes) {
        this.name = name;
        this.birthDate = birthDate;
        this.nationality = nationality;
        this.position = position;
        this.photo = photo;
        this.number = number;
        this.attributes = attributes;
    }


    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public LocalDate getBirthDate() {
        return this.birthDate;
    }

    @Override
    public int getAge() {
        return this.birthDate.getYear() - LocalDate.now().getYear();
    }

    @Override
    public String getNationality() {
        return this.nationality;
    }

    @Override
    public void setPosition(IPlayerPosition iPlayerPosition) {
        if (iPlayerPosition == null) {
            throw new IllegalArgumentException("Posição não pode ser nula");
        }
        this.position = iPlayerPosition;
    }

    @Override
    public String getPhoto() {
        return this.photo;
    }


    @Override
    public int getNumber() {
        return this.number;
    }

    @Override
    public int getShooting() {
        return this.attributes.getShooting();
    }

    @Override
    public int getPassing() {
        return this.attributes.getPassing();
    }

    @Override
    public int getStamina() {
        return this.attributes.getStamina();
    }

    @Override
    public int getSpeed() {
        return this.attributes.getSpeed();
    }

    @Override
    public IPlayerPosition getPosition() {
        return this.position;
    }

    @Override
    public float getHeight() {
        return this.attributes.height;
    }

    @Override
    public float getWeight() {
        return this.attributes.weight;
    }

    @Override
    public PreferredFoot getPreferredFoot() {
        return this.attributes.getPreferredFoot();
    }

    public int getDefence() {
        return this.attributes.getDefence();
    }


    @Override
    public String toString() {
        return
                "\n--------------\n" +
                        "name='" + name + "\n" +
                        "birthDate=" + birthDate + "\n" +
                        "nationality='" + nationality + "\n" +
                        "position=" + position + "\n" +
                        "number=" + number + "\n" +
                        "\n\n" + attributes.toString();

    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Player)) {
            return false;
        }
        Player player = (Player) obj;
        return this.number == player.getNumber() && this.name.equals(player.getName())
                && this.birthDate.equals(player.birthDate)
                && this.nationality.equals(player.nationality)
                && this.position.equals(player.position);
    }


    @Override
    public void exportToJson() throws IOException {

        String filename = this.name.replaceAll(" ", "_") + ".json";
        String path = "src/Files/saves/players/" + filename;

        File playerFile = new File(path);

        if(!playerFile.exists()){
            playerFile.createNewFile();
        }

        JSONObject playerJson = this.getJsonObject();

        // Escrever o JSON no arquivo
        FileWriter writer = new FileWriter(playerFile);
        try {
            writer.write(playerJson.toJSONString());
            System.out.println("Jogador exportado com sucesso para: " + path);

        }catch (IOException e){
            System.out.println("Erro ao exportar o jogador para o arquivo: " + path);
        }
        finally {
            writer.close();
        }

    }




    public JSONObject getJsonObject() {
        //cria um json object
        JSONObject player = new JSONObject();
        player.put("name", this.name);
        player.put("birthDate", this.birthDate.toString());
        player.put("nationality", this.nationality);
        player.put("position", this.position.getDescription());
        player.put("number", this.number);
        player.put("attributes", this.attributes.getJsonObject());
        return player;
    }


    @Override
    public Player clone() throws CloneNotSupportedException {

        try {
            PlayerAttributes attribute = this.attributes.clone();
            return new Player(this.name, this.birthDate, this.nationality, this.position, this.photo, this.number, attribute);

        } catch (CloneNotSupportedException e) {
            throw new CloneNotSupportedException("Erro ao clonar o atributo do player: " + this.name);
        }

    }
}
