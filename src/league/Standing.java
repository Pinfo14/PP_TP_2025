package league;

import com.ppstudios.footballmanager.api.contracts.league.IStanding;
import com.ppstudios.footballmanager.api.contracts.team.IClub;
import com.ppstudios.footballmanager.api.contracts.team.ITeam;
import org.json.simple.JSONObject;

/**
 * Nome: Emanuel Jose Teixeira Pinto
 * Número: 8230371
 * Turma: LEI1T1
 *
 * Nome: Roberto Cristiano Martins Faria
 * Número: 8230067
 * Turma: LEI1T2
 */
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
        this.numberDraws = 0;
        this.numberWins = 0;
        this.numberLosses = 0;
        this.numberGoalsScored = 0;
        this.numberGoalsConceded = 0;
        this.points = 0;
    }

    public Standing(IClub club,int points, int numberWins, int numberLosses, int numberDraws, int numberGoalsScored, int numberGoalsConceded) {
        this.club = club;
        this.numberDraws = numberDraws;
        this.numberWins = numberWins;
        this.numberLosses = numberLosses;
        this.numberGoalsScored = numberGoalsScored;
        this.numberGoalsConceded = numberGoalsConceded;
        this.points = points;
    }

    public IClub getClub() {
        return club;
    }

    @Override
    public ITeam getTeam() {
        return team;
    }

    @Override
    public int getPoints() {
        return points;
    }

    @Override
    public void addPoints(int i) {
        if (i < 0) {
            throw new IllegalArgumentException("Number of points cannot be negative");
        }
        this.points += i;
    }

    @Override
    public void addWin(int pointsPerWin) {
        if (pointsPerWin < 0) {
            throw new IllegalArgumentException("Points per win cannot be negative");
        }
        this.numberWins += 1;
        this.points += pointsPerWin;
    }

    @Override
    public void addDraw(int pointsPerDraw) {
        if (pointsPerDraw < 0) {
            throw new IllegalArgumentException("Points per draw cannot be negative");
        }
        this.numberDraws += 1;
        this.points += pointsPerDraw;
    }

    @Override
    public void addLoss(int pointsPerLoss) {
        if (pointsPerLoss < 0) {
            throw new IllegalArgumentException("Points per loss cannot be negative");
        }
        this.numberLosses += 1;
        this.points += pointsPerLoss;
    }

    /**
     * Adiciona golos marcados
     */
    public void addGoalsScored(int goals) {
        if (goals < 0) {
            throw new IllegalArgumentException("Number of goals cannot be negative");
        }
        this.numberGoalsScored += goals;
    }

    /**
     * Adiciona golos sofridos
     */
    public void addGoalsConceded(int goals) {
        if (goals < 0) {
            throw new IllegalArgumentException("Number of goals cannot be negative");
        }
        this.numberGoalsConceded += goals;
    }

    /**
     * Método  para atualizar depois de uma vitória
     */
    public void addWinResult(int goalsScored, int goalsConceded, int pointsPerWin) {
        addGoalsScored(goalsScored);
        addGoalsConceded(goalsConceded);
        addWin(pointsPerWin);
    }

    /**
     * Método  para atualizar depois de um empate
     */
    public void addDrawResult(int goalsScored, int goalsConceded, int pointsPerDraw) {
        addGoalsScored(goalsScored);
        addGoalsConceded(goalsConceded);
        addDraw(pointsPerDraw);
    }

    /**
     * Método  para atualizar depois de uma derrota
     */
    public void addLossResult(int goalsScored, int goalsConceded, int pointsPerLoss) {
        addGoalsScored(goalsScored);
        addGoalsConceded(goalsConceded);
        addLoss(pointsPerLoss);
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
        return (numberDraws + numberLosses + numberWins);
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
        return (numberGoalsScored - numberGoalsConceded);
    }

    /**
     * Reset das estatísticas (útil para nova época)
     */
    public void reset() {
        this.numberDraws = 0;
        this.numberWins = 0;
        this.numberLosses = 0;
        this.numberGoalsScored = 0;
        this.numberGoalsConceded = 0;
        this.points = 0;
    }

    public JSONObject getJsonObj() {
        JSONObject standing = new JSONObject();
        standing.put("Club", this.club.getCode());
        standing.put("Points", this.points);
        standing.put("Wins", this.numberWins);
        standing.put("Draws", this.numberDraws);
        standing.put("Losses", this.numberLosses);
        standing.put("GoalsScored", this.numberGoalsScored);
        standing.put("GoalsConceded", this.numberGoalsConceded);
        standing.put("GoalDifference", getGoalDifference());
        standing.put("TotalMatches", getTotalMatches());
        return standing;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("Club: ").append(club.getName()).append(" ");
        sb.append("| Pts: ").append(points).append(" J: ").append(getTotalMatches());
        sb.append(" GM:").append(numberGoalsScored).append(" GS:").append(numberGoalsConceded);
        sb.append(" Dif:").append(getGoalDifference());
        sb.append(" V:").append(numberWins).append(" E:").append(numberDraws).append(" D:").append(numberLosses);

        return sb.toString();
    }

}