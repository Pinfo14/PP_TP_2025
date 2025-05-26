package management;

import com.ppstudios.footballmanager.api.contracts.league.ISeason;
import league.League;
import league.Season;
import reader.Reader;

import java.io.IOException;

public class LeagueManagement {

    private static final int INITIAL_LEAGUE = 1;
    private static final int INCREMENT_FACTOR = 2;
    private League[] league = new League[INITIAL_LEAGUE];  // Inicializado
    private int countLeague;
    private SaveGame saveGame ;


    public LeagueManagement() {
        this.saveGame = new SaveGame();
    }

    public void startNewGame( boolean useDefaultData) {

        Reader reader = new Reader();
        int year;
        String leagueName;
        countLeague = 0;
        ISeason season;

        initialMessage();

        leagueName = reader.readString("Insira o nome da liga: ");
        year = reader.readInt(2020, 2040, "Insira o ano que pretende iniciar (entre 2020 e 2040): ");

        // Verifica se há espaço no array
        if (countLeague >= league.length) {
            increaseLeagueArray();
        }

        league[countLeague] = new League(leagueName);
        season = new Season(leagueName, year);
        league[countLeague].createSeason(season);

        SeasonManagement seasonManagement = new SeasonManagement(useDefaultData);
        seasonManagement.run(league[countLeague].getSeason(year));

        countLeague++;



    }

    private void initialMessage() {
        StringBuilder sb = new StringBuilder();

        sb.append("\n");
        sb.append("Bem-vindo ao PP Soccer Manager. Para iniciar um novo jogo tem de indicar o ano em que\n");
        sb.append("pretende começar. Também tem de colocar o nome da sua Liga.\n");
        sb.append("Após indicar o nome e o ano, será redirecionado para a página de gestão da sua liga.\n");

        System.out.println(sb);
    }

    private void increaseLeagueArray() {
        League[] newArray = new League[league.length * INCREMENT_FACTOR];

        System.arraycopy(league, 0, newArray, 0, league.length);
        league = newArray;
    }

    public void saveGame() {
        try {
            this.saveGame.saveGame(league[countLeague]);
            System.out.println("Jogo guardado com sucesso.");
        } catch (IOException e) {
           System.out.println(e.getMessage());
        }

    }
}