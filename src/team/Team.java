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
 * <p>
 * Nome: <Nome completo do colega de grupo>
 * Número: <Número mecanográfico do colega de grupo>
 * Turma: <Turma do colega de grupo>
 */
public class Team implements ITeam {

    private static final int MAX_PLAYERS = 11;

    private IClub club;
    private IFormation formation;
    private Player[] squad;
    private int playerCount;
    private int defense;
    private int midfield;
    private int forward;
    private boolean goalkeeper;


    public Team(IClub club) {
        this.club = club;
        this.squad = new Player[MAX_PLAYERS];
        this.playerCount = 0;
    }

    @Override
    public IClub getClub() {
        return this.club;
    }

    @Override
    public IFormation getFormation() {
        return this.formation;
    }

    @Override
    public IPlayer[] getPlayers() {
        IPlayer[] players = new IPlayer[this.playerCount];
        for (int i = 0; i < this.playerCount; i++) {
            players[i] = this.squad[i];
        }
        return players;
    }

    @Override
    public void addPlayer(IPlayer iPlayer) {
        if (iPlayer == null) {
            throw new IllegalArgumentException("player não pode ser nulo");
        }
        if (this.formation == null) {
            throw new IllegalStateException("nenhuma formation definida");
        }

        if (this.playerCount == MAX_PLAYERS ) {
            throw new IllegalStateException("A team esta cheia");
        }

        if (!club.isPlayer(iPlayer)) {
            throw new IllegalStateException("Player nao pertence ao clube");
        }
        if (isInTeam(iPlayer)){
            throw new IllegalStateException("Player ja esta na team");
        }

        if (!isValidPositionForFormation(iPlayer.getPosition())) {
            throw new IllegalStateException("Posicao invalida para a formation");
        }
        this.squad[this.playerCount++] = (Player) iPlayer;
    }


    @Override
    public int getPositionCount(IPlayerPosition iPlayerPosition) {
        int count = 0;
        for (IPlayer player : this.squad) {
            if (player.getPosition().equals(iPlayerPosition)) {
                count++;
            }
        }
        return count;
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
        this.formation = iFormation;
    }

    @Override
    public void exportToJson() throws IOException {

    }

    private boolean isInTeam(IPlayer iPlayer) {
        if (iPlayer == null) {
            throw new IllegalArgumentException("player não pode ser nulo");
        }
        for(IPlayer player: this.squad){
            if(player.equals(iPlayer)){
                return true;
            }
        }
        return false;
    }


}
