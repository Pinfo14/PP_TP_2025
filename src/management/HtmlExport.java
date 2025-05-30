package management;

import com.ppstudios.footballmanager.api.contracts.data.htmlgenerators.ClubHtmlGenerator;
import com.ppstudios.footballmanager.api.contracts.data.htmlgenerators.LeagueHtmlGenerator;
import com.ppstudios.footballmanager.api.contracts.data.htmlgenerators.MatchHtmlGenerator;
import com.ppstudios.footballmanager.api.contracts.data.htmlgenerators.SeasonHtmlGenerator;
import com.ppstudios.footballmanager.api.contracts.league.ISeason;
import com.ppstudios.footballmanager.api.contracts.match.IMatch;
import com.ppstudios.footballmanager.api.contracts.team.IClub;
import com.ppstudios.footballmanager.api.contracts.team.ITeam;

import java.io.IOException;

public class HtmlExport {

    private static final String SAVE_DIRECTORY = "src/Files/HTMLFiles/";
    private static final String LEAGUES_FILE_PATH = "src/Files/SaveGames/";

    public static void exportClubHtml(IClub club) {
        try {
            ClubHtmlGenerator.generate(club,SAVE_DIRECTORY+club.getName()+".html");
        } catch (IOException e) {
          System.out.println(e.getMessage());
        }
    }


    public static void exportMatchHtml(IMatch match) {
        try {
            MatchHtmlGenerator.generate(match,SAVE_DIRECTORY+match.getHomeClub()+"vs"+match.getAwayClub()+".html");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
