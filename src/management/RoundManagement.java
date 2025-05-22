package management;

import com.ppstudios.footballmanager.api.contracts.match.IMatch;
import com.ppstudios.footballmanager.api.contracts.team.IClub;
import com.ppstudios.footballmanager.api.contracts.team.IFormation;
import com.ppstudios.footballmanager.api.contracts.team.ITeam;
import league.Season;
import match.Match;
import menus.RoundMenu;
import menus.SeasonMenu;
import reader.Reader;
import team.Formation;
import team.Team;

public class RoundManagement {

    public void run(Season season) {

        FormationManagement formationManagement = new FormationManagement();
        Reader reader = new Reader();

        SeasonMenu.mainSeasonMenu(season.getYear(), season.getNameCoachingClub(), season.getName(), season.getCurrentRound());
        RoundMenu.topMenu(season.getCurrentRound(), getOpponentName(season), getGamesString(season));

        //Selects the tactic formation
        int indexFormation;
        do {
            formationManagement.listFormations();
            indexFormation = reader.readInt(1, formationManagement.getNumFormations(), "Seleicone a tatica que pretende (0 - para criar nova tatica): ");
            switch (indexFormation) {
                case 0:
                    int defense = reader.readInt(1,10, "Numero de defesas: ");
                    int middle = reader.readInt(1,10, "Numero de medios: ");
                    int attackers = reader.readInt(1,10, "Numero de avançados: ");
                    formationManagement.addFormation(defense, middle, attackers);
            }
        } while (indexFormation==0);
        Formation formation = (Formation) formationManagement.getFormation(indexFormation);
        setCoachFormation(season, formation);

        //Seleciona jogadores


    }

    private IClub findCoachingClub(Season season) {

        IClub[] clubs = season.getCurrentClubs();
        int coachingClubIndex = season.getCoachingClubIndex();

        if (clubs == null || coachingClubIndex < 0 || coachingClubIndex >= clubs.length) {
            return null;
        }
        return clubs[coachingClubIndex];
    }

    private void setCoachFormation(Season season, IFormation formation) {
        IMatch[] matches = season.getMatches(season.getCurrentRound());
        IClub coachingClub = findCoachingClub(season);

        for (IMatch match : matches) {
            try {
                if (coachingClub.equals(match.getHomeClub())) {
                    match.setTeam(new Team(coachingClub, formation));
                }
                if (coachingClub.equals(match.getAwayClub())) {
                    match.setTeam(new Team(coachingClub, formation));
                }
            }catch (NullPointerException | IllegalStateException e) {
                System.out.println(e.getMessage());
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
