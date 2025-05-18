package menus;

public class HeaderMenu {

    private static final int W_SEASOM = 11;
    private static final int W_LEAGUE_NAME = 25;
    private static final int W_ROUND = 9;
    private static final int W_CLUB = 20;

    public static void print(int year, int round, String club, String leagueName) {
        String season_year = year + "-" + (year + 1);

        String header = "%-" + W_SEASOM + "s | %-" + W_LEAGUE_NAME + "s | %-" + W_ROUND + "s | %-" + W_CLUB + "s\n";
        String data = "%-" + W_SEASOM + "s | %-" + W_LEAGUE_NAME + "s | %-" + W_ROUND + "d | %-" + W_CLUB + "s\n";

        System.out.println("\n--------------------------------------------------------------------------");
        System.out.printf(header, "ÉPOCA", "COMPETIÇÃO", "JORNADA", "CLUBE A TREINAR");
        System.out.println("--------------------------------------------------------------------------");
        System.out.printf(data, season_year, leagueName, round, club);
        System.out.println("--------------------------------------------------------------------------");
    }
}
