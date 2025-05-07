package player;

import com.ppstudios.footballmanager.api.contracts.player.IPlayerPosition;
/**
 * Nome: Emanuel Jose Teixeira Pinto
 * Número: 8230371
 * Turma: LEI1T1
 * <p>
 * Nome: Roberto Cristiano Martins Faria
 * Número: 8230067
 * Turma: LEI1T2
 */
public class PlayerPosition implements IPlayerPosition {

    private final String description;

    public PlayerPosition(String description) {
        this.description = description;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public String toString() {
        String s = this.description;
        return s;
    }
}
