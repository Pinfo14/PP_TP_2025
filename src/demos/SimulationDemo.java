package demos;

import com.ppstudios.footballmanager.api.contracts.event.IEvent;
import com.ppstudios.footballmanager.api.contracts.league.ISeason;
import com.ppstudios.footballmanager.api.contracts.match.IMatch;
import com.ppstudios.footballmanager.api.contracts.player.IPlayerPosition;
import com.ppstudios.footballmanager.api.contracts.simulation.MatchSimulatorStrategy;
import com.ppstudios.footballmanager.api.contracts.team.IFormation;
import com.ppstudios.footballmanager.api.contracts.team.IPlayerSelector;
import com.ppstudios.footballmanager.api.contracts.team.ITeam;
import event.*;
import com.ppstudios.footballmanager.api.contracts.event.IEventManager;
import com.ppstudios.footballmanager.api.contracts.player.IPlayer;
import com.ppstudios.footballmanager.api.contracts.team.IClub;
import imports.Imports;
import league.League;
import league.Season;
import match.Match;
import player.Player;
import player.PlayerPositionManage;
import simulation.MatchSimulator;
import team.Club;
import team.Formation;
import team.RandomPlayerSelector;
import team.Team;

public class SimulationDemo {

    private static IPlayer selectPlayerUnique(IClub club, IPlayerPosition position, IPlayer[] alreadySelected, IPlayerSelector selector) {
        IPlayer[] all = club.getPlayers();
        IPlayer[] notUsed = excludeAlreadySelected(all, alreadySelected);
        IClub tempClub = new Club(club.getName(), club.getCode(), club.getCountry(), club.getFoundedYear(), club.getStadiumName(), club.getLogo());

        for (IPlayer p : notUsed) {
            try {
                tempClub.addPlayer(p);
            } catch (Exception ignored) {}
        }

        return selector.selectPlayer(tempClub, position);
    }

    private static IPlayer[] excludeAlreadySelected(IPlayer[] players, IPlayer[] selected) {
        int count = 0;
        for (IPlayer p : players) {
            if (!isInArray(p, selected)) count++;
        }

        IPlayer[] filtered = new IPlayer[count];
        int idx = 0;
        for (IPlayer p : players) {
            if (!isInArray(p, selected)) {
                filtered[idx++] = p;
            }
        }
        return filtered;
    }

    private static boolean isInArray(IPlayer p, IPlayer[] array) {
        for (IPlayer other : array) {
            if (other != null && other.equals(p)) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {



        League liga = new League("Liga Portugal");

        Season season1 = new Season("Liga Portugal", 2023);

        Imports importClubs = new Imports();

        IClub[] clubs = importClubs.importClubs();
        for (IClub c : clubs) {
            System.out.println(c.getName());
        }

        IPlayer[] playersPorto = importClubs.importPlayers("Porto.json");

        for(IPlayer p : playersPorto){
            try{
                clubs[0].addPlayer(p);
                clubs[1].addPlayer(p);
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
        }

        Formation formation433 = new Formation("4-3-3", 4, 3, 3,0);
        IPlayerSelector selector = new RandomPlayerSelector();
        PlayerPositionManage positionManager = new PlayerPositionManage();

        IPlayer[] players = new IPlayer[11]; // 1 GK + 4 DEF + 3 MID + 3 ATT
        int idx = 0;

        players[idx++] = selectPlayerUnique(clubs[0], positionManager.getPositionByDescription("Goalkeeper"), players, selector);

        for (int i = 0; i < formation433.getNumDefenders(); i++) {
            players[idx++] = selectPlayerUnique(clubs[0], positionManager.getPositionByDescription("Defender"), players, selector);
        }
        for (int i = 0; i < formation433.getNumMidfielders(); i++) {
            players[idx++] = selectPlayerUnique(clubs[0], positionManager.getPositionByDescription("Midfielder"), players, selector);
        }
        for (int i = 0; i < formation433.getNumAttackers(); i++) {
            players[idx++] = selectPlayerUnique(clubs[0], positionManager.getPositionByDescription("Forward"), players, selector);
        }


      int conta=0;
      for(IPlayer p : players){
        System.out.println(conta++ + " " + p.toString());
      }


      ITeam team = new Team(clubs[0]);
      ITeam team2 = new Team(clubs[1]);
      team.setFormation(formation433);
      team2.setFormation(formation433);

      for(IPlayer p : players){
          try{
              team.addPlayer(p);
              team2.addPlayer(p);
          }catch (Exception e){
              System.out.println(e.getMessage());
          }
      }


        try{
            liga.createSeason(season1);
        } catch (Exception e){
            System.out.println(e.getMessage());
        }

        ISeason season = liga.getSeason(2023);



        for(IClub c : clubs) {
            try {
                season.addClub(c);
            }catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }



        System.out.println(season.getSchedule());

        Match match = (Match) (season.getMatches()[0]);

        match.setHomeTeam(team);
        match.setAwayTeam(team2);


        MatchSimulatorStrategy simulator = new MatchSimulator();

        simulator.simulate(match);


      for(IEvent e: match.getEvents()){
          System.out.println(e+"\n");
      }

      System.out.println(match.getEventCount());

    }
}
