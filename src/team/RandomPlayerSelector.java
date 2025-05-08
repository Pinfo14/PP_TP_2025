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
        if (iClub == null) {
            throw new IllegalArgumentException("club não pode ser nulo");
        }
        if (iPlayerPosition == null) {
            throw new IllegalArgumentException("position não pode ser nula");
        }
        // equipa vazia?
        if (iClub.getPlayerCount() == 0) {
            throw new IllegalStateException("clube vazio");
        }

        IPlayer[] playersByPosition = getIPllayersByPosition(iClub.getPlayers(), iPlayerPosition);
        return playersByPosition[random(playersByPosition.length)];
    }

    private int countPlayersByPosition(IPlayer[] players, IPlayerPosition position) {
        int count = 0;
        for (IPlayer player : players) {
            if (player.getPosition().equals(position)) {
                count++;
            }
        }
        return count;
    }

    private IPlayer[] getIPllayersByPosition(IPlayer[] players, IPlayerPosition position) {

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

    private int random(int lenght) {
        return (int) (Math.random() * lenght);
    }
}
