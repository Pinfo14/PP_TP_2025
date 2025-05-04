package league;

import com.ppstudios.footballmanager.api.contracts.league.ILeague;
import com.ppstudios.footballmanager.api.contracts.league.ISeason;

import java.io.IOException;

public class League implements ILeague {

    private String leagueName;
    private String leagueDescription;


    @Override
    public String getName() {
        return "";
    }

    @Override
    public ISeason[] getSeasons() {
        return new ISeason[0];
    }

    @Override
    public boolean createSeason(ISeason iSeason) {
        return false;
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
