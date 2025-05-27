package management;

import com.ppstudios.footballmanager.api.contracts.event.IEvent;
import com.ppstudios.footballmanager.api.contracts.match.IMatch;
import com.ppstudios.footballmanager.api.contracts.player.IPlayer;
import com.ppstudios.footballmanager.api.contracts.team.IClub;
import league.Season;
import match.Match;
import menus.ListAllPlayers;
import menus.ListTeams;
import menus.RoundMenu;
import menus.SeasonMenu;
import reader.Reader;
import simulation.GenerateTeams;
import simulation.MatchSimulator;
import team.Formation;
import team.Team;
import util.Utils;

public class RoundManagement {

    public void run(Season season, FormationManagement formationManagement) {

        Reader reader = new Reader();

        //TODO - jogo contra FOLGA simula os outros.
        SeasonMenu.mainSeasonMenu(season.getYear(), season.getNameCoachingClub(), season.getName(), season.getCurrentRound());
        RoundMenu.topMenu(season.getCurrentRound(), getOpponentName(season), getGamesString(season));


        //Selects the tactic formation
        int indexFormation;
        do {
            formationManagement.listFormations();
            indexFormation = reader.readInt(0, formationManagement.getNumFormations(), "Seleicone a tatica que pretende (0 - para criar nova tatica): ");
            if(indexFormation == 0) {
                int defense = reader.readInt(1,10, "Numero de defesas: ");
                int middle = reader.readInt(1,10, "Numero de medios: ");
                int attackers = reader.readInt(1,10, "Numero de avançados: ");
                formationManagement.addFormation(defense, middle, attackers);
            }

        } while (indexFormation == 0);

        Formation formation = (Formation) formationManagement.getFormation(indexFormation-1);
        Team teamCoach = createTeam(season, formation);
        Team otherTeam = createTeamOpponent(season);
        ListTeams.list(teamCoach, otherTeam);
        Utils.waitEnter();

        //vale apena confirmar se a equipa e essa?

        IMatch match = findCoachingClubMatch(season);
        match.setTeam(teamCoach);
        match.setTeam(otherTeam);

        simulate(match);








    }

    private String getGamesString(Season season) {

        IMatch[] matches = season.getMatches(season.getCurrentRound());
        if (matches == null || matches.length == 0) {
            return "Erro. Nao existem jogos.";
        }

        StringBuilder sb = new StringBuilder();
        for(IMatch match : matches) {
            if(match != null) {
                sb.append("- ").append(match.toString()).append("\n");
            }
        }

        return sb.toString();
    }

    private IClub getOpponent(Season season) {
        IMatch match = findCoachingClubMatch(season);
        if (match == null) {
            throw new NullPointerException("Season is NULL.");
        }

        IClub coachingClub = getCoachingClub(season);
        if (coachingClub.equals(match.getHomeClub())) {
            return match.getAwayClub();
        } else {
            return match.getHomeClub();
        }
    }

    private String getOpponentName(Season season) {
        return getOpponent(season).getName();
    }

    private IMatch findCoachingClubMatch(Season season) {
        if (season == null) {
            return null;
        }
        IMatch[] matches = season.getMatches(season.getCurrentRound());
        IClub coachingClub = getCoachingClub(season);
        if (matches == null || coachingClub == null) {
            return null;
        }
        for (IMatch m : matches) {
            if (coachingClub.equals(m.getHomeClub()) || coachingClub.equals(m.getAwayClub())) {
                return m;
            }
        }
        return null;
    }

    private IClub getCoachingClub(Season season) {
        if (season == null) {
            return null;
        }
        IClub[] clubs = season.getCurrentClubs();
        int coachingClubIndex = season.getCoachingClubIndex();
        if (clubs == null || coachingClubIndex < 0 || coachingClubIndex >= clubs.length) {
            return null;
        }
        return clubs[coachingClubIndex];
    }

    private IClub getClub(Season season) {
        return season.getCurrentClubs()[season.getCoachingClubIndex()];
    }

    private Team createTeam(Season season, Formation formation) {
        Team team = new Team(season.getCurrentClubs()[season.getCoachingClubIndex()], formation);
        int countPlayers = 0;
        Reader reader = new Reader();
        IPlayer[] players = getClub(season).getPlayers();
        int index = 0;
        int count = 0;

        ListAllPlayers.indexPlayer(players);
        //gk
        index = reader.readInt(1, players.length, "Indique o GR: ");
        try{
            team.addPlayer(players[index - 1]);
            countPlayers++;
        }catch(Exception e){
            System.out.println("Erro ao adicionar jogador - " + e.getMessage());
        }
        //defenses
        do{
            index = reader.readInt(1, players.length, "Indique um defesa: ");
            try{
                team.addPlayer(players[index-1]);
                countPlayers++;
                count++;
            }catch (Exception e){
                System.out.println("Erro ao adicionar jogador - " + e.getMessage());
            }
        }while (count < formation.getNumDefenders());
        //mid
        count = 0;
        do{
            index = reader.readInt(1, players.length, "Indique um medio: ");
            try{
                team.addPlayer(players[index-1]);
                countPlayers++;
                count++;
            }catch (Exception e){
                System.out.println("Erro ao adicionar jogador - " + e.getMessage());
            }
        }while (count < formation.getNumMidfielders());
        //
        count = 0;
        do{
            index = reader.readInt(1, players.length, "Indique um avançado: ");
            try{
                team.addPlayer(players[index-1]);
                countPlayers++;
                count++;
            }catch (Exception e){
                System.out.println("Erro ao adicionar jogador - " + e.getMessage());
            }
        }while (count < formation.getNumAttackers());

        return team;
    }

    private Team createTeamOpponent(Season season) {

        GenerateTeams generateTeams = new GenerateTeams();

        IClub clubOpponent = getOpponent(season);

        return (Team) generateTeams.randomTeam(clubOpponent);



    }

    private void simulate(IMatch match) {
        MatchSimulator simulator = new MatchSimulator();

        simulator.simulate(match);

        if (match.getEvents().length == 0) {
            System.out.println("  (sem eventos)");
        } else {
            for (IEvent ev : match.getEvents()) {
                System.out.println(ev);
            }
        }



        System.out.println("Golos Casa: " + simulator.getHomeGoals());
        System.out.println("Golos Fora: " + simulator.getAwayGoals());


    }


}
