package league;

public class DemoLeague {

    public static void main(String[] args) {

        League league = new League("Liga Portugal");
        Season season = new Season(2022);

        try {
            league.createSeason(season);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        System.out.println(league.getName());

    }


}
