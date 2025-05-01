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
 *
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

    @Override
    public String getName() {
        return "";
    }

    @Override
    public LocalDate getBirthDate() {
        return null;
    }

    @Override
    public int getAge() {
        return 0;
    }

    @Override
    public String getNationality() {
        return "";
    }

    @Override
    public void setPosition(IPlayerPosition iPlayerPosition) {

    }

    @Override
    public String getPhoto() {
        return "";
    }


    @Override
    public int getNumber() {
        return 0;
    }

    @Override
    public int getShooting() {
        return 0;
    }

    @Override
    public int getPassing() {
        return 0;
    }

    @Override
    public int getStamina() {
        return 0;
    }

    @Override
    public int getSpeed() {
        return 0;
    }

    @Override
    public IPlayerPosition getPosition() {
        return null;
    }

    @Override
    public float getHeight() {
        return 0;
    }

    @Override
    public float getWeight() {
        return 0;
    }

    @Override
    public PreferredFoot getPreferredFoot() {
        return null;
    }

    @Override
    public void exportToJson() throws IOException {

    }
}
