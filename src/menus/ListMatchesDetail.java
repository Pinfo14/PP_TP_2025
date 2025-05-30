package menus;

import com.ppstudios.footballmanager.api.contracts.event.IEvent;
import com.ppstudios.footballmanager.api.contracts.match.IMatch;
import league.Season;
import match.Match;
import reader.Reader;
import team.Team;
import util.Utils;

public class ListMatchesDetail {

    public static void list(Season season) {
        Reader reader = new Reader();
        IMatch[] matches;
        int[] realIndexes = new int[100]; // tamanho máximo assumido, ou matches.length

        try {
            matches = season.getMatches();
        } catch (Exception e) {
            System.out.println("Erro ao obter os partidas");
            Utils.waitEnter();
            return;
        }

        System.out.println("----+---------------------------------------------------------------------");
        System.out.printf("%-3s | %s\n", "ID", "JOGOS");
        System.out.println("----+---------------------------------------------------------------------");

        int countID = 1;
        for (int i = 0; i < matches.length; i++) {
            IMatch match = matches[i];

            if (match != null && match.isPlayed()) {
                System.out.printf("%-3d | %s", countID, formatMatch(match));
                realIndexes[countID - 1] = i;
                countID++;
            }
        }

        if (countID == 1) {
            System.out.println("Não existem jogos.");
        } else {
            int option = reader.readInt(0, countID - 1, "\nSelecione um jogo ou 0 para voltar ao menu anterior: ");

            if (option > 0) {
                int realIndex = realIndexes[option - 1];
                showMatchDetail(matches[realIndex]);
            }
        }

        Utils.waitEnter();
    }

    private static String formatMatch(IMatch match) {
        if (!(match instanceof Match)) {
            return "(partida inválida)\n";
        }

        Match m = (Match) match;

        StringBuilder sb = new StringBuilder();
        sb.append("Jornada ").append(m.getRound()).append(" - ");
        sb.append(m.getHomeClub().getName()).append(" (").append(m.getHomeGoals()).append(") vs ");
        sb.append("(").append(m.getAwayGoals()).append(") ").append(m.getAwayClub().getName()).append("\n");

        return sb.toString();
    }


    private static void showMatchDetail(IMatch match) {
        System.out.println("\nResumo do jogo:");
        ListTeams.list((Team) match.getHomeTeam(), (Team) match.getAwayTeam());
        showEvents(match);
        System.out.println("\n" + formatMatch(match));
    }

    private static void showEvents(IMatch match) {
        IEvent[] eventos = match.getEvents();

        if (eventos == null || eventos.length == 0) {
            System.out.println("(sem eventos)");
        } else {
            System.out.println("\n = INÍCIO DO JOGO = ");
            for (IEvent evento : eventos) {
                System.out.println(evento);
            }
            System.out.println(" = FIM DO JOGO = ");
        }
    }
}
