package imports;

import com.ppstudios.footballmanager.api.contracts.league.ILeague;
import com.ppstudios.footballmanager.api.contracts.player.IPlayerPosition;
import com.ppstudios.footballmanager.api.contracts.player.PreferredFoot;
import com.ppstudios.footballmanager.api.contracts.team.IClub;
import league.League;
import league.Season;
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
public class ImportSaveGame {

    private static final String SAVE_DIRECTORY = "src/Files/SaveGames/";
    private static final String CLUBS_FILE = "all_clubs.json";
    private static final String LEAGUE_EXTENSION = "_league.json";

    /**
     * Importa uma liga completa a partir de um ficheiro JSON
     * @param leagueName Nome da liga a carregar
     * @return Liga carregada ou null se houver erro
     */
    public ILeague importLeague(String leagueName) {
        try {
            // Primeiro carregar todos os clubes disponíveis
            IClub[] allClubs = importAllClubs();

            // Carregar o ficheiro da liga
            String leaguePath = SAVE_DIRECTORY + leagueName + LEAGUE_EXTENSION;
            File leagueFile = new File(leaguePath);

            if (!leagueFile.exists()) {
                System.out.println("Ficheiro da liga não encontrado: " + leaguePath);
                return null;
            }

            JSONParser parser = new JSONParser();
            FileReader reader = new FileReader(leagueFile);
            JSONObject leagueJson = (JSONObject) parser.parse(reader);
            reader.close();

            // Criar e popular a liga
            return createLeagueFromJson(leagueJson, allClubs);

        } catch (Exception e) {
            System.out.println("Erro ao importar liga '" + leagueName + "': " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Lista todos os saves de ligas disponíveis
     * @return Array com nomes das ligas salvas
     */
    public String[] listAvailableLeagues() {
        File saveDir = new File(SAVE_DIRECTORY);

        if (!saveDir.exists()) {
            throw new IllegalStateException("Directory nao existe");
        }

        String[] files = saveDir.list();
        if (files == null) {
            throw new IllegalStateException("Nao existe, Save Files ");
        }

        int leagueCount = countFiles(files);


        // Extrair nomes das ligas
        String[] leagueNames = new String[leagueCount];
        int index = 0;

        for (String file : files) {
            if (file.endsWith(LEAGUE_EXTENSION)) {
                // Remover a extensão para obter o nome da liga
                String leagueName = file.substring(0, file.length() - LEAGUE_EXTENSION.length());
                leagueNames[index++] = leagueName;
            }
        }

        return leagueNames;
    }



    private int countFiles(String[] files){
        int leagueCount = 0;
        for (String file : files) {
            if (file.endsWith(LEAGUE_EXTENSION)) {
                leagueCount++;
            }
        }
        return leagueCount;
    }

    /**
     * Cria uma liga a partir do JSON
     */
    private ILeague createLeagueFromJson(JSONObject leagueJson, IClub[] allClubs) {
        String name = (String) leagueJson.get("name");
        League league = new League(name);

        // Carregar temporadas
        JSONArray seasonsArray = (JSONArray) leagueJson.get("seasons");
        if (seasonsArray != null) {
            for (Object seasonObj : seasonsArray) {
                JSONObject seasonJson = (JSONObject) seasonObj;
                Season season = createSeasonFromJson(seasonJson, allClubs);
                if (season != null) {
                    league.createSeason(season);
                }
            }
        }

        System.out.println("Liga '" + name + "' importada com sucesso!");
        return league;
    }

    /**
     * Cria uma temporada a partir do JSON
     */
    private Season createSeasonFromJson(JSONObject seasonJson, IClub[] allClubs) {
        try {
            // Extrair dados básicos
            String name = (String) seasonJson.get("name");
            int year = ((Long) seasonJson.get("year")).intValue();

            // Dados opcionais com valores default
            int coachingClubIndex = getIntValue(seasonJson, "coachingClubIndex", -1);
            int currentRound = getIntValue(seasonJson, "currentRound", 1);
            int pointsPerWin = getIntValue(seasonJson, "pointsPerWin", 3);
            int pointsPerDraw = getIntValue(seasonJson, "pointsPerDraw", 1);
            int pointsPerLoss = getIntValue(seasonJson, "pointsPerLoss", 0);

            // Criar temporada
            Season season = new Season(name, year, coachingClubIndex, currentRound,
                    pointsPerWin, pointsPerDraw, pointsPerLoss);

            // Adicionar clubes através dos standings
            addClubsToSeason(season, seasonJson, allClubs);

            loadMatchesFromJson(season, seasonJson);

            return season;

        } catch (Exception e) {
            System.out.println("Erro ao criar temporada: " + e.getMessage());
            return null;
        }
    }



    private void loadMatchesFromJson(Season season,JSONObject seasonJson){

    }

    /**
     * Adiciona clubes à temporada baseado nos standings
     */
    private void addClubsToSeason(Season season, JSONObject seasonJson, IClub[] allClubs) {
        if (!seasonJson.containsKey("standings")) {
            return;
        }

        JSONArray standingsArray = (JSONArray) seasonJson.get("standings");

        for (Object standingObj : standingsArray) {
            JSONObject standingJson = (JSONObject) standingObj;
            String clubCode = (String) standingJson.get("Club");

            IClub club = findClubByCode(allClubs, clubCode);
            if (club != null) {
                try {
                    season.addClub(club);
                } catch (Exception e) {
                    System.out.println("Erro ao adicionar clube " + clubCode + ": " + e.getMessage());
                }
            } else {
                System.out.println("Clube não encontrado: " + clubCode);
            }
        }
    }

    /**
     * Importa todos os clubes disponíveis
     */
    private IClub[] importAllClubs() {
        // Primeiro tentar carregar do ficheiro de save
        File clubsFile = new File(SAVE_DIRECTORY + CLUBS_FILE);

        if (clubsFile.exists()) {
            try {
                return importClubsFromSaveFile();
            } catch (Exception e) {
                System.out.println("Erro ao carregar clubes do save, usando dados default: " + e.getMessage());
            }
        }

        // Fallback para dados default
        System.out.println("A carregar clubes dos saves default...");
        Imports defaultImports = new Imports();
        return defaultImports.importPlayersAndClub();
    }

    /**
     * Importa clubes do ficheiro de save
     */
    private IClub[] importClubsFromSaveFile() throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        FileReader reader = new FileReader(SAVE_DIRECTORY + CLUBS_FILE);
        JSONArray clubsArray = (JSONArray) parser.parse(reader);
        reader.close();

        IClub[] clubs = new Club[clubsArray.size()];

        for (int i = 0; i < clubsArray.size(); i++) {
            JSONObject clubJson = (JSONObject) clubsArray.get(i);
            clubs[i] = createClubFromJson(clubJson);
        }

        return clubs;
    }

    /**
     * Cria um clube a partir do JSON
     */
    private IClub createClubFromJson(JSONObject clubJson) {
        String name = (String) clubJson.get("name");
        String code = (String) clubJson.get("code");
        String country = (String) clubJson.get("country");
        int foundedYear = ((Long) clubJson.get("foundedYear")).intValue();
        String stadiumName = (String) clubJson.get("stadiumName");
        String logo = (String) clubJson.get("logo");
        int maxPlayers = getIntValue(clubJson, "maxPlayers", 40);

        Club club = new Club(name, code, country, foundedYear, stadiumName, logo, maxPlayers);

        // Adicionar jogadores se existirem
        if (clubJson.containsKey("players")) {
            JSONArray playersArray = (JSONArray) clubJson.get("players");
            addPlayersToClub(club, playersArray);
        }

        return club;
    }

    /**
     * Adiciona jogadores a um clube
     */
    private void addPlayersToClub(Club club, JSONArray playersArray) {
        for (Object playerObj : playersArray) {
            JSONObject playerJson = (JSONObject) playerObj;

            try {
                Player player = createPlayerFromJson(playerJson);
                club.addPlayer(player);
            } catch (Exception e) {
                System.out.println("Erro ao adicionar jogador ao clube " + club.getCode() + ": " + e.getMessage());
            }
        }
    }

    /**
     * Cria um jogador a partir do JSON
     */
    private Player createPlayerFromJson(JSONObject playerJson) {
        String name = (String) playerJson.get("name");
        LocalDate birthDate = LocalDate.parse((String) playerJson.get("birthDate"));
        String nationality = (String) playerJson.get("nationality");
        String photo = (String) playerJson.get("photo");
        int number = getIntValue(playerJson, "number", 0);

        String positionStr = (String) playerJson.get("position");
        IPlayerPosition position = new PlayerPosition(positionStr);

        PlayerAttributes attributes = createAttributesFromJson(playerJson, positionStr);

        return new Player(name, birthDate, nationality, position, photo, number, attributes);
    }

    /**
     * Cria atributos de jogador a partir do JSON
     */
    private PlayerAttributes createAttributesFromJson(JSONObject playerJson, String position) {
        if (!playerJson.containsKey("attributes")) {
            // Gerar atributos default
            PlayerAttributes attr = new PlayerAttributes();
            return attr.generateAttributes(position);
        }

        JSONObject attributesJson = (JSONObject) playerJson.get("attributes");

        int shooting = getIntValue(attributesJson, "shooting", 50);
        int passing = getIntValue(attributesJson, "passing", 50);
        int stamina = getIntValue(attributesJson, "stamina", 50);
        int speed = getIntValue(attributesJson, "speed", 50);
        int defence = getIntValue(attributesJson, "defence", 50);

        float height = getFloatValue(attributesJson, "height", 1.80f);
        float weight = getFloatValue(attributesJson, "weight", 75.0f);

        PreferredFoot preferredFoot = PreferredFoot.Right;
        if (attributesJson.containsKey("preferredFoot")) {
            String footStr = (String) attributesJson.get("preferredFoot");
            try {
                preferredFoot = PreferredFoot.fromString(footStr);
            } catch (Exception e) {
                // Manter default se houver erro
            }
        }

        return new PlayerAttributes(shooting, passing, stamina, speed, height, weight, defence, preferredFoot);
    }

    /**
     * Encontra um clube pelo código
     */
    private IClub findClubByCode(IClub[] clubs, String code) {
        for (IClub club : clubs) {
            if (club.getCode().equals(code)) {
                return club;
            }
        }
        return null;
    }

    /**
     * Obtém um valor inteiro do JSON com fallback
     */
    private int getIntValue(JSONObject json, String key, int defaultValue) {
        if (!json.containsKey(key)) {
            return defaultValue;
        }
        long value = (long)json.get(key);
        return ((Long) value).intValue();
    }

    /**
     * Obtém um valor float do JSON com fallback
     */
    private float getFloatValue(JSONObject json, String key, float defaultValue) {
        if (!json.containsKey(key)) {
            return defaultValue;
        }
        Object value = json.get(key);
        if (value == null) {
            return defaultValue;
        }

      float floatValue = ((Double) value).floatValue();

        return floatValue;
    }

}