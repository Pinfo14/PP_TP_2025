package demos;

import management.LeagueManagement;

public class ImportSavegameDemo {
    public static void main(String[] args) {
        LeagueManagement leagueManagement = new LeagueManagement();

        leagueManagement.loadGame();
    }
}
