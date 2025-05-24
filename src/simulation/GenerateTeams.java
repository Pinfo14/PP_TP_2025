package simulation;

import com.ppstudios.footballmanager.api.contracts.player.IPlayer;
import com.ppstudios.footballmanager.api.contracts.player.IPlayerPosition;
import com.ppstudios.footballmanager.api.contracts.team.IClub;
import com.ppstudios.footballmanager.api.contracts.team.IPlayerSelector;
import com.ppstudios.footballmanager.api.contracts.team.ITeam;
import management.FormationManagement;
import player.PlayerPositionManage;
import team.Formation;
import team.RandomPlayerSelector;
import team.Team;

public class GenerateTeams {

    private IPlayerSelector playerSelector;
    private PlayerPositionManage positionManage;
    private FormationManagement formations;

    public GenerateTeams() {

        this.playerSelector = new RandomPlayerSelector();
        this.positionManage = new PlayerPositionManage();
        this.formations = new FormationManagement();
    }

    public ITeam randomTeam(IClub club) {


        Formation form = null;

        for (Formation formation : this.formations.getFormations()) { // Usar getFormations()
            if (hasMinimumPlayersForFormation(club, formation)) {
                form = formation;
                break;
            }
        }

        ITeam team = new Team(club, form);


        generatePlayersByPos(form.getNumAttackers(), this.positionManage.getPositionByDescription("Forward"), team);

        generatePlayersByPos(form.getNumMidfielders(), this.positionManage.getPositionByDescription("Midfielder"), team);

        generatePlayersByPos(form.getNumDefenders(), this.positionManage.getPositionByDescription("Defender"), team);


        generatePlayersByPos(1, this.positionManage.getPositionByDescription("Goalkeeper"), team);


        return team;
    }

    private boolean hasMinimumPlayersForFormation(IClub club, Formation formation) {
        int numDefenders = formation.getNumDefenders();
        int numMidfielders = formation.getNumMidfielders();
        int numAttackers = formation.getNumAttackers();

        // Contar players disponíveis por posição
        int availableDefenders = numPlayerByPos(club.getPlayers(), this.positionManage.getPositionByDescription("Defender"));
        int availableMidfielders = numPlayerByPos(club.getPlayers(), this.positionManage.getPositionByDescription("Midfielder"));
        int availableForwards = numPlayerByPos(club.getPlayers(), this.positionManage.getPositionByDescription("Forward"));
        int availableGoalkeepers = numPlayerByPos(club.getPlayers(), this.positionManage.getPositionByDescription("Goalkeeper"));

        // TODOS os requisitos devem ser satisfeitos (AND, não OR)
        return availableDefenders >= numDefenders &&
                availableMidfielders >= numMidfielders &&
                availableForwards >= numAttackers &&
                availableGoalkeepers >= 1; // Sempre precisa de pelo menos 1 GK
    }

    private int numPlayerByPos(IPlayer[] players, IPlayerPosition position) {
        int count = 0;
        for (IPlayer player : players) {
            if (player.getPosition().equals(position)) {
                count++;
            }
        }
        return count;
    }



    private void generatePlayersByPos(int num, IPlayerPosition position, ITeam team) {
        try {
            for (int pos = 0; pos < num; pos++) {
                IPlayer player = playerSelector.selectPlayer(team.getClub(), position);
                while (veryfiPlayerInTeam(team.getPlayers(), player)) {
                    player = playerSelector.selectPlayer(team.getClub(), position);
                }
                try {
                    team.addPlayer(player);
                } catch (Exception e) {
                    System.out.println("Erro ao adicionar jogador: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            System.out.println("Erro ao gerar equipe: " + e.getMessage());
        }
    }


    private boolean veryfiPlayerInTeam(IPlayer[] team, IPlayer player) {
        for (int i = 0; i < team.length; i++) {
            if (team[i] != null && team[i].equals(player)) {
                return true;
            }
        }
        return false;
    }


}
