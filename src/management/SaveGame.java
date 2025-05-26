package management;

import com.ppstudios.footballmanager.api.contracts.league.ILeague;
import com.ppstudios.footballmanager.api.contracts.league.ISeason;
import com.ppstudios.footballmanager.api.contracts.league.IStanding;
import com.ppstudios.footballmanager.api.contracts.match.IMatch;
import com.ppstudios.footballmanager.api.contracts.player.IPlayer;
import com.ppstudios.footballmanager.api.contracts.player.IPlayerPosition;
import com.ppstudios.footballmanager.api.contracts.player.PreferredFoot;
import com.ppstudios.footballmanager.api.contracts.team.IClub;
import imports.Imports;
import league.League;
import league.Season;
import league.Standing;
import match.Match;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import player.Player;
import player.PlayerAttributes;
import player.PlayerPosition;
import team.Club;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;

/**
 * Nome: Emanuel Jose Teixeira Pinto
 * Número: 8230371
 * Turma: LEI1T1
 *
 * Nome: Roberto Cristiano Martins Faria
 * Número: 8230067
 * Turma: LEI1T2
 */
public class SaveGame {

    private static final String SAVE_DIRECTORY = "src/Files/SaveGames/";
    private static final String CLUBS_FILE = "all_clubs.json";
    private static final String LEAGUE_FILE = "_league.json";

    public SaveGame() {
        // Criar diretório de saves se não existir
        createSaveDirectory();
    }

    /**
     * Cria o diretório de saves se não existir
     */
    private void createSaveDirectory() {
        File directory = new File(SAVE_DIRECTORY);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    /**
     * Guarda um jogo completo (liga com todas as épocas)
     */
    public void saveGame(ILeague league) throws IOException {
        if (league == null) {
            throw new IllegalArgumentException("Liga não pode ser nula");
        }

        System.out.println("A guardar jogo: " + league.getName() + "...");

        // Guardar todos os clubes com jogadores
        saveAllClubs();

        // Guardar a liga
        saveLeague(league);

        System.out.println("Jogo guardado com sucesso: " + league.getName());
    }

    /**
     * Carrega um jogo guardado
     */
    public ILeague loadGame(String leagueName) throws IOException {
        if (leagueName == null || leagueName.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome da liga não pode ser vazio");
        }

        System.out.println("A carregar jogo: " + leagueName + "...");

        // Carregar todos os clubes
        IClub[] clubs = loadAllClubs();

        // Carregar a liga
        ILeague league = loadLeague(leagueName, clubs);

        System.out.println("Jogo carregado com sucesso: " + leagueName);
        return league;
    }

    /**
     * Carrega uma liga específica
     */
    private ILeague loadLeague(String leagueName, IClub[] clubs) throws IOException {
        JSONParser parser = new JSONParser();
        String leaguePath = SAVE_DIRECTORY + leagueName + LEAGUE_FILE;

        try {
            FileReader reader = new FileReader(leaguePath);
            JSONObject leagueJson = (JSONObject) parser.parse(reader);

            String name = (String) leagueJson.get("name");

            // Criar liga
            League league = new League(name);

            // Carregar épocas
            Season[] seasons = loadSeasons(leagueJson, clubs);

            for (Season season : seasons) {
                if (season != null) {
                    league.createSeason(season);
                }
            }

            return league;

        } catch (ParseException e) {
            throw new IOException("Erro ao fazer parse do ficheiro da liga: " + e.getMessage());
        }
    }

    /**
     * Carrega todas as épocas da liga
     */
    private Season[] loadSeasons(JSONObject leagueJson, IClub[] clubs) throws IOException {
        JSONArray seasonsJson = (JSONArray) leagueJson.get("seasons");

        if (seasonsJson == null) {
            return new Season[0];
        }

        Season[] seasons = new Season[seasonsJson.size()];

        int i = 0;
        for (Object obj : seasonsJson) {
            JSONObject seasonJson = (JSONObject) obj;

            // Extrair dados básicos da temporada
            int year = ((Long) seasonJson.get("year")).intValue();
            String name = (String) seasonJson.get("name");

            int coachingClubIndex = -1;
            if (seasonJson.containsKey("coachingClubIndex")) {
                coachingClubIndex = ((Long) seasonJson.get("coachingClubIndex")).intValue();
            }

            int currentRound = 1;
            if (seasonJson.containsKey("currentRound")) {
                currentRound = ((Long) seasonJson.get("currentRound")).intValue();
            }

            int pointsPerWin = 3;
            int pointsPerDraw = 1;
            int pointsPerLoss = 0;

            if (seasonJson.containsKey("pointsPerWin")) {
                pointsPerWin = ((Long) seasonJson.get("pointsPerWin")).intValue();
            }
            if (seasonJson.containsKey("pointsPerDraw")) {
                pointsPerDraw = ((Long) seasonJson.get("pointsPerDraw")).intValue();
            }
            if (seasonJson.containsKey("pointsPerLoss")) {
                pointsPerLoss = ((Long) seasonJson.get("pointsPerLoss")).intValue();
            }

            // Criar época
            Season season = new Season(name, year, coachingClubIndex, currentRound,
                    pointsPerWin, pointsPerDraw, pointsPerLoss);

            // Adicionar clubes à temporada e restaurar standings
            if (seasonJson.containsKey("standings")) {
                JSONArray standingsArray = (JSONArray) seasonJson.get("standings");
                loadStandingsIntoSeason(season, standingsArray, clubs);
            }

            // Restaurar estado do calendário se existir
            if (seasonJson.containsKey("schedule")) {
                loadScheduleState(seasonJson, season);
            }

            seasons[i++] = season;
        }

        return seasons;
    }

    /**
     * Carrega os standings para uma época
     */
    private void loadStandingsIntoSeason(Season season, JSONArray standingsArray, IClub[] clubs) throws IOException {
        for (Object standingObj : standingsArray) {
            JSONObject standingJson = (JSONObject) standingObj;
            String clubCode = (String) standingJson.get("Club");

            // Encontrar o clube pelo código
            IClub club = findClubByCode(clubs, clubCode);
            if (club != null) {
                // Adicionar clube à época (isto cria automaticamente o standing)
                season.addClub(club);

                // Restaurar estatísticas do standing
                restoreStandingStats(season, club, standingJson);
            }
        }
    }

    /**
     * Restaura as estatísticas de um standing
     */
    private void restoreStandingStats(Season season, IClub club, JSONObject standingJson) {
        IStanding[] standings = season.getLeagueStandings();

        // Encontrar o standing deste clube
        for (IStanding standing : standings) {
            if (standing != null && standing instanceof Standing) {
                Standing s = (Standing) standing;
                if (s.getClub().equals(club)) {
                    // Restaurar estatísticas
                    int points = getIntFromJson(standingJson, "Points", 0);
                    int wins = getIntFromJson(standingJson, "Wins", 0);
                    int draws = getIntFromJson(standingJson, "Draws", 0);
                    int losses = getIntFromJson(standingJson, "Losses", 0);
                    int goalsScored = getIntFromJson(standingJson, "GoalsScored", 0);
                    int goalsConceded = getIntFromJson(standingJson, "GoalsConceded", 0);

                    // Aplicar estatísticas diretamente
                    s.addPoints(points);
                    s.addGoalsScored(goalsScored);
                    s.addGoalsConceded(goalsConceded);

                    // Adicionar vitórias, empates e derrotas sem pontos (já foram adicionados)
                    for (int w = 0; w < wins; w++) {
                        s.addWin(0);
                    }
                    for (int d = 0; d < draws; d++) {
                        s.addDraw(0);
                    }
                    for (int l = 0; l < losses; l++) {
                        s.addLoss(0);
                    }

                    break;
                }
            }
        }
    }

    /**
     * Método auxiliar para obter int do JSON com valor padrão
     */
    private int getIntFromJson(JSONObject json, String key, int defaultValue) {
        if (json.containsKey(key)) {
            return ((Long) json.get(key)).intValue();
        }
        return defaultValue;
    }

    /**
     * Encontra um clube pelo código
     */
    private IClub findClubByCode(IClub[] clubs, String code) {
        if (clubs == null || code == null) {
            return null;
        }

        for (IClub club : clubs) {
            if (club != null && club.getCode().equals(code)) {
                return club;
            }
        }
        return null;
    }

    /**
     * Carrega o estado do calendário (jogos realizados, etc.)
     */
    private void loadScheduleState(JSONObject seasonJson, Season season) throws IOException {
        JSONObject scheduleJson = (JSONObject) seasonJson.get("schedule");

        if (scheduleJson == null) {
            return;
        }

        // Por enquanto, apenas gerar o calendário normal
        // Para implementação completa, seria necessário:
        // - Restaurar jogos já realizados
        // - Restaurar resultados dos jogos
        // - Restaurar equipas definidas para cada jogo
        // - Restaurar eventos dos jogos

        // Isto é complexo e requer métodos adicionais nas interfaces
        System.out.println("Estado do calendário não totalmente restaurado (implementação futura)");
    }

    /**
     * Carrega todos os clubes do ficheiro guardado
     */
    private IClub[] loadAllClubs() throws IOException {
        JSONParser parser = new JSONParser();
        String clubsPath = SAVE_DIRECTORY + CLUBS_FILE;

        try {
            FileReader reader = new FileReader(clubsPath);
            JSONArray clubsArray = (JSONArray) parser.parse(reader);

            IClub[] clubs = new Club[clubsArray.size()];

            int i = 0;
            for (Object obj : clubsArray) {
                JSONObject clubJson = (JSONObject) obj;

                String name = (String) clubJson.get("name");
                String code = (String) clubJson.get("code");
                String country = (String) clubJson.get("country");
                int foundedYear = ((Long) clubJson.get("foundedYear")).intValue();
                String stadiumName = (String) clubJson.get("stadiumName");
                String logo = (String) clubJson.get("logo");
                int maxPlayers = ((Long) clubJson.get("maxPlayers")).intValue();

                // Carregar jogadores
                IPlayer[] players = loadPlayersFromClubJson(clubJson);

                Club club = new Club(name, code, country, foundedYear, stadiumName, logo, maxPlayers);

                // Adicionar jogadores ao clube
                for (IPlayer player : players) {
                    if (player != null) {
                        try {
                            club.addPlayer(player);
                        } catch (Exception e) {
                            System.out.println("Erro ao adicionar jogador " + player.getName() + " ao clube " + club.getName());
                        }
                    }
                }

                clubs[i++] = club;
            }

            return clubs;

        } catch (ParseException e) {
            throw new IOException("Erro ao fazer parse do ficheiro de clubes: " + e.getMessage());
        }
    }

    /**
     * Carrega jogadores de um clube do JSON
     */
    private IPlayer[] loadPlayersFromClubJson(JSONObject clubJson) throws IOException {
        JSONArray playersArray = (JSONArray) clubJson.get("players");

        if (playersArray == null) {
            return new IPlayer[0];
        }

        IPlayer[] players = new IPlayer[playersArray.size()];

        int i = 0;
        for (Object obj : playersArray) {
            JSONObject playerJson = (JSONObject) obj;

            String name = (String) playerJson.get("name");
            LocalDate birthDate = LocalDate.parse((String) playerJson.get("birthDate"));
            String nationality = (String) playerJson.get("nationality");
            String photo = (String) playerJson.get("photo");

            int number = 0;
            if (playerJson.containsKey("number") && playerJson.get("number") != null) {
                number = ((Long) playerJson.get("number")).intValue();
            }

            String playerPos = (String) playerJson.get("position");
            IPlayerPosition position = new PlayerPosition(playerPos);

            PlayerAttributes attributes = loadPlayerAttributesFromJson(playerJson);

            Player player = new Player(name, birthDate, nationality, position, photo, number, attributes);
            players[i++] = player;
        }

        return players;
    }

    /**
     * Carrega atributos de jogador do JSON
     */
    private PlayerAttributes loadPlayerAttributesFromJson(JSONObject playerJson) throws IOException {
        JSONObject attributesJson = (JSONObject) playerJson.get("attributes");

        if (attributesJson == null) {
            throw new IOException("Atributos do jogador não encontrados no JSON");
        }

        int shooting = ((Long) attributesJson.get("shooting")).intValue();
        int passing = ((Long) attributesJson.get("passing")).intValue();
        int stamina = ((Long) attributesJson.get("stamina")).intValue();
        int speed = ((Long) attributesJson.get("speed")).intValue();
        int defence = ((Long) attributesJson.get("defence")).intValue();

        float height = ((Double) attributesJson.get("height")).floatValue();
        float weight = ((Double) attributesJson.get("weight")).floatValue();

        PreferredFoot preferredFoot = PreferredFoot.fromString((String) attributesJson.get("preferredFoot"));

        return new PlayerAttributes(shooting, passing, stamina, speed, height, weight, defence, preferredFoot);
    }

    /**
     * Guarda todos os clubes com os seus jogadores
     */
    private void saveAllClubs() throws IOException {
        // Importar todos os clubes com jogadores
        Imports imports = new Imports();
        IClub[] clubs = imports.importPlayersToClub();

        JSONArray clubsJson = new JSONArray();
        for (IClub club : clubs) {
            if (club instanceof Club) {
                Club c = (Club) club;
                clubsJson.add(c.getJson());
            }
        }

        String clubsPath = SAVE_DIRECTORY + CLUBS_FILE;
        FileWriter writer = new FileWriter(clubsPath);
        try {
            writer.write(clubsJson.toJSONString());
            System.out.println("Todos os clubes exportados para: " + clubsPath);
        } catch (IOException e) {
            System.out.println("Erro ao exportar os clubes para o arquivo: " + clubsPath);
            throw e;
        } finally {
            writer.close();
        }
    }

    /**
     * Guarda uma liga
     */
    private void saveLeague(ILeague league) throws IOException {
        JSONObject leagueJson = ((League) league).getLeagueJson();

        String leaguePath = SAVE_DIRECTORY + league.getName() + LEAGUE_FILE;
        FileWriter writer = new FileWriter(leaguePath);
        try {
            writer.write(leagueJson.toJSONString());
            System.out.println("Liga exportada para: " + leaguePath);
        } catch (IOException e) {
            System.out.println("Erro ao exportar a liga para o arquivo: " + leaguePath);
            throw e;
        } finally {
            writer.close();
        }
    }

    /**
     * Lista todos os jogos guardados
     */
    public String[] listSavedGames() {
        File directory = new File(SAVE_DIRECTORY);

        if (!directory.exists() || !directory.isDirectory()) {
            System.out.println("Diretório de saves não existe");
            return new String[0];
        }

        String[] files = directory.list();

        if (files == null || files.length == 0) {
            System.out.println("Não existem jogos guardados");
            return new String[0];
        }

        // Filtrar apenas ficheiros de liga
        String[] leagueFiles = new String[files.length];
        int count = 0;

        for (String file : files) {
            if (file.endsWith(LEAGUE_FILE)) {
                // Remover a extensão para mostrar apenas o nome da liga
                String leagueName = file.substring(0, file.length() - LEAGUE_FILE.length());
                leagueFiles[count++] = leagueName;
            }
        }

        // Criar array do tamanho correto
        String[] result = new String[count];
        for (int i = 0; i < count; i++) {
            result[i] = leagueFiles[i];
        }

        return result;
    }

    /**
     * Verifica se um jogo existe
     */
    public boolean gameExists(String leagueName) {
        if (leagueName == null || leagueName.trim().isEmpty()) {
            return false;
        }

        String leaguePath = SAVE_DIRECTORY + leagueName + LEAGUE_FILE;
        File file = new File(leaguePath);
        return file.exists();
    }

    /**
     * Apaga um jogo guardado
     */
    public boolean deleteGame(String leagueName) {
        if (leagueName == null || leagueName.trim().isEmpty()) {
            return false;
        }

        String leaguePath = SAVE_DIRECTORY + leagueName + LEAGUE_FILE;
        File file = new File(leaguePath);

        if (file.exists()) {
            boolean deleted = file.delete();
            if (deleted) {
                System.out.println("Jogo apagado: " + leagueName);
            } else {
                System.out.println("Erro ao apagar jogo: " + leagueName);
            }
            return deleted;
        }

        return false;
    }

    /**
     * Obtém informações sobre um jogo guardado
     */
    public String getGameInfo(String leagueName) {
        if (!gameExists(leagueName)) {
            return "Jogo não existe";
        }

        try {
            JSONParser parser = new JSONParser();
            String leaguePath = SAVE_DIRECTORY + leagueName + LEAGUE_FILE;
            FileReader reader = new FileReader(leaguePath);
            JSONObject leagueJson = (JSONObject) parser.parse(reader);

            StringBuilder info = new StringBuilder();
            info.append("Liga: ").append(leagueJson.get("name")).append("\n");
            info.append("Número de épocas: ").append(leagueJson.get("numberOfSeasons")).append("\n");

            JSONArray seasons = (JSONArray) leagueJson.get("seasons");
            if (seasons != null && seasons.size() > 0) {
                JSONObject lastSeason = (JSONObject) seasons.get(seasons.size() - 1);
                info.append("Última época: ").append(lastSeason.get("year")).append("\n");
                info.append("Jornada atual: ").append(lastSeason.get("currentRound")).append("\n");

                if (lastSeason.containsKey("coachingClubIndex")) {
                    int coachIndex = ((Long) lastSeason.get("coachingClubIndex")).intValue();
                    if (coachIndex >= 0) {
                        info.append("A treinar: Sim\n");
                    } else {
                        info.append("A treinar: Não\n");
                    }
                }
            }

            return info.toString();

        } catch (Exception e) {
            return "Erro ao ler informações do jogo";
        }
    }
}