package team;

import com.ppstudios.footballmanager.api.contracts.player.IPlayer;
import com.ppstudios.footballmanager.api.contracts.player.IPlayerPosition;
import com.ppstudios.footballmanager.api.contracts.team.IClub;
import com.ppstudios.footballmanager.api.contracts.team.IFormation;
import com.ppstudios.footballmanager.api.contracts.team.ITeam;
import player.Player;

import java.io.IOException;
/**
 * Nome: Emanuel Jose Teixeira Pinto
 * Número: 8230371
 * Turma: LEI1T1
 *
 * Nome: <Nome completo do colega de grupo>
 * Número: <Número mecanográfico do colega de grupo>
 * Turma: <Turma do colega de grupo>
 */
public class Team implements ITeam {

    private Club club;
    private IFormation formation;
    private Player[] players;


    @Override
    public IClub getClub() {
        return null;
    }

    @Override
    public IFormation getFormation() {
        return null;
    }

    @Override
    public IPlayer[] getPlayers() {
        return new IPlayer[0];
    }

    @Override
    public void addPlayer(IPlayer iPlayer) {

    }

    @Override
    public int getPositionCount(IPlayerPosition iPlayerPosition) {
        return 0;
    }

    @Override
    public boolean isValidPositionForFormation(IPlayerPosition iPlayerPosition) {
        return false;
    }

    @Override
    public int getTeamStrength() {
        return 0;
    }

    @Override
    public void setFormation(IFormation iFormation) {

    }

    @Override
    public void exportToJson() throws IOException {

    }
}
