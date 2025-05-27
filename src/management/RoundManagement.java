package management;

import com.ppstudios.footballmanager.api.contracts.match.IMatch;
import com.ppstudios.footballmanager.api.contracts.player.IPlayer;
import com.ppstudios.footballmanager.api.contracts.team.IClub;
import com.ppstudios.footballmanager.api.contracts.team.IFormation;
import com.ppstudios.footballmanager.api.contracts.team.ITeam;
import league.Season;
import match.Match;
import menus.ListAllPlayers;
import menus.RoundMenu;
import menus.SeasonMenu;
import player.Player;
import reader.Reader;
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
            switch (indexFormation) {
                case 0:
                    int defense = reader.readInt(1,10, "Numero de defesas: ");
                    int middle = reader.readInt(1,10, "Numero de medios: ");
                    int attackers = reader.readInt(1,10, "Numero de avançados: ");
                    formationManagement.addFormation(defense, middle, attackers);
            }

        } while (indexFormation==0);

        Formation formation = (Formation) formationManagement.getFormation(indexFormation-1);
        Team team = createTeam(season, formation);


        IPlayer[] players = createTeamPlayers(season, formation.getNumDefenders(),formation.getNumMidfielders(),formation.getNumAttackers());

        IMatch match = findCoachingClubMatch(season);
        match.setTeam(new Team(getClub(season), formation, players, formation.getNumDefenders(), formation.getNumMidfielders(), formation.getNumAttackers()));

        System.out.println(match);

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

    private String getOpponentName(Season season) {
        IMatch match = findCoachingClubMatch(season);
        if (match == null) {
            return "desconhecido";
        }
        IClub coachingClub = getCoachingClub(season);
        if (coachingClub.equals(match.getHomeClub())) {
            return match.getAwayClub().getName();
        } else {
            return match.getHomeClub().getName();
        }
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

    private IPlayer[] createTeamPlayers(Season season, int defense, int middle, int attackers) {
        Reader reader = new Reader();
        IPlayer[] players = getClub(season).getPlayers();
        IPlayer[] teamPlayers = new IPlayer[11];
        int countPlayers = 0;

        ListAllPlayers.indexPlayer(players);

        int index = reader.readInt(1, players.length, "Indique o GR: ");
        teamPlayers[countPlayers++] = players[index - 1];

        countPlayers = selectPlayers("defesa", defense,  players, teamPlayers, countPlayers);

        countPlayers = selectPlayers("médio", middle, players, teamPlayers, countPlayers);

        countPlayers = selectPlayers("avançado", attackers, players, teamPlayers, countPlayers);

        return teamPlayers;
    }

    private int selectPlayers(String posicao, int quantidade, IPlayer[] players, IPlayer[] teamPlayers, int countPlayers) {
        Reader reader = new Reader();
        int count = 0;

        while (count < quantidade) {

            boolean repetido = false;
            int index = reader.readInt(1, players.length, "Indique um " + posicao + ": ");
            if (index != -1) {
                for (int i = 0; i < countPlayers; i++) {
                    if (teamPlayers[i].equals(players[index - 1])) {
                        repetido = true;
                        break;
                    }
                }
                if (!repetido) {
                    teamPlayers[countPlayers++] = players[index - 1];
                    count++;
                    System.out.println("Jogador adicionado com sucesso.");
                } else {
                    System.out.println("Jogador já está na equipa.");
                }
            } else {
                System.out.println("Índice inválido.");
            }
        }
        return countPlayers;
    }

    private Team createTeam(Season season, Formation formation) {
        Team team = new Team(season.getCurrentClubs()[season.getCoachingClubIndex()], formation);

        Reader reader = new Reader();
        IPlayer[] players = getClub(season).getPlayers();
        int index = 0, countPlayers = 0;

        ListAllPlayers.indexPlayer(players);
        do{
            index = reader.readInt(1, players.length, "Indique um jogador: ");
            try {
                team.addPlayer(players[index - 1]);
                countPlayers++;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }while (countPlayers < 11);
        return team;
    }




}
