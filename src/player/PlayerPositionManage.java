package player;

import com.ppstudios.footballmanager.api.contracts.player.IPlayerPosition;

public class PlayerPositionManage {

    private static final int NUM_POS = 5;

    private static IPlayerPosition[] positions;


    public PlayerPositionManage() {
       this.positions = new IPlayerPosition[] {
                new PlayerPosition("Striker"),
                new PlayerPosition("Forward"),
                new PlayerPosition("Defender"),
                new PlayerPosition("Goalkeeper"),
                new PlayerPosition("Midfielder")
        };
    }



    /**
     * Devolve uma cópia do array de posições,
     * para não expor o array original ao exterior.
     */
    public IPlayerPosition[] getPositions() {
        IPlayerPosition[] copy = new IPlayerPosition[NUM_POS];
        for (int i = 0; i < NUM_POS; i++) {
            copy[i] = positions[i];
        }
        return copy;
    }

    /**
     * Procura e devolve a posição com a descrição fornecida.
     * @param description descrição exata da posição (case-sensitive)
     * @return IPlayerPosition correspondente, ou null se não encontrar
     */
    public IPlayerPosition getPositionByDescription(String description) {
        if (description == null) {
            return null;
        }
        for (IPlayerPosition pos : positions) {
            if (pos.getDescription().equals(description)) {
                return pos;
            }
        }
        return null;
    }
}
