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

    @Override
    public IPlayer selectPlayer(IClub iClub, IPlayerPosition iPlayerPosition) {
        verifyClub(iClub);
        verifyPosition(iPlayerPosition);
        verifyClubPlayers(iClub);

        IPlayer[] playersByPosition = getIPlayersByPosition(iClub.getPlayers(), iPlayerPosition);
        return playersByPosition[random(playersByPosition.length)];
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

        IPlayer[] positionPLayers = new IPlayer[numPlayers];
        int count = 0;

        for (IPlayer player : players) {
            if (player.getPosition().equals(position)) {
                positionPLayers[count++] = player;
            }
        }
        return positionPLayers;
    }

    private int random(int length) {
        return (int) (Math.random() * length);
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
