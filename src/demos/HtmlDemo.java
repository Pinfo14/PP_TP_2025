package demos;

import com.ppstudios.footballmanager.api.contracts.team.IClub;
import imports.ImportSaveGame;
import imports.Imports;
import management.HtmlExport;

public class HtmlDemo {
    public static void main(String[] args) {
        Imports imports = new Imports();
        IClub[] clubs = imports.importPlayersAndClub();

        HtmlExport.exportClubHtml(clubs[10]);

    }
}
