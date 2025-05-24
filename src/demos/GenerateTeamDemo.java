package demos;

import com.ppstudios.footballmanager.api.contracts.team.IClub;
import com.ppstudios.footballmanager.api.contracts.team.ITeam;
import imports.Imports;
import simulation.GenerateTeams;

public class GenerateTeamDemo {
    public static void main(String[] args) {

        Imports imports = new Imports();
        IClub[] clubs = imports.importPlayersToClub();

        GenerateTeams generateTeams = new GenerateTeams();

        for(IClub c : clubs){
          ITeam team = generateTeams.randomTeam(c);
          System.out.println(c.getName());
          System.out.println(team);
        }
    }
}
