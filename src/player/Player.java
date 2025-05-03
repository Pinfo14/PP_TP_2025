package player;

import com.ppstudios.footballmanager.api.contracts.player.IPlayer;
import com.ppstudios.footballmanager.api.contracts.player.IPlayerPosition;
import com.ppstudios.footballmanager.api.contracts.player.PreferredFoot;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Nome: Emanuel Jose Teixeira Pinto
 * Número: 8230371
 * Turma: LEI1T1
 * <p>
 * Nome: <Nome completo do colega de grupo>
 * Número: <Número mecanográfico do colega de grupo>
 * Turma: <Turma do colega de grupo>
 */

public class Player implements IPlayer {

    private String name;
    private LocalDate birthDate;
    private String nationality;
    private PlayerPosition position;
    private String photo;
    private int number;

    private int shooting;
    private int passing;
    private int stamina;
    private int speed;

    private float height;
    private float weight;
    private PreferredFoot preferredFoot;

    /**
     * Construtor com dados base para criação de um jogador.
     */
    public Player(String name,LocalDate birthDate, String nationality, PlayerPosition position, String photo, int number) {
       this(name, birthDate, nationality, position, photo, number, 0, 0, 0, 0, 0, 0, null);
    }


    /**
     * Construtor completo para criação de um jogador.
     */
    public Player(String name, LocalDate birthDate, String nationality, PlayerPosition position, String photo, int number, int shooting, int passing, int stamina, int speed, float height, float weight, PreferredFoot preferredFoot) {
        this.name = name;
        this.birthDate = birthDate;
        this.nationality = nationality;
        this.position = position;
        this.photo = photo;
        this.number = number;

        this.shooting = shooting;
        this.passing = passing;
        this.stamina = stamina;
        this.speed = speed;

        this.height = height;
        this.weight = weight;
        this.preferredFoot = preferredFoot;
    }
    /**
     * Construtor para clone
     */
    private Player(Player outro) {
        this(outro.name, outro.birthDate, outro.nationality, outro.position,
                outro.photo, outro.number, outro.shooting, outro.passing,
                outro.stamina, outro.speed, outro.height, outro.weight,
                outro.preferredFoot);
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
        this.position = (PlayerPosition) iPlayerPosition;
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
        return this.shooting;
    }

    @Override
    public int getPassing() {
        return this.passing;
    }

    @Override
    public int getStamina() {
        return this.stamina;
    }

    @Override
    public int getSpeed() {
        return this.speed;
    }

    @Override
    public IPlayerPosition getPosition() {
        return this.position;
    }

    @Override
    public float getHeight() {
        return this.height;
    }

    @Override
    public float getWeight() {
        return this.weight;
    }

    @Override
    public PreferredFoot getPreferredFoot() {
        return this.preferredFoot;
    }

    @Override
    public void exportToJson() throws IOException {

    }


    @Override
    protected Object clone() throws CloneNotSupportedException {
        return new Player(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Player)) return false;
        Player player = (Player) obj;
        return number == player.number && Objects.equals(name, player.name)
                && Objects.equals(birthDate, player.birthDate)
                && Objects.equals(nationality, player.nationality)
                && Objects.equals(position, player.position)
                && Objects.equals(photo, player.photo);
    }

}
