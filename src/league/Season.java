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

    private static final int MAX_CLUBS = 18;
    private String name;
    private int year;
    private IClub[] clubs;
    private int numClubs;
    private IStanding[] standings; //mesmo index que os clubs
    private ISchedule schedule;



    private int pointsPerLoss;
    private int pointsPerWin;
    private int pointsPerDraw;

    private int currentRound;



    public Season(String leagueName, int year) {
        this.name = String.format("%s %d", leagueName, year);
        this.year = year;
        this.clubs = new Club[MAX_CLUBS];
        this.numClubs = 0;

        this.pointsPerLoss = 0;
        this.pointsPerWin = 3;
        this.pointsPerDraw = 1;
        this.currentRound = 0;





    }

    @Override
    public int getYear() {
        return year;
    }


    //se a seasom ja tiver começado nao pode começar outra liga? e remover ?

    @Override
    public boolean addClub(IClub iClub) {
        if (iClub == null) {
            throw new IllegalArgumentException("Club cannot be null.");
        }

        if(clubExist(iClub)) {
            throw new IllegalArgumentException("Club already exists.");
        }

        if(numClubs == MAX_CLUBS) {
            throw new IllegalStateException("League is full.");
        }

        clubs[numClubs] = iClub;
        numClubs++;

        generateSchedule();

        return true;
    }

    @Override
    public boolean removeClub(IClub iClub) {
        if (iClub == null) {
            throw new IllegalArgumentException("Club cannot be null.");
        }

        int index = clubIndex(iClub);

        if(index == -1) {
            throw new IllegalStateException("Club does not exist in the league.");
        }

        for(int i = index; i < numClubs - 1; i++) {
            clubs[i] = clubs[i + 1];
        }

        clubs[--numClubs] = null;
        System.out.println("Schedule generated.");

        generateSchedule();

        return true;
    }

    @Override
    public void generateSchedule() {

        schedule = new Schedule(clubs, numClubs);

    }

    private int calculateNumberOfMatches() {

        return numClubs * (numClubs - 1);

    }

    @Override
    public IMatch[] getMatches() {
        IMatch[] matches = new IMatch[calculateNumberOfMatches()];

        IMatch[] scheduledMatches = null;
        try {
            scheduledMatches = schedule.getAllMatches();
        } catch (IllegalStateException e) {
            System.err.println("No matches found.");
        }

        System.arraycopy(scheduledMatches, 0, matches, 0, scheduledMatches.length);

        return matches;

    }

    @Override
    public IMatch[] getMatches(int i) {
        IMatch[] matches = new IMatch[calculateNumberOfMatches()];

        IMatch[] scheduledMatches = schedule.getAllMatches();

        System.arraycopy(scheduledMatches, 0, matches, 0, scheduledMatches.length);

        return matches;

    }

    @Override
    public void simulateRound() {

    }

    @Override
    public void simulateSeason() {

    }

    @Override
    public int getCurrentRound() {
        return currentRound;
    }

    @Override
    public boolean isSeasonComplete() {

        //verifica se os jogos estao todos realizados


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
        //System.out.println(schedule.toString());
        return schedule;
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
        return 0;
    }

    @Override
    public IClub[] getCurrentClubs() {
        IClub[] clubTemp = new IClub[numClubs];

        System.arraycopy(clubs, 0, clubTemp, 0, numClubs);

        return clubTemp;
    }

    @Override
    public void exportToJson() throws IOException {

    }

    private boolean clubExist(IClub iclub) {
        for(int i = 0; i < numClubs; i++) {
            if(clubs[i].equals(iclub)) {
                return true;
            }
        }

        return false;
    }

    private int clubIndex(IClub iclub) {
        for(int i = 0; i < numClubs; i++) {
            if(clubs[i].equals(iclub)) {
                return i;
            }
        }

        return -1;
    }




    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }

        Season other = (Season) obj;

        return true ;// ou outra comparação relevante
    }

    public String teste () {

        return schedule.toString();

    }
}