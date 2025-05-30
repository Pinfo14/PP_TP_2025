package imports;

import com.ppstudios.footballmanager.api.contracts.match.IMatch;
import com.ppstudios.footballmanager.api.contracts.player.IPlayer;
import com.ppstudios.footballmanager.api.contracts.team.IClub;
import com.ppstudios.footballmanager.api.contracts.team.ITeam;
import league.Season;
import match.Match;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import team.Club;
import team.Formation;
import team.Team;


import static imports.EventImporter.*;
import static imports.ImportUtils.*;

public class MatchesImport {

    private static final Club folga = new Club("FOLGA");

    private static int calculateRoundFromIndex(int matchIndex, int numberOfClubs) {

        int effectiveClubs = numberOfClubs;
        if (numberOfClubs % 2 != 0) {
            effectiveClubs = numberOfClubs + 1; // Adicionar FOLGA para número ímpar
        }

        int matchesPerRound = effectiveClubs / 2;
        return (matchIndex / matchesPerRound) + 1;
    }
    /**
     * Carrega uma lista de partidas (matches) a partir de um objeto JSON fornecido..
     *
     * @param season Objeto da temporada associada às partidas.
     * @param scheduleJson Objeto JSON que contém as informações para carregar as partidas.
     * @return Um array de objetos IMatch contendo as partidas carregadas. Retorna null caso o JSON
     *         não contenha a chave "matches", o array esteja vazio, ou ocorra algum erro durante o processo.
     */
    public static IMatch[] loadMatches(Season season, JSONObject scheduleJson) {
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
            IClub[] clubs = season.getCurrentClubs();
            int numberOfClubs = season.getNumberOfCurrentTeams();

            for (int i = 0; i < matchesArray.size(); i++) {
                JSONObject matchJson = (JSONObject) matchesArray.get(i);

                // Calcular o round baseado no índice
                int round = calculateRoundFromIndex(i, numberOfClubs);

                IMatch match = createMatch(matchJson, clubs, round);

                if (match != null) {
                    matches[i] = match;

                } else {
                    logger.writeLog("ERRO: createMatch retornou null para posição " + i);
                    matches[i] = new Match(folga, folga, round);
                }
            }

            return matches;

        } catch (Exception e) {
            logger.writeLog("Erro ao carregar matches: " + e.getMessage());
            e.printStackTrace();
            return new Match[0];
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
    private static IMatch createMatch(JSONObject matchJson, IClub[] clubs, int round) {
        try {
            String homeClubCode = (String) matchJson.get("homeClub");
            String awayClubCode = (String) matchJson.get("awayClub");
            boolean played = (boolean) matchJson.get("played");

            IClub homeClub;
            IClub awayClub;
            Match match;

            // Caso 1: homeClub é null (FOLGA)
            if (homeClubCode == null) {
                awayClub = ImportUtils.findClubByCode(clubs, awayClubCode);
                if (awayClub == null) {
                    logger.writeLog("Clube away não encontrado: " + awayClubCode);
                    match = new Match(folga, folga, round);
                } else {
                    match = new Match(folga, awayClub, round);
                }

                if (played) {
                    match.setPlayed();
                    setMatchGoals(match, matchJson);
                }

                logger.writeLog("Match criada: FOLGA vs " + (awayClub != null ? awayClub.getCode() : "FOLGA") +
                        (played ? " (jogada)" : " (não jogada)"));
                return match;
            }

            // Caso 2: awayClub é null (FOLGA)
            if (awayClubCode == null) {
                homeClub = ImportUtils.findClubByCode(clubs, homeClubCode);
                if (homeClub == null) {
                    logger.writeLog("Clube home não encontrado: " + homeClubCode);
                    match = new Match(folga, folga, round);
                } else {
                    match = new Match(homeClub, folga, round);
                }

                if (played) {
                    match.setPlayed();
                    setMatchGoals(match, matchJson);
                }

                logger.writeLog("Match criada: " + (homeClub != null ? homeClub.getCode() : "FOLGA") + " vs FOLGA" +
                        (played ? " (jogada)" : " (não jogada)"));
                return match;
            }

            // Caso 3: Ambos os clubes existem
            homeClub = ImportUtils.findClubByCode(clubs, homeClubCode);
            awayClub = ImportUtils.findClubByCode(clubs, awayClubCode);

            if (homeClub == null || awayClub == null) {
                logger.writeLog("Clube(s) não encontrado(s): " + homeClubCode + " vs " + awayClubCode);
                return new Match(folga, folga, round);
            }

            match = new Match(homeClub, awayClub, round);

            // Carregar equipas e eventos apenas se a match foi jogada
            if (played) {
                loadTeams(match, matchJson);
                loadEventsForMatch(match, matchJson, clubs);
                match.setPlayed();
                setMatchGoals(match, matchJson);

                logger.writeLog("Match criada: " + homeClubCode + " vs " + awayClubCode + " (jogada)");
            } else {
                logger.writeLog("Match criada: " + homeClubCode + " vs " + awayClubCode + " (não jogada)");
            }

            return match;

        } catch (Exception e) {
            logger.writeLog("Erro ao criar match: " + e.getMessage());
            e.printStackTrace();
            return new Match(folga, folga, round);
        }
    }

    /**
     * Método auxiliar para definir os golos da match
     */
    private static void setMatchGoals(Match match, JSONObject matchJson) {
        if (matchJson.containsKey("homeGoals") && matchJson.containsKey("awayGoals")) {
            int homeGoals = ImportUtils.getIntValue(matchJson, "homeGoals", 0);
            int awayGoals = ImportUtils.getIntValue(matchJson, "awayGoals", 0);

            match.setHomeGoals(homeGoals);
            match.setAwayGoals(awayGoals);
        }
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
    private static void loadTeams(IMatch match, JSONObject matchJson) {
        try {

            if (matchJson.containsKey("homeTeam") && matchJson.containsKey("awayTeam")) {

                JSONObject homeTeamJson = (JSONObject) matchJson.get("homeTeam");
                JSONObject awayTeamJson = (JSONObject) matchJson.get("awayTeam");

                ITeam homeTeam = createTeam(homeTeamJson, match.getHomeClub());
                ITeam awayTeam = createTeam(awayTeamJson, match.getAwayClub());

                match.setTeam(homeTeam);
                match.setTeam(awayTeam);

                logger.writeLog("Equipas carregadas para a match");

            }
        } catch (Exception e) {
            logger.writeLog("Erro ao carregar equipas: " + e.getMessage());
        }
    }



    private  static ITeam createTeam(JSONObject teamJson, IClub club) {
        Formation formation = createFormation(teamJson);
        Team team = new Team(club, formation);

        if (teamJson.containsKey(SQUAD_KEY)) {
            addSquadPlayers(team, (JSONArray) teamJson.get(SQUAD_KEY), club);
        }

        return team;
    }

    private static Formation createFormation(JSONObject teamJson) {
        if (!teamJson.containsKey(FORMATION_KEY)) {
            return null;
        }
        return new Formation((String) teamJson.get(FORMATION_KEY));
    }

    private static void addSquadPlayers(Team team, JSONArray squadArray, IClub club) {
        for (Object playerNameObj : squadArray) {
            String playerName = (String) playerNameObj;
            addPlayerToTeam(team, playerName, club);
        }
    }

    private static void addPlayerToTeam(Team team, String playerName, IClub club) {
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

}
