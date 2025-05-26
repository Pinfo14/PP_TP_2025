package team;

import com.ppstudios.footballmanager.api.contracts.player.IPlayer;
import com.ppstudios.footballmanager.api.contracts.player.IPlayerPosition;
import com.ppstudios.footballmanager.api.contracts.team.IClub;
import com.ppstudios.footballmanager.api.contracts.team.IPlayerSelector;
import org.json.simple.*;
import player.Player;

import java.io.File;
import java.io.FileWriter;
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

public class Club implements IClub {

    private static final int MINIMUM_PLAYERS = 16;
    private static final int MAX_PLAYERS = 40;
    private static final int INCREMENT_FACTOR = 2;

    private String name;
    private String code;
    private String country;
    private int foundedYear;
    private String stadiumName;
    private String logo;
    private IPlayer[] players;
    private int playerCount;
    private boolean isCoach;
    private int maxPlayers;


    public Club(String name, String code, String country, int foundedYear, String stadiumName, String logo) {
        this.name = name;
        this.code = code;
        this.country = country;
        this.foundedYear = foundedYear;
        this.stadiumName = stadiumName;
        this.logo = logo;
        this.playerCount = 0;
        this.players = new Player[MINIMUM_PLAYERS];
        this.isCoach = false;
        this.maxPlayers = MAX_PLAYERS;
    }

    public Club(String name, String code, String country, int foundedYear, String stadiumName, String logo, int maxPlayers) {
        this.name = name;
        this.code = code;
        this.country = country;
        this.foundedYear = foundedYear;
        this.stadiumName = stadiumName;
        this.logo = logo;
        this.playerCount = 0;
        this.players = new Player[MINIMUM_PLAYERS];
        this.isCoach = false;
        this.maxPlayers = maxPlayers;
    }


    public Club(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public IPlayer[] getPlayers() {
        IPlayer[] players = new Player[this.playerCount];
        for (int i = 0; i < this.playerCount; i++) {
            try {
                players[i] = ((Player) this.players[i]).clone();
            } catch (CloneNotSupportedException e) {
                System.out.println("Erro ao clonar o player: " + this.players[i].getName());
            }
        }
        return players;
    }

    @Override

    public String getCode() {
        return this.code;
    }

    @Override
    public String getCountry() {
        return this.country;
    }

    @Override
    public int getFoundedYear() {
        return this.foundedYear;
    }

    @Override
    public String getStadiumName() {
        return this.stadiumName;
    }

    @Override
    public String getLogo() {
        return this.logo;
    }

    @Override
    public void addPlayer(IPlayer iPlayer) {
        if (iPlayer == null) {
            throw new IllegalArgumentException("Player nao pode ser nulo");
        }
        if (isPlayer(iPlayer)) {
            throw new IllegalArgumentException("Player já está no clube");
        }
        if (this.playerCount == this.maxPlayers) {
            throw new IllegalStateException("Clube está cheio");
        }

        if (this.playerCount == this.players.length) {
            expandArray();
        }
        this.players[this.playerCount++] = iPlayer;
    }


    private void expandArray() {
        IPlayer[] newPlayers = new Player[this.players.length + INCREMENT_FACTOR];
        for (int i = 0; i < this.players.length; i++) {
            newPlayers[i] = this.players[i];
        }
        this.players = newPlayers;
    }

    @Override
    public boolean isPlayer(IPlayer iPlayer) {
        if (iPlayer == null) {
            throw new IllegalArgumentException("Player nao pode ser nulo");
        }
        for (int i = 0; i < this.playerCount; i++) {
            if (this.players[i].equals(iPlayer)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void removePlayer(IPlayer iPlayer) {
        if (iPlayer == null) {
            throw new IllegalArgumentException("Player nao pode ser nulo");
        }

        int index = getPlayerIndex(iPlayer);
        if (index < 0) {
            throw new IllegalArgumentException("Player não está no clube");
        }
        for (int i = index; i < this.playerCount - 1; i++) {
            this.players[i] = this.players[i + 1];
        }
        this.players[this.playerCount - 1] = null;
        this.playerCount--;
    }


    @Override
    public int getPlayerCount() {
        return this.playerCount;
    }

    @Override
    public IPlayer selectPlayer(IPlayerSelector iPlayerSelector, IPlayerPosition iPlayerPosition) {
        if (iPlayerPosition == null) {
            throw new IllegalArgumentException("Position esta vazio");
        }
        return iPlayerSelector.selectPlayer(this, iPlayerPosition);
    }

    @Override
    public boolean isValid() {

        if (this.playerCount == 0) {
            throw new IllegalStateException("Clube não tem jogadores");
        }
        if (this.playerCount < MINIMUM_PLAYERS) {
            throw new IllegalStateException("Clube não tem pelo menos 16 jogadores");
        }
        if (!asGK()) {
            throw new IllegalStateException("Clube não tem pelo menos 1 guarda redes");
        }
        return true;
    }

    @Override
    public void exportToJson() throws IOException {
        String filename = this.code + ".json";
        String path = "src/Files/saves/clubs/" + filename;

        File clubFile = new File(path);


        if (!clubFile.exists()) {
            clubFile.createNewFile();
        }

        // Obter JSON do clube
        JSONObject clubJson = this.getJson();

        // Escrever no arquivo
        FileWriter writer = new FileWriter(clubFile);
        try {
            writer.write(clubJson.toJSONString());
            System.out.println("Clube exportado com sucesso para: " + path);
        } finally {
            writer.close();
        }
    }

    public JSONObject getJson() {
        JSONObject obj = new JSONObject();
        obj.put("name", this.name);
        obj.put("code", this.code);
        obj.put("country", this.country);
        obj.put("foundedYear", this.foundedYear);
        obj.put("stadiumName", this.stadiumName);
        obj.put("logo", this.logo);
        obj.put("players", getPlayersJson());
        obj.put("maxPlayers", this.maxPlayers);
        obj.put("playerCount", this.playerCount);
        return obj;
    }




    private JSONArray getPlayersJson() {
        JSONArray playersArray = new JSONArray();

        for (int i = 0; i < this.playerCount; i++) {
            if (this.players[i] instanceof Player) {
                Player player = (Player) this.players[i];
                playersArray.add(player.getJsonObject());
            }
        }

        return playersArray;
    }

    @Override
    public String toString() {
        return this.name;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Club)) {
            return false;
        }
        Club club = (Club) o;
        return this.name.equals(club.name)
                && this.code.equals(club.code)
                && this.country.equals(club.country)
                && this.foundedYear == club.foundedYear
                && this.stadiumName.equals(club.stadiumName);
    }


    /**
     * Metodo auxiliar para obter a posiçao do player no array
     */
    private int getPlayerIndex(IPlayer iPlayer) {
        for (int i = 0; i < this.playerCount; i++) {
            if (this.players[i].equals(iPlayer)) {
                return i;
            }
        }
        return -1;
    }

    private boolean asGK() {
        for (IPlayer player : this.players) {
            if (player.getPosition().getDescription().equals("Goalkeeper")) {
                return true;
            }
        }
        return false;
    }


}
