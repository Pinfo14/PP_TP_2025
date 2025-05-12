package league;

import com.ppstudios.footballmanager.api.contracts.league.IStanding;
import com.ppstudios.footballmanager.api.contracts.team.IClub;
import com.ppstudios.footballmanager.api.contracts.team.ITeam;
import team.Club;

public class Standing implements IStanding {

    private IClub club;
    private ITeam team;
    private int numberDraws;
    private int numberWins;
    private int numberLosses;
    private int numberGoalsScored;
    private int numberGoalsConceded;
    private int points;


    public Standing(IClub club) {
        this.club = club;
        //this.team = null;
        this.numberDraws = 0;
        this.numberWins = 0;
        this.numberLosses = 0;
        this.numberGoalsScored = 0;
        this.numberGoalsConceded = 0;
        this.points = 0;
    }

    public IClub getClub() {
        return club;
    }

    @Override
    public ITeam getTeam() {
        return team;
    }

    @Override
    public int getPoints() {return points;}

    @Override
    public void addPoints(int i) {
        if (i < 0) {
            throw new IllegalArgumentException("Number of points cannot be negative");
        }
        this.points += i;
    }

    @Override
    public void addWin(int i) {
        if (i < 0) {
            throw new IllegalArgumentException("Number of wins cannot be negative");
        }
        this.numberWins += i;
    }

    @Override
    public void addDraw(int i) {
        if (i < 0) {
            throw new IllegalArgumentException("Number of draws cannot be negative");
        }
        this.numberDraws += i;
    }

    @Override
    public void addLoss(int i) {
        if (i < 0) {
            throw new IllegalArgumentException("Number of losses cannot be negative");
        }
        this.numberLosses += i;
    }

    @Override
    public int getWins() {
        return numberWins;
    }

    @Override
    public int getDraws() {
        return numberDraws;
    }

    @Override
    public int getLosses() {
        return numberLosses;
    }

    @Override
    public int getTotalMatches() {
        return (numberDraws+numberLosses+numberWins);
    }

    @Override
    public int getGoalScored() {
        return numberGoalsScored;
    }

    @Override
    public int getGoalsConceded() {
        return numberGoalsConceded;
    }

    @Override
    public int getGoalDifference() {
        return (numberGoalsScored+numberGoalsConceded);
    }




    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("Club: ").append(club).append(" ");
        sb.append("| Pts: ").append(points).append(" J: ").append(getTotalMatches());
        sb.append("").append(" GM:").append(numberGoalsScored).append(" GS:").append(numberGoalsConceded);
        sb.append(" E:").append(numberDraws).append(" D:").append(numberLosses).append(" V:").append(numberWins);

        return sb.toString();

    }

}
