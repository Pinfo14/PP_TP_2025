package management;

import com.ppstudios.footballmanager.api.contracts.match.IMatch;
import com.ppstudios.footballmanager.api.contracts.team.IClub;
import league.Season;
import match.Match;
import menus.RoundMenu;
import menus.SeasonMenu;
import reader.Reader;
import team.Formation;

public class RoundManagement {

    public void run(Season season) {

        FormationManagement formationManagement = new FormationManagement();
        Reader reader = new Reader();

        SeasonMenu.mainSeasonMenu(season.getYear(), season.getNameCoachingClub(), season.getName(), season.getCurrentRound());
        RoundMenu.topMenu(season.getCurrentRound(),getOpponentName(season), getGamesString(season));
        formationManagement.listFormations();
        reader.readInt(1, formationManagement.getNumFormations(), "Seleicone a tatica que pretende: ");
        //descobrir a posicçao do jogo



    }

    private IClub findCoachingClub(Season season) {

        IClub[] clubs = season.getCurrentClubs();
        int coachingClubIndex = season.getCoachingClubIndex();

        if (clubs == null || coachingClubIndex < 0 || coachingClubIndex >= clubs.length) {
            return null;
        }
        return clubs[coachingClubIndex];
    }

    private IMatch setCoachFormation(Season season, Formation formation) {

        IMatch[] matches = season.getMatches(season.getCurrentRound());
        IClub coachingClub = findCoachingClub(season);

        for(IMatch match : matches) {
            if(match.getAwayClub().equals(coachingClub)) {
                match.setTeam();
            }
        }



    }


    private String getGamesString(Season season) {
        IMatch[] matches = season.getMatches(season.getCurrentRound());

        if ((matches == null) && (matches.length == 0)) {
            return "Erro. Nao existem jogos.";
        }

        StringBuilder sb = new StringBuilder();
        for(IMatch match : matches) {
            sb.append("- ").append(match.toString()).append("\n");
        }

        return sb.toString();
    }

    private String getOpponentName(Season season) {

        IMatch[] matches = season.getMatches(season.getCurrentRound());
        if (matches == null || matches.length == 0) {
            return null;
        }

        IClub club = findCoachingClub(season);

        for (IMatch match : matches) {
            if (match.getHomeClub().equals(club)) {
                return match.getAwayClub().getName();
            }
            if (match.getAwayClub().equals(club)) {
                return match.getHomeClub().getName();
            }
        }

        return "desconhecido";
    }


}
