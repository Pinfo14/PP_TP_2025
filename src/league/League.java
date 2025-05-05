package league;

import com.ppstudios.footballmanager.api.contracts.league.ILeague;
import com.ppstudios.footballmanager.api.contracts.league.ISeason;

import java.io.IOException;

public class League implements ILeague {

    private static final int INITIAL_SEASONS = 2;
    private static final int INCREMENT_FACTOR = 2;
    private String leagueName;
    private Season[] seasons;
    private int numberOfSeasons;


    League(String leagueName){
        this.leagueName = leagueName;
        this.numberOfSeasons = 0;
        this.seasons = new Season[INITIAL_SEASONS];

    }

    @Override
    public String getName() {
        return leagueName;
    }

    @Override
    public ISeason[] getSeasons() {
        return new ISeason[0];
    }

    private void incrementSeasons () {
        Season[] seasonsTemp = new Season[seasons.length * INCREMENT_FACTOR];

        System.arraycopy(seasons, 0, seasonsTemp, 0, seasons.length);

        seasons = seasonsTemp;
    }

    @Override
    public boolean createSeason(ISeason iSeason) {
        if(iSeason == null) {
            throw new IllegalArgumentException("Season cannot be null");
        }

        //verifica se existe alguma season igual - definir o equals para season

        if (numberOfSeasons == seasons.length) {
            incrementSeasons();
        }

        seasons[numberOfSeasons++] = (Season) iSeason;

        return true;
    }

    @Override
    public ISeason removeSeason(int i) {
        return null;
    }

    @Override
    public ISeason getSeason(int i) {
        return null;
    }

    @Override
    public void exportToJson() throws IOException {

    }
}
