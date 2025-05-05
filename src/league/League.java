package league;

import com.ppstudios.footballmanager.api.contracts.league.ILeague;
import com.ppstudios.footballmanager.api.contracts.league.ISeason;

import java.io.IOException;
/**
 * Nome: Emanuel Jose Teixeira Pinto
 * Número: 8230371
 * Turma: LEI1T1
 *
 * Nome: Roberto Cristiano Martins Faria
 * Número: 8230067
 * Turma: LEI1T2
 */

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
        ISeason[] seasonsTemp = new ISeason[numberOfSeasons];

        System.arraycopy(seasons, 0, seasonsTemp, 0, numberOfSeasons);

        return seasonsTemp;
    }

    private void incrementSizeToSeasons() {
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
            incrementSizeToSeasons();
        }

        seasons[numberOfSeasons++] = (Season) iSeason;

        return true;
    }

    /**
     * Find index
     *
     * @param year -
     * @return index correspondent in seasons array
     */
    private int findSeassonIndex(int year) {

        for (int j = 0; j < numberOfSeasons; j++) {
            if(seasons[j].getYear() == year ){
                return j;
            }
        }

        return -1;

    }

    @Override
    public ISeason removeSeason(int i) {

        int seasonIndex = findSeassonIndex(i);

        if(seasonIndex == -1){
            throw new IllegalArgumentException("Season does not found.");
        }

        ISeason season = seasons[seasonIndex];

        for (int j = seasonIndex; j < numberOfSeasons - 1; j++) {
            seasons[j] = seasons[j+1];
        }
        seasons[--numberOfSeasons] = null;

        return season;


    }

    @Override
    public ISeason getSeason(int i) {

        int seasonIndex = findSeassonIndex(i);

        if(seasonIndex == -1){
            throw new IllegalArgumentException("Season does not found.");
        }

        return seasons[seasonIndex];
    }

    @Override
    public void exportToJson() throws IOException {

    }
}
