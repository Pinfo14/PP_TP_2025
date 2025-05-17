package player;

import com.ppstudios.footballmanager.api.contracts.player.IPlayer;
import com.ppstudios.footballmanager.api.contracts.player.IPlayerPosition;
import com.ppstudios.footballmanager.api.contracts.player.PreferredFoot;

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
public class Player implements IPlayer {

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

    public int getDefence(){
        return this.attributes.getDefence();
    }

    @Override
    public void exportToJson() throws IOException {

    }


    @Override
    public String toString() {
        return
                "\n--------------\n"+
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
                && this.position.equals(player.position)
                && this.photo.equals(player.photo);
    }

}
