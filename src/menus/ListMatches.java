package menus;

import com.ppstudios.footballmanager.api.contracts.match.IMatch;


public class ListMatches {

    public static void listMatches(IMatch[] matches) {
        if (matches == null) {
            throw new NullPointerException("\nLista de jogos é nula!");
        }
        if (matches.length == 0) {
            System.out.println("\nNenhum jogo foi carregado.");
            return;
        }



        System.out.println("\n--------+--------------------------------+--------------------------------");
        System.out.println(String.format("%-7s | %-30s | %-30s","JORNADA", "VISITADO", "VISITANTE"));
        System.out.println("--------+--------------------------------+--------------------------------");

        int lastRound = -1;
        for (IMatch mt : matches) {

            int round = mt.getRound();

            String home = mt.getHomeClub().getName();
            String away = mt.getAwayClub().getName();

            if (mt.getHomeClub().getName().equals("FOLGA")) {
                home = "-";
            }
            if (mt.getAwayClub().getName().equals("FOLGA")) {
                away = "-";
            }

            if (round != lastRound && lastRound != -1) {
                System.out.println("--------+--------------------------------+--------------------------------");
            }

            lastRound = round;

            System.out.println(String.format("%-7s | %-30s | %-30s", mt.getRound(), home, away));

        }
    }
}
