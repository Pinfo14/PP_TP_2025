package simulation;

import com.ppstudios.footballmanager.api.contracts.player.IPlayer;
import com.ppstudios.footballmanager.api.contracts.player.IPlayerPosition;
import com.ppstudios.footballmanager.api.contracts.team.IClub;
import com.ppstudios.footballmanager.api.contracts.team.IPlayerSelector;
import com.ppstudios.footballmanager.api.contracts.team.ITeam;
import imports.Imports;
import player.PlayerPositionManage;
import team.DefaultFormations;
import team.Formation;
import team.RandomPlayerSelector;
import team.Team;

public class GenerateTeams {

    private IPlayerSelector playerSelector;
    private PlayerPositionManage positionManage;
    private DefaultFormations formations;

    public GenerateTeams() {

        this.playerSelector = new RandomPlayerSelector();
        this.positionManage = new PlayerPositionManage();
        this.formations = new DefaultFormations();
    }

    public ITeam randomTeam(IClub club) {
        Formation form = selectRandomForm();
        ITeam team = new Team(club,form);

        IPlayer[] players= this.fullTeam(form,club);

        for (IPlayer p : players) {
            try {
                team.addPlayer(p);
            } catch (Exception e) {
                System.out.println("Erro ao adicionar jogador: " + e.getMessage());
            }
        }
        return team;
    }

    private Formation selectRandomForm() {
        Formation[] formations = this.formations.getFormations();
        return formations[(int) (Math.random() * formations.length)];
    }

    private IPlayer generateGK(IClub club) {
        return this.playerSelector.selectPlayer(club, this.positionManage.getPositionByDescription("Goalkeeper"));
    }

    private IPlayer[] generatePlayersByPos(int num, IPlayerPosition position, IClub club) {

        IPlayer[] players = new IPlayer[num];
        int idx = 0;

        for (int def = 0; def < num; def++) {
            IPlayer player = playerSelector.selectPlayer(club, position);
            while (veryfiPlayerInTeam(players, player)) {
                player = playerSelector.selectPlayer(club, position);
            }
            players[idx++] = player;
        }
        return players;
    }

    private boolean veryfiPlayerInTeam(IPlayer[] team, IPlayer player) {
        for (int i = 0; i < team.length; i++) {
            if (team[i] != null && team[i].equals(player)) {
                return true;
            }
        }
        return false;
    }

    private IPlayer[] fullTeam(Formation formation,IClub club) {

        IPlayer[] players= new IPlayer[11];
        int idx = 0;

        IPlayer[] defenders = generatePlayersByPos(formation.getNumDefenders(), this.positionManage.getPositionByDescription("Defender"),club);
        IPlayer[] midfielders = generatePlayersByPos(formation.getNumMidfielders(), this.positionManage.getPositionByDescription("Midfielder"),club);
        IPlayer[] attackers = generatePlayersByPos(formation.getNumAttackers(), this.positionManage.getPositionByDescription("Forward"),club);
        /*
        if (formation.getNumStrikers()>0){
            IPlayer[] strikers = generatePlayersByPos(formation.getNumStrikers(), this.positionManage.getPositionByDescription("Striker"),club);

            for (IPlayer p : strikers) {
                players[idx++] = p;
            }
        }
        */
        for (IPlayer p : defenders) {
            players[idx++] = p;
        }
        for (IPlayer p : midfielders) {
            players[idx++] = p;
        }
        for (IPlayer p : attackers) {
            players[idx++] = p;
        }

        players[idx++] = generateGK(club);

        return players;
    }
}
