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
 * Nome: Roberto Cristiano Martins Faria
 * Número: 8230067
 * Turma: LEI1T2
 */
public class Team implements ITeam {

    private static final int MAX_PLAYERS = 11;

    private IClub club;
    private IFormation formation;
    private IPlayer[] squad;
    private int playerCount;
    private int defense;
    private int midfield;
    private int forward;
    private int striker;
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

        validatePlayerNotNull(iPlayer);
        validateFormationDefined();
        validateTeamNotFull();
        validatePlayerBelongsToClub(iPlayer);
        validatePlayerNotInTeam(iPlayer);
        validatePositionForFormation(iPlayer);

        incrementPosition(iPlayer);
        this.squad[this.playerCount++] = iPlayer;

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

        if (iPlayerPosition.getDescription().equals("Goalkeeper") && this.goalkeeper) {
            return false;
        }
        if (((Formation) this.formation).getNumAttackers() == this.forward && iPlayerPosition.getDescription().equals("Forward")) {
            return false;
        }
        if (((Formation) this.formation).getNumDefenders() == this.defense && iPlayerPosition.getDescription().equals("Defender")) {
            return false;
        }
        if (((Formation) this.formation).getNumMidfielders() == this.midfield && iPlayerPosition.getDescription().equals("Midfielder")) {
            return false;
        }
        if (((Formation) this.formation).getNumStrikers() == this.striker && iPlayerPosition.getDescription().equals("Striker")) {
            return false;
        }
        return true;
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

    @Override
    public String toString() {
        String s = "";
        for (IPlayer player : this.squad) {
            if (player != null) {
                s += player + "\n";
            }
        }
        return s;
    }

    private boolean isInTeam(IPlayer iPlayer) {
        validatePlayerNotNull(iPlayer);
        try {
            for (int i = 0; i < this.playerCount; i++) {
                if (this.squad[i].equals(iPlayer)) {
                    return true;
                }
            }
        } catch (NullPointerException e) {
            return false;
        }
        return false;
    }


    private void incrementPosition(IPlayer iPlayer) {
        String position = iPlayer.getPosition().getDescription();

        switch (position) {
            case "Goalkeeper":
                this.goalkeeper = true;
                break;
            case "Defender":
                this.defense++;
                break;
            case "Midfielder":
                this.midfield++;
                break;
            case "Forward":
                this.forward++;
                break;
            case "Striker":
                this.striker++;
                break;
            default:
                throw new IllegalStateException("Posicao invalida");
        }
    }

    private void validatePositionForFormation(IPlayer iPlayer) {
        if (!isValidPositionForFormation(iPlayer.getPosition())) {
            throw new IllegalStateException("Player: " + iPlayer.getName() + " Posicao " + iPlayer.getPosition().getDescription() + " invalida para a formation");
        }
    }

    private void validatePlayerNotNull(IPlayer p) {
        if (p == null) {
            throw new IllegalArgumentException("player não pode ser nulo");
        }
    }

    private void validateFormationDefined() {
        if (formation == null) {
            throw new IllegalStateException("nenhuma formation definida");
        }
    }

    private void validateTeamNotFull() {
        if (playerCount == MAX_PLAYERS) {
            throw new IllegalStateException("A team está cheia");
        }
    }

    private void validatePlayerBelongsToClub(IPlayer p) {
        if (!club.isPlayer(p)) {
            throw new IllegalStateException("Player não pertence ao clube");
        }
    }

    private void validatePlayerNotInTeam(IPlayer p) {
        if (isInTeam(p)) {
            throw new IllegalStateException("Player já está na team");
        }
    }
}
