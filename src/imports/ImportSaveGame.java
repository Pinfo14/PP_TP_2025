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

import static imports.ClubImport.importAllClubs;
import static imports.ImportUtils.*;
import static imports.MatchesImport.loadMatches;

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


    private static final Logs logger = new Logs();

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
}