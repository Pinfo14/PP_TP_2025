package league;

import com.ppstudios.footballmanager.api.contracts.league.ISchedule;
import com.ppstudios.footballmanager.api.contracts.league.ISeason;
import com.ppstudios.footballmanager.api.contracts.league.IStanding;
import com.ppstudios.footballmanager.api.contracts.match.IMatch;
import com.ppstudios.footballmanager.api.contracts.simulation.MatchSimulatorStrategy;
import com.ppstudios.footballmanager.api.contracts.team.IClub;
import team.Club;

import java.io.IOException;
/**
 * Nome: Emanuel Jose Teixeira Pinto
 * Número: 8230371
 * Turma: LEI1T1
 *
 * Nome: Roberto Cristiano Martins Faria
 * Número: 8230067
 * Turma: LEI1T2
 */
public class Season implements ISeason {

    private String name;
    private int year;
    private Club[] clubs;
    private Standing[] standings;

    private int pointsPerLoss;
    private int pointsPerWin;
    private int pointsPerDraw;

    private int currentRound;
    private int numberOfMatches;


    /**
     * CONSTRUTOR SEM ESTAR CONFIGURADO
     * @param year
     */
    public Season(int year) {
        this.year = year;
        this.clubs = new Club[20];

        this.pointsPerLoss = 0;
        this.pointsPerWin = 3;
        this.pointsPerDraw = 1;



    }

    @Override
    public int getYear() {
        return year;
    }

    @Override
    public boolean addClub(IClub iClub) {
        return false;
    }

    @Override
    public boolean removeClub(IClub iClub) {
        return false;
    }

    @Override
    public void generateSchedule() {

    }

    @Override
    public IMatch[] getMatches() {
        return new IMatch[0];
    }

    @Override
    public IMatch[] getMatches(int i) {
        return new IMatch[0];
    }

    @Override
    public void simulateRound() {

    }

    @Override
    public void simulateSeason() {

    }

    @Override
    public int getCurrentRound() {
        return 0;
    }

    @Override
    public boolean isSeasonComplete() {
        return false;
    }

    @Override
    public void resetSeason() {

    }

    @Override
    public String displayMatchResult(IMatch iMatch) {
        return "";
    }

    @Override
    public void setMatchSimulator(MatchSimulatorStrategy matchSimulatorStrategy) {

    }

    @Override
    public IStanding[] getLeagueStandings() {
        return new IStanding[0];
    }

    @Override
    public ISchedule getSchedule() {
        return null;
    }

    @Override
    public int getPointsPerWin() {
        return pointsPerWin;
    }

    @Override
    public int getPointsPerDraw() {
        return pointsPerDraw;
    }

    @Override
    public int getPointsPerLoss() {
        return pointsPerLoss;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getMaxTeams() {
        return 0;
    }

    @Override
    public int getMaxRounds() {
        return 0;
    }

    @Override
    public int getCurrentMatches() {
        return 0;
    }

    @Override
    public int getNumberOfCurrentTeams() {
        int count = 0;
        for (Club club : clubs) {
            if(club != null) {
                count++;
            }
        }
        return count;
    }

    @Override
    public IClub[] getCurrentClubs() {
        return new IClub[0];
    }

    @Override
    public void exportToJson() throws IOException {

    }

    public int oioi(){
        return 2;
    }

    @Override
    public boolean equals(Object object){
        if(this == object){
            return true;
        }



        return true;
    }
}
