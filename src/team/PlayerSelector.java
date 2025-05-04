package team;

import com.ppstudios.footballmanager.api.contracts.player.IPlayer;
import com.ppstudios.footballmanager.api.contracts.player.IPlayerPosition;
import com.ppstudios.footballmanager.api.contracts.team.IClub;
import com.ppstudios.footballmanager.api.contracts.team.IPlayerSelector;

public class PlayerSelector implements IPlayerSelector {
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

        for (IPlayer player : iClub.getPlayers()) {
            if (player.getPosition().equals(iPlayerPosition)) {
                return player;
            }
        }
        throw new IllegalStateException("Nao foi encontrado um jogador para a posicao" );
    }
}
