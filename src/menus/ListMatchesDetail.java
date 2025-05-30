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
        int countID = 1;

        IMatch[] matches = null;
        try {
            matches = season.getMatches();
        }catch (Exception e) {
            System.out.println("Erro ao obter os partidas");
            Utils.waitEnter();
            return;
        }

        System.out.println("----+---------------------------------------------------------------------");
        System.out.printf("%-3s | %s\n", "ID", "JOGOS");
        System.out.println("----+---------------------------------------------------------------------");

        for (IMatch match : matches) {
            if (match != null && match.isPlayed()) {
                System.out.printf("%-3d | %s", countID++, formatMatc(match));
            }
        }


        if(countID == 1) {
            System.out.printf("Não existem jogos.");
        } else {
            int option = reader.readInt(0, countID - 1, "\nSelecione um jogo ou 0 para voltar ao menu anterior: ");
            int index = option - 1;

            if (option > 0 && index < matches.length && matches[index] != null && matches[index].isPlayed()) {
                showMatchDetail(matches[index]);
            }
        }



        Utils.waitEnter();
    }

    private static String formatMatc(IMatch match) {
        if (!(match instanceof Match m)) {
            return "(partida inválida)\n";
        }

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
        System.out.println("\n" + formatMatc(match));
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
