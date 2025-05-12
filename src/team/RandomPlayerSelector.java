package team;

import com.ppstudios.footballmanager.api.contracts.player.IPlayer;
import com.ppstudios.footballmanager.api.contracts.player.IPlayerPosition;
import com.ppstudios.footballmanager.api.contracts.team.IClub;
import com.ppstudios.footballmanager.api.contracts.team.IPlayerSelector;

/**
 * Nome: Emanuel Jose Teixeira Pinto
 * Número: 8230371
 * Turma: LEI1T1
 * <p>
 * Nome: Roberto Cristiano Martins Faria
 * Número: 8230067
 * Turma: LEI1T2
 */
public class RandomPlayerSelector implements IPlayerSelector {
    private boolean[] alreadySelected;
    private IPlayer[] lastPool;



    @Override
    public IPlayer selectPlayer(IClub iClub, IPlayerPosition iPlayerPosition) {
        verifyClub(iClub);
        verifyPosition(iPlayerPosition);
        verifyClubPlayers(iClub);

        IPlayer[] playersByPosition = getIPlayersByPosition(iClub.getPlayers(), iPlayerPosition);

        if (this.lastPool != playersByPosition) {
            this.lastPool = playersByPosition;
            this.alreadySelected = new boolean[playersByPosition.length];
        }

        int available = 0;
        for (int i = 0; i < this.alreadySelected.length; i++) {
            if (!this.alreadySelected[i]) available++;
        }

        if (available == 0) {
            throw new IllegalStateException("Todos os jogadores da posição " + iPlayerPosition.getDescription() + " já foram selecionados.");
        }

        int selectedIndex = getUniqueRandomIndex(this.alreadySelected);
        this.alreadySelected[selectedIndex] = true;
        return playersByPosition[selectedIndex];
    }

    private int getUniqueRandomIndex(boolean[] used) {
        int index;
        do {
            index = (int) (Math.random() * used.length);
        } while (used[index]);
        return index;
    }

    private int countPlayersByPosition(IPlayer[] players, IPlayerPosition position) {
        verifyPosition(position);
        verifyPlayers(players);
        int count = 0;
        for (IPlayer player : players) {
            if (player.getPosition().equals(position)) {
                count++;
            }
        }
        return count;
    }

    private IPlayer[] getIPlayersByPosition(IPlayer[] players, IPlayerPosition position) {
        int numPlayers = countPlayersByPosition(players, position);
        if (numPlayers == 0) {
            throw new IllegalStateException("Nao foi encontrado um jogador para a posicao: " + position.getDescription());
        }
        IPlayer[] positionPlayers = new IPlayer[numPlayers];
        int count = 0;
        for (IPlayer player : players) {
            if (player.getPosition().equals(position)) {
                positionPlayers[count++] = player;
            }
        }
        return positionPlayers;
    }

    private void verifyClubPlayers(IClub club) {
        if (club.getPlayerCount() == 0) {
            throw new IllegalStateException("clube vazio");
        }
    }

    private void verifyClub(IClub club) {
        if (club == null) {
            throw new IllegalArgumentException("club não pode ser nulo");
        }
    }

    private void verifyPosition(IPlayerPosition position) {
        if (position == null) {
            throw new IllegalArgumentException("Position não pode ser null");
        }
    }

    private void verifyPlayers(IPlayer[] players) {
        if (players == null) {
            throw new IllegalArgumentException("Array players não pode ser null");
        }
    }

}
