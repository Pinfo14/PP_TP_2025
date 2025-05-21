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

    private int maxClubs;
    private String name;
    private int year;
    private IClub[] clubs;
    private int numClubs;
    private int coachingClubIndex;
    private IStanding[] standings; //mesmo index que os clubs
    private ISchedule schedule;
    private int pointsPerLoss;
    private int pointsPerWin;
    private int pointsPerDraw;
    private int currentRound;



    public Season(String leagueName, int year) {
        this.maxClubs = 18;
        this.name = leagueName;
        this.year = year;
        this.clubs = new Club[maxClubs];
        this.standings = new IStanding[maxClubs];
        this.numClubs = 0;
        this.pointsPerLoss = 0;
        this.pointsPerWin = 3;
        this.pointsPerDraw = 1;
        this.currentRound = 0;
        this.coachingClubIndex = -1;
    }

    @Override
    public int getYear() {
        return year;
    }

    public void setCoachingClubIndex(int coachingClubIndex) {
        this.coachingClubIndex = coachingClubIndex;
    }

    public int getCoachingClubIndex() {
        return coachingClubIndex;
    }

    public String getNameCoachingClub() {
        if (coachingClubIndex == -1) {
            return "";
        }
        return clubs[coachingClubIndex].getName();
    }

    @Override
    public boolean addClub(IClub iClub) {
        //se a seasom ja tiver começado nao pode começar outra liga? e remover ?
        if (iClub == null) {
            throw new IllegalArgumentException("Club cannot be null.");
        }

        if(clubExist(iClub)) {
            throw new IllegalArgumentException("Club already exists.");
        }

        if(numClubs == maxClubs) {
            throw new IllegalStateException("League is full.");
        }

        standings[numClubs] = new Standing(iClub);
        clubs[numClubs] = iClub;
        numClubs++;
        currentRound = 1;
        generateSchedule();

        return true;
    }

    @Override
    public boolean removeClub(IClub iClub) {
        //todo verifica se algum jogo ja foi jogado
        if (iClub == null) {
            throw new IllegalArgumentException("Club cannot be null.");
        }

        int index = clubIndex(iClub);

        if(index == -1) {
            throw new IllegalStateException("Club does not exist in the league.");
        }

        if(index == coachingClubIndex) {
            coachingClubIndex = -1;
        }

        for(int i = index; i < numClubs - 1; i++) {
            clubs[i] = clubs[i + 1];
        }

        for(int i = index; i < numClubs - 1; i++) {
            standings[i] = standings[i + 1];
        }

        clubs[--numClubs] = null;
        currentRound = 1;
        generateSchedule();

        return true;
    }

    @Override
    public void generateSchedule() {

        schedule = new Schedule(clubs, numClubs);

    }

    private int calculateNumberOfMatches() {
        int totalClubs = numClubs;
        if (totalClubs % 2 != 0) {
            totalClubs++;        }
        return totalClubs * (totalClubs - 1);
    }

    @Override
    public IMatch[] getMatches() {
        int numMatches = calculateNumberOfMatches();
        IMatch[] matches = new IMatch[numMatches];

        System.out.println("Matches: " + numMatches);

        IMatch[] scheduledMatches = null;

        try {
            scheduledMatches = schedule.getAllMatches();
        } catch (IllegalStateException e) {
            System.out.println("No matches found.");
            return matches;
        }

        int newLength;
        if (scheduledMatches.length < matches.length) {
            newLength = scheduledMatches.length;
        } else {
            newLength = matches.length;
        }
        System.arraycopy(scheduledMatches, 0, matches, 0, newLength);

        return matches;

    }

    @Override
    public IMatch[] getMatches(int i) {

        if(i < 0 || i > schedule.getNumberOfRounds()) {
            throw new IllegalArgumentException("Jornada Inexistente");
        }

        IMatch[] scheduledMatches = new IMatch[calculateNumberOfMatches()];

        try {
            scheduledMatches = schedule.getMatchesForRound(i);
        } catch (IllegalArgumentException | IllegalStateException e) {
            throw e;
        }

        return scheduledMatches;

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
        IStanding[] standingsTemp = new Standing[standings.length];

        System.arraycopy(standings, 0, standingsTemp, 0, standings.length);

        return standingsTemp;

    }

    @Override
    public ISchedule getSchedule() {
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
        return maxClubs;
    }

    @Override
    public int getMaxRounds() {
        return 0 ;
    }

    @Override
    public int getCurrentMatches() {
        return 0;
    }

    @Override
    public int getNumberOfCurrentTeams() {
        return numClubs;
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
}