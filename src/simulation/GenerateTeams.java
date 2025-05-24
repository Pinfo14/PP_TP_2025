package simulation;
import com.ppstudios.footballmanager.api.contracts.player.IPlayer;
import com.ppstudios.footballmanager.api.contracts.player.IPlayerPosition;
import com.ppstudios.footballmanager.api.contracts.team.IClub;
import com.ppstudios.footballmanager.api.contracts.team.IPlayerSelector;
import com.ppstudios.footballmanager.api.contracts.team.ITeam;
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
        ITeam team = new Team(club);
        Formation form = selectRandomForm();
        team.setFormation(form);

        //verificar se o clube tem o numero necessario para a formação!!

        generatePlayersByPos(form.getNumAttackers(), this.positionManage.getPositionByDescription("Forward"), team);

        generatePlayersByPos(form.getNumMidfielders(), this.positionManage.getPositionByDescription("Midfielder"), team);

        generatePlayersByPos(form.getNumDefenders(), this.positionManage.getPositionByDescription("Defender"), team);

        if (asSriker(club)) {
            generatePlayersByPos(form.getNumStrikers(), this.positionManage.getPositionByDescription("Striker"), team);
        } else {
            generatePlayersByPos(form.getNumStrikers(), this.positionManage.getPositionByDescription("Forward"), team);
        }

        generatePlayersByPos(1, this.positionManage.getPositionByDescription("Goalkeeper"), team);



        return team;
    }



    private boolean asSriker(IClub club) {
        for (IPlayer p : club.getPlayers()) {
            if (p.getPosition().equals(this.positionManage.getPositionByDescription("Striker"))) {
                return true;
            }
        }
        return false;
    }



    private Formation selectRandomForm() {
        Formation[] formations = this.formations.getFormations();
        return formations[(int) (Math.random() * formations.length)];
    }


    private void generatePlayersByPos(int num, IPlayerPosition position, ITeam team) {

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
    }


    private boolean veryfiPlayerInTeam(IPlayer[] team, IPlayer player) {
        for (int i = 0; i < team.length; i++) {
            if (team[i] != null && team[i].equals(player)) {
                return true;
            }
        }
        return false;
    }


     //export pra json usar JSONObject e JSONArrays
 }
