package team;

import com.ppstudios.footballmanager.api.contracts.player.IPlayer;
import com.ppstudios.footballmanager.api.contracts.player.IPlayerPosition;
import com.ppstudios.footballmanager.api.contracts.team.IClub;
import com.ppstudios.footballmanager.api.contracts.team.IPlayerSelector;
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

public class Club implements IClub {

    private static final int MINIMUM_PLAYERS = 10;

    private String name;
    private String code;
    private String country;
    private int foundedYear;
    private String stadiumName;
    private String logo;
    private Player[] players;
    private int playerCount;


    public Club(String name, String code, String country, int foundedYear, String stadiumName, String logo) {
        this.name = name;
        this.code = code;
        this.country = country;
        this.foundedYear = foundedYear;
        this.stadiumName = stadiumName;
        this.logo = logo;
        this.playerCount = 0;
        this.players = new Player[MINIMUM_PLAYERS];
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public IPlayer[] getPlayers() {
        return new IPlayer[0];
    }

    @Override

    public String getCode() {
        return this.code;
    }

    @Override
    public String getCountry() {
        return "";
    }

    @Override
    public int getFoundedYear() {
        return 0;
    }

    @Override
    public String getStadiumName() {
        return "";
    }

    @Override
    public String getLogo() {
        return "";
    }

    @Override
    public void addPlayer(IPlayer iPlayer) {

    }

    @Override
    public boolean isPlayer(IPlayer iPlayer) {
        return false;
    }

    @Override
    public void removePlayer(IPlayer iPlayer) {

    }

    @Override
    public int getPlayerCount() {
        return 0;
    }

    @Override
    public IPlayer selectPlayer(IPlayerSelector iPlayerSelector, IPlayerPosition iPlayerPosition) {
        return null;
    }

    @Override
    public boolean isValid() {
        return false;
    }

    @Override
    public void exportToJson() throws IOException {

    }
}
