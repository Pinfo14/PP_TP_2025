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
        //Formation formation = (Formation) formationManagement.getFormation(indexFormation);
        //setCoachFormation(season, formation);

        //CRIAR MENU PARA AVISAR QUE VAI ESCOLHER JOGADORES
        System.out.println("Selecione os jogadores: ");
        Utils.waitEnter();

        IPlayer[] players = createTeamPlayers(season);

        //Seleciona jogadores




    }

    private void setCoachFormation(Season season, IFormation formation) {
        if (season == null || formation == null) {
            System.out.println("NULL - Season/formation");
            return;
        }

        int coachingIndex = season.getCoachingClubIndex();
        IClub[] clubs = season.getCurrentClubs();
        if (clubs == null || coachingIndex < 0 || coachingIndex >= clubs.length) {
            System.out.println("Erro - Clube a treinar invalido.");
            return;
        }

        IClub coachClub = clubs[coachingIndex];
        IMatch[] matches = season.getMatches(season.getCurrentRound());
        if (matches == null) {
            System.out.println("Erro - Nao foi possivel encontrar jogos.");
            return;
        }

        for (IMatch match : matches) {
            try {
                if (coachClub.equals(match.getHomeClub()) || coachClub.equals(match.getAwayClub())) {
                    //match.setTeam(new Team(coachClub, formation));
                }
            } catch (NullPointerException | IllegalStateException e) {
                System.out.println("Erro ao definir formação: " + e.getMessage());
            }
        }
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
        if (season == null) {
            return "desconhecido";
        }

        IMatch[] matches = season.getMatches(season.getCurrentRound());
        if (matches == null || matches.length == 0) {
            return "desconhecido";
        }

        IClub[] clubs = season.getCurrentClubs();
        int coachingClubIndex = season.getCoachingClubIndex();
        if (clubs == null || coachingClubIndex < 0 || coachingClubIndex >= clubs.length) {
            return "desconhecido";
        }
        IClub club = clubs[coachingClubIndex];

        for (IMatch match : matches) {
            if (club.equals(match.getHomeClub())) {
                return match.getAwayClub().getName();
            }
            if (club.equals(match.getAwayClub())) {
                return match.getHomeClub().getName();
            }
        }

        return "desconhecido";
    }

    private IClub getClub(Season season) {
        return season.getCurrentClubs()[season.getCoachingClubIndex()];
    }

    private IPlayer[] createTeamPlayers(Season season) {
        int countPlayers = 0, index = -1;
        Reader reader = new Reader();
        boolean repeted;
        IPlayer[] players = getClub(season).getPlayers();
        IPlayer[] teamPlayers = new IPlayer[11];

        ListAllPlayers.indexPlayer(players);
        do{
            repeted = false;
            index = reader.readInt(1,players.length,"Jogador a adicionar: ");
            if(index != -1) {
                for(int i = 0; i < countPlayers; i++) {
                    if (teamPlayers[i].equals(players[index])) {
                        repeted = true;
                        System.out.println("Jogador não encontrado.");
                    }
                }
                if(!repeted) {
                    teamPlayers[countPlayers] = players[index-1];
                    countPlayers++;
                    System.out.println("Jogador adicionado com sucesso.");
                }else{
                    System.out.println("Jogador ja esta na equipa.");
                }
            }

        }while (countPlayers < 11);

        return teamPlayers;
    }




}
