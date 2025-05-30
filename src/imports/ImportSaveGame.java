package imports;

import com.ppstudios.footballmanager.api.contracts.event.IEvent;
import com.ppstudios.footballmanager.api.contracts.league.ILeague;
import com.ppstudios.footballmanager.api.contracts.match.IMatch;
import com.ppstudios.footballmanager.api.contracts.player.IPlayer;
import com.ppstudios.footballmanager.api.contracts.player.IPlayerPosition;
import com.ppstudios.footballmanager.api.contracts.player.PreferredFoot;
import com.ppstudios.footballmanager.api.contracts.team.IClub;
import com.ppstudios.footballmanager.api.contracts.team.ITeam;
import event.*;
import league.League;
import league.Schedule;
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
import team.Formation;
import team.Team;
import util.Logs;

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
    private static final Logs logger = new Logs();
    private static final String FORMATION_KEY = "Formation";
    private static final String SQUAD_KEY = "Squad";



    private IClub[] allClubs;

    public ImportSaveGame(){
        this.allClubs = importAllClubs();
    }

    /**
     * Importa uma liga completa a partir de um ficheiro JSON
     * @param leagueName Nome da liga a carregar
     * @return Liga carregada ou null se houver erro
     */
    public ILeague importLeague(String leagueName) {
        try {
            // Primeiro carregar todos os clubes disponíveis
           if (allClubs == null) {
               logger.writeLog("Array de clubes vazio");
               return null;
           }

            // Carregar o ficheiro da liga
            String leaguePath = SAVE_DIRECTORY + leagueName + LEAGUE_EXTENSION;
            File leagueFile = new File(leaguePath);

            if (!leagueFile.exists()) {
                logger.writeLog("Ficheiro da liga não encontrado: " + leaguePath);
                return null;
            }

            JSONParser parser = new JSONParser();
            FileReader reader = new FileReader(leagueFile);
            JSONObject leagueJson = (JSONObject) parser.parse(reader);
            reader.close();
            
            return createLeagueFromJson(leagueJson, allClubs);

        } catch (Exception e) {
            logger.writeLog("Erro ao importar liga '" + leagueName + "': " + e.getMessage());
            return null;
        }
    }

    /**
     * Lista os nomes das ligas disponíveis no diretório de save.
     *
     * @return Um array de strings contendo os nomes das ligas disponíveis.
     * @throws IllegalStateException Se o diretório de save não existir ou
     *                               se não houver nenhum ficheiro de saveGame válido.
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

        int leagueCount = ImportUtils.countFiles(files,LEAGUE_EXTENSION);

        String[] leagueNames = new String[leagueCount];
        int index = 0;

        for (String file : files) {
            if (file.endsWith(LEAGUE_EXTENSION)) {
                String leagueName = file.substring(0, file.length() - LEAGUE_EXTENSION.length());
                leagueNames[index++] = leagueName;
            }
        }
        return leagueNames;
    }





    /**
     * Cria uma liga a partir de um objeto JSON.
     *
     * Este método constrói uma instância de League utilizando os dados
     * fornecidos no objeto JSON, incluindo as temporadas. As temporadas associadas
     * são criadas a partir do conteúdo JSON, e os clubes correspondentes são atribuídos
     * conforme fornecido no array allClubs.
     *
     * @param leagueJson Objeto JSON contendo os dados da liga a ser criada.
     * @param allClubs Array de clubes disponíveis para associar à liga.
     * @return Uma instância de ILeague criada a partir do JSON fornecido.
     */
    private ILeague createLeagueFromJson(JSONObject leagueJson, IClub[] allClubs) {
        String name = (String) leagueJson.get("name");
        League league = new League(name);

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
     * Cria uma instância da classe Season a partir de um objeto JSON fornecido.
     * 
     * @param seasonJson Objeto JSON contendo os dados da temporada.
     * @param allClubs Array de clubes disponíveis para associar à temporada.
     * @return Uma instância da classe Season criada a partir dos dados fornecidos,
     *         ou null se ocorrer um erro durante o processo de criação.
     */
    private Season createSeasonFromJson(JSONObject seasonJson, IClub[] allClubs) {
        try {
            String name = (String) seasonJson.get("name");
            int year = ((Long) seasonJson.get("year")).intValue();
            
            int coachingClubIndex = ImportUtils.getIntValue(seasonJson, "coachingClubIndex", -1);
            int currentRound = ImportUtils.getIntValue(seasonJson, "currentRound", 1);
            int pointsPerWin = ImportUtils.getIntValue(seasonJson, "pointsPerWin", 3);
            int pointsPerDraw = ImportUtils.getIntValue(seasonJson, "pointsPerDraw", 1);
            int pointsPerLoss = ImportUtils.getIntValue(seasonJson, "pointsPerLoss", 0);
            
            Season season = new Season(name, year, coachingClubIndex, currentRound,
                    pointsPerWin, pointsPerDraw, pointsPerLoss);
            
            addClubsToSeason(season, seasonJson, allClubs);

            loadSchedule(season, seasonJson);
            loadStandings(season, seasonJson);

            return season;

        } catch (Exception e) {
            logger.writeLog("Erro ao criar temporada: " + e.getMessage());
            return null;
        }
    }

    /**
     * Carrega o calendário (schedule) de uma temporada a partir de um objeto JSON. 
     * Caso o calendário não esteja presente ou ocorra algum erro durante o processo de carregamento,
     * será gerado um calendário automático.
     *
     * @param season Objeto da temporada que receberá o calendário.
     * @param seasonJson Objeto JSON contendo os dados da temporada, incluindo o calendário, se disponível.
     */
    private void loadSchedule(Season season, JSONObject seasonJson) {
        if (!seasonJson.containsKey("schedule")) {
            logger.writeLog("Nenhum schedule encontrado no save, a gerar calendário automático...");
            season.generateSchedule();
            return;
        }

        try {
            JSONObject scheduleJson = (JSONObject) seasonJson.get("schedule");
            System.out.println("A carregar schedule do save...");
            
            IMatch[] matches = loadMatches(season, scheduleJson);

            if (matches != null && matches.length > 0) {
                System.out.println("Schedule carregado com " + matches.length + " matches!");

                IClub[] clubs = season.getCurrentClubs();
                int numberOfClubs = season.getNumberOfCurrentTeams();
                int numberOfRounds =ImportUtils.getIntValue(scheduleJson, "numberOfRounds", (numberOfClubs - 1) * 2);

                Schedule schedule = new Schedule(matches, clubs, numberOfClubs, numberOfRounds);
                season.setSchedule(schedule);

            } else {
                logger.writeLog("Erro ao carregar matches, a gerar calendário automático...");
                season.generateSchedule();
            }

        } catch (Exception e) {
            logger.writeLog("Erro ao carregar schedule: " + e.getMessage());
            season.generateSchedule();
        }
    }

    /**
     * Carrega uma lista de partidas (matches) a partir de um objeto JSON fornecido..
     *
     * @param season Objeto da temporada associada às partidas.
     * @param scheduleJson Objeto JSON que contém as informações para carregar as partidas.
     * @return Um array de objetos IMatch contendo as partidas carregadas. Retorna null caso o JSON 
     *         não contenha a chave "matches", o array esteja vazio, ou ocorra algum erro durante o processo.
     */
    private IMatch[] loadMatches(Season season, JSONObject scheduleJson) {
        if (!scheduleJson.containsKey("matches")) {
            logger.writeLog("Nenhuma partida encontrada no save");
            return null;
        }

        try {
            JSONArray matchesArray = (JSONArray) scheduleJson.get("matches");

            if (matchesArray == null || matchesArray.isEmpty()) {
                logger.writeLog("Array de matches vazio");
                return null;
            }

            System.out.println("A carregar " + matchesArray.size() + " matches...");

            IMatch[] matches = new Match[matchesArray.size()];
            IClub[] clubs = season.getCurrentClubs(); // CORRIGIR: usar clubs da season

            for (int i = 0; i < matchesArray.size(); i++) {
                JSONObject matchJson = (JSONObject) matchesArray.get(i);
                matches[i] = createMatch(matchJson, clubs, i + 1); // CORRIGIR: nome do método
            }

            return matches;

        } catch (Exception e) {
            logger.writeLog("Erro ao carregar matches: " + e.getMessage());
            return null;
        }
    }

    /**
     * Cria uma partida (match) com base nos dados fornecidos no objeto JSON, associando os clubes correspondentes
     * e configurando as informações da partida, como equipas, eventos e resultados, caso disponível.
     *
     * @param matchJson Objeto JSON contendo os dados da partida, incluindo clubes, eventos e resultados, se aplicável.
     * @param clubs Um array de clubes disponíveis, usado para associar os clubes da partida com base nos seus códigos.
     * @param round O número da round a que a partida pertence.
     * @return Uma instância de IMatch correspondente à partida criada. Retorna null caso ocorra um erro ou se os clubes
     *         especificados não forem encontrados.
     */
    private IMatch createMatch(JSONObject matchJson, IClub[] clubs, int round) {
        try {
            String homeClubCode = (String) matchJson.get("homeClub");
            String awayClubCode = (String) matchJson.get("awayClub");

            IClub homeClub = ImportUtils.findClubByCode(clubs, homeClubCode);
            IClub awayClub = ImportUtils.findClubByCode(clubs, awayClubCode);

            if (homeClub == null || awayClub == null) {
                logger.writeLog("Clube não encontrado: " + homeClubCode + " vs " + awayClubCode);
                return null;
            }
            
            Match match = new Match(homeClub, awayClub, round);

            loadTeams(match,matchJson);
            loadEventsForMatch(match, matchJson);

            boolean played = (boolean) matchJson.get("played");

            if (played) {
                match.setPlayed();

                if (matchJson.containsKey("homeGoals") && matchJson.containsKey("awayGoals")) {
                    int homeGoals = ImportUtils.getIntValue(matchJson, "homeGoals", 0);
                    int awayGoals = ImportUtils.getIntValue(matchJson, "awayGoals", 0);

                    match.setHomeGoals(homeGoals);
                    match.setAwayGoals(awayGoals);

                    logger.writeLog("Match criada: " + homeClubCode + " " + homeGoals + "-" + awayGoals + " " + awayClubCode);
                }

            } else {
                logger.writeLog("Match criada: " + homeClubCode + " vs " + awayClubCode + " (não jogada)");
            }
            return match;
            
        } catch (Exception e) {
            logger.writeLog("Erro ao criar match: " + e.getMessage());
            return null;
        }
    }

    /**
     * Carrega os eventos associados a uma partida a partir de um objeto JSON fornecido.
     *
     * O método verifica se a chave "events" está presente no JSON da partida e, se presente, 
     * percorre a lista de eventos para criar e associar instâncias de eventos à partida. 
     * Caso a chave "events" não esteja presente, o método termina sem realizar alterações.
     *
     * @param match Objeto da partida à qual os eventos serão associados.
     * @param matchJson Objeto JSON que contém os dados da partida, incluindo eventos.
     */
    private void loadEventsForMatch(Match match, JSONObject matchJson) {
        if (!matchJson.containsKey("events")) {
            return;
        }

        JSONObject eventsJson = (JSONObject) matchJson.get("events");
        JSONArray eventsArray = (JSONArray) eventsJson.get("events");

        for (Object eventObj : eventsArray) {
            JSONObject eventJson = (JSONObject) eventObj;

            IEvent event = createEventFromJson(eventJson);
            if (event != null) {
                match.addEvent(event);
            }
        }
    }

    /**
     * Cria um evento a partir de um objeto JSON.
     * O evento é interpretado com base no tipo fornecido no JSON ("Goal", "Foul", "PassEvent", 
     * "GoalKick" ou "HalftimeEvent"). Caso o tipo seja válido e todas as informações necessárias 
     * estejam presentes e corretas, o correspondente IEvent é criado. 
     * Caso contrário, retorna null.
     *
     * @param eventJson Objeto JSON contendo os dados do evento, incluindo tipo, minuto, 
     *                  descrição e outras informações necessárias dependendo do tipo 
     *                  do evento.
     * @return Instância de IEvent correspondente ao tipo de evento e dados fornecidos, 
     *         ou null se o tipo for desconhecido ou houver erro na criação.
     */
    private IEvent createEventFromJson(JSONObject eventJson) {
        String type = (String) eventJson.get("type");
        int minute = ((Long) eventJson.get("minute")).intValue();
        String description = (String) eventJson.get("description");

        switch (type) {
            case "Goal":
                String autorName = (String) eventJson.get("autor");
                IPlayer player = ImportUtils.findPlayerByName(allClubs, autorName);
                if (player != null) {
                    return new GoalEvent(player, minute, description);
                }
                break;

            case "Foul":
                String autorFoul = (String) eventJson.get("autor");
                String victimName = (String) eventJson.get("victim");
                IPlayer autor = ImportUtils.findPlayerByName(allClubs, autorFoul);
                IPlayer victim =ImportUtils.findPlayerByName(allClubs, victimName);
                if (autor != null && victim != null) {
                    return new FoulEvent(description, minute, autor, victim);
                }
                break;

            case "PassEvent":
                String autorPass = (String) eventJson.get("autor");
                IPlayer autorPlayer = ImportUtils.findPlayerByName(allClubs, autorPass);
                if (autorPlayer != null) {
                    return new PassEvent(description, minute, autorPlayer);
                }
                break;

            case "GoalKick":
                String autorKick = (String) eventJson.get("autor");
                IPlayer kickPlayer = ImportUtils.findPlayerByName(allClubs, autorKick);
                if (kickPlayer != null) {
                    return new GoalKickEvent(minute, description, kickPlayer);
                }
                break;

            case "HalftimeEvent":
                return new HalftimeEvent(minute);

            default:
                logger.writeLog("Tipo de evento desconhecido: " + type);
                break;
        }
        return null;
    }


    /**
     * Carrega as equipas (homeTeam e awayTeam) para uma partida (match) a partir de um objeto JSON.
     *
     * Este método verifica se as chaves "homeTeam" e "awayTeam" estão presentes no objeto JSON fornecido.
     * Se disponíveis, cria as equipas correspondentes utilizando o JSON e associa-as à partida.
     * Caso contrário, exibe uma mensagem de aviso indicando que as equipas não estão definidas.
     * Em caso de erro durante o processo, exibe uma mensagem de erro.
     *
     * @param match Objeto da partida à qual as equipas serão associadas.
     * @param matchJson Objeto JSON contendo os dados das equipas (homeTeam e awayTeam) a serem carregadas.
     */
    private void loadTeams(IMatch match, JSONObject matchJson) {
        try {
  
            if (matchJson.containsKey("homeTeam") && matchJson.containsKey("awayTeam")) {

                JSONObject homeTeamJson = (JSONObject) matchJson.get("homeTeam");
                JSONObject awayTeamJson = (JSONObject) matchJson.get("awayTeam");
                
                ITeam homeTeam = createTeam(homeTeamJson, match.getHomeClub());
                ITeam awayTeam = createTeam(awayTeamJson, match.getAwayClub());
                
                match.setTeam(homeTeam);
                match.setTeam(awayTeam);

                logger.writeLog("Equipas carregadas para a match");

            } else {
                logger.writeLog("AVISO: Match sem equipas definidas no JSON");
            }
        } catch (Exception e) {
            logger.writeLog("Erro ao carregar equipas: " + e.getMessage());
        }
    }



    private ITeam createTeam(JSONObject teamJson, IClub club) {
        Formation formation = createFormation(teamJson);
        Team team = new Team(club, formation);

        if (teamJson.containsKey(SQUAD_KEY)) {
            addSquadPlayers(team, (JSONArray) teamJson.get(SQUAD_KEY), club);
        }

        return team;
    }

    private Formation createFormation(JSONObject teamJson) {
        if (!teamJson.containsKey(FORMATION_KEY)) {
            return null;
        }
        return new Formation((String) teamJson.get(FORMATION_KEY));
    }

    private void addSquadPlayers(Team team, JSONArray squadArray, IClub club) {
        for (Object playerNameObj : squadArray) {
            String playerName = (String) playerNameObj;
            addPlayerToTeam(team, playerName, club);
        }
    }

    private void addPlayerToTeam(Team team, String playerName, IClub club) {
        IPlayer player = ImportUtils.findPlayerInClub(club, playerName);

        if (player == null) {
            logger.writeLog("Jogador não encontrado no clube: " + playerName);
            return;
        }

        try {
            team.addPlayer(player);
        } catch (Exception e) {
            logger.writeLog("ERRO ao adicionar " + playerName + ": " + e.getMessage());
        }
    }

    /**
     * Carrega os standings (classificação) da temporada
     */
    private void loadStandings(Season season, JSONObject seasonJson) {
        if (!seasonJson.containsKey("standings")) {
            logger.writeLog("Nenhum standing encontrado no save");
            return;
        }

        try {
            JSONArray standingsArray = (JSONArray) seasonJson.get("standings");
            IClub[] clubs = season.getCurrentClubs();

            System.out.println("A carregar " + standingsArray.size() + " standings...");

            // Criar array de standings
            Standing[] standings = new Standing[standingsArray.size()];

            for (int i = 0; i < standingsArray.size(); i++) {
                JSONObject standingJson = (JSONObject) standingsArray.get(i);

                String clubCode = (String) standingJson.get("Club");
                IClub club = ImportUtils.findClubByCode(clubs, clubCode);

                if (club != null) {
                    // Extrair dados do standing do JSON
                    int points = ImportUtils.getIntValue(standingJson, "Points", 0);
                    int wins = ImportUtils.getIntValue(standingJson, "Wins", 0);
                    int losses = ImportUtils.getIntValue(standingJson, "Losses", 0);
                    int draws = ImportUtils.getIntValue(standingJson, "Draws", 0);
                    int goalsScored = ImportUtils.getIntValue(standingJson, "GoalsScored", 0);
                    int goalsConceded = ImportUtils.getIntValue(standingJson, "GoalsConceded", 0);

                    // Criar standing com o novo construtor
                    standings[i] = new Standing(club, points, wins, losses, draws, goalsScored, goalsConceded);

                    System.out.println("Standing carregado para " + clubCode + ": " + points + " pts");

                } else {
                    System.out.println("Clube não encontrado para standing: " + clubCode);
                    standings[i] = null; // ou criar um standing vazio
                }
            }

            // Definir todos os standings na temporada de uma vez
            season.setStandings(standings);
            System.out.println("Todos os standings definidos na temporada!");

        } catch (Exception e) {
            System.out.println("Erro ao carregar standings: " + e.getMessage());
            e.printStackTrace();
        }
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

            IClub club = ImportUtils.findClubByCode(allClubs, clubCode);
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
        int maxPlayers =ImportUtils.getIntValue(clubJson, "maxPlayers", 40);

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
        int number = ImportUtils.getIntValue(playerJson, "number", 0);

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

        int shooting = ImportUtils.getIntValue(attributesJson, "shooting", 50);
        int passing = ImportUtils.getIntValue(attributesJson, "passing", 50);
        int stamina = ImportUtils.getIntValue(attributesJson, "stamina", 50);
        int speed = ImportUtils.getIntValue(attributesJson, "speed", 50);
        int defence = ImportUtils.getIntValue(attributesJson, "defence", 50);

        float height = ImportUtils.getFloatValue(attributesJson, "height", 1.80f);
        float weight =ImportUtils.getFloatValue(attributesJson, "weight", 75.0f);

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



}