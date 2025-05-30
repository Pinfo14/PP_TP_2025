package league;
import com.ppstudios.footballmanager.api.contracts.league.ISchedule;
import com.ppstudios.footballmanager.api.contracts.league.ISeason;
import com.ppstudios.footballmanager.api.contracts.league.IStanding;
import com.ppstudios.footballmanager.api.contracts.match.IMatch;
import com.ppstudios.footballmanager.api.contracts.simulation.MatchSimulatorStrategy;
import com.ppstudios.footballmanager.api.contracts.team.IClub;
import com.ppstudios.footballmanager.api.contracts.team.ITeam;
import match.Match;
import menus.ListStanding;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import simulation.GenerateTeams;
import simulation.MatchSimulator;
import team.Club;

import java.io.File;
import java.io.FileWriter;

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

    private static final String FOLGA = "FOLGA";

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
    private MatchSimulator matchSimulator;

    public Season(String leagueName, int year, int coachingClubIndex, int currentRound,
                  int pointsPerWin, int pointsPerDraw, int pointsPerLoss) {
        this.maxClubs = 18;
        this.name = leagueName;
        this.year = year;
        this.clubs = new Club[maxClubs];
        this.standings = new IStanding[maxClubs];
        this.numClubs = 0;
        this.pointsPerLoss = pointsPerLoss;
        this.pointsPerWin = pointsPerWin;
        this.pointsPerDraw = pointsPerDraw;
        this.currentRound = currentRound;
        this.coachingClubIndex = coachingClubIndex;
    }

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

    public void setSchedule( Schedule schedule){
        this.schedule=schedule;
    }

    private boolean verifyMactchPlayed(){
        if (schedule == null) {
            return false; // Nenhuma partida foi jogada se o calendário nem existe
        }

        IMatch[] matches = schedule.getAllMatches();

        for(IMatch match : matches){
            if (match != null && match.isPlayed()){
                    return true;
                }

        }

        return false;
    }

    @Override
    public boolean addClub(IClub iClub) {
        if(verifyMactchPlayed()) {
            throw new IllegalArgumentException("Não pode adicionar um clube após a primeira jornada");
        }

        if (iClub == null) {
            throw new IllegalArgumentException("Club cannot be null.");
        }

        if (clubExist(iClub)) {
            throw new IllegalArgumentException("Clube já existe");
        }

        if (numClubs == maxClubs) {
            throw new IllegalStateException("Liga está cheia.");
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
        if(verifyMactchPlayed()) {
            throw new IllegalArgumentException("Não pode remover um clube após a primeira jornada");
        }

        if (iClub == null) {
            throw new IllegalArgumentException("Club cannot be null.");
        }

        int index = clubIndex(iClub);

        if (index == -1) {
            throw new IllegalStateException("Clube não existe na liga.");
        }

        if (index == coachingClubIndex) {
            coachingClubIndex = -1;
        }

        for (int i = index; i < numClubs - 1; i++) {
            clubs[i] = clubs[i + 1];
            standings[i] = standings[i + 1];
        }

        clubs[--numClubs] = null;
        standings[numClubs] = null;
        generateSchedule();
        currentRound = 1;

        return true;
    }

    @Override
    public void generateSchedule() {

        schedule = new Schedule(clubs, numClubs);

    }

    private int calculateNumberOfMatches() {
        int totalClubs = numClubs;
        if (totalClubs % 2 != 0) {
            totalClubs++;
        }
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

        if (i < 0 || i > schedule.getNumberOfRounds()) {
            throw new IllegalArgumentException("Jornada Inexistente");
        }

        IMatch[] scheduledMatches = new IMatch[calculateNumberOfMatches()];

        try {
            scheduledMatches = schedule.getMatchesForRound(i);
        } catch (IllegalArgumentException | IllegalStateException e) {
           System.out.println(e.getMessage());
        }

        return scheduledMatches;

    }

    @Override
    public void simulateRound() {
        IMatch[] scheduledMatches = getMatches(currentRound);
        MatchSimulator simulator = new MatchSimulator();
        GenerateTeams generateTeams = new GenerateTeams();

        if (scheduledMatches.length > 1) {
            System.out.println("\nResultados da jornada:");
        }

        for (IMatch match : scheduledMatches) {
            if (!shouldSimulate(match)) {
                continue;
            }

            simulateMatch(match, simulator, generateTeams);
        }

        roundCompleted();
    }

    private boolean shouldSimulate(IMatch match) {

        if (match == null || match.isPlayed()) {
            return false;
        }

        if (match.getHomeClub() == null || match.getAwayClub() == null) {
            return false;
        }

        if (match.getHomeClub().getName().equals(FOLGA) || match.getAwayClub().getName().equals(FOLGA)) {
            System.out.println(FOLGA);
            return false;
        }

        return true;
    }

    private void simulateMatch(IMatch match, MatchSimulator simulator, GenerateTeams generator) {
        ITeam homeLineup = generator.randomTeam(match.getHomeClub());
        ITeam awayLineup = generator.randomTeam(match.getAwayClub());

        match.setTeam(homeLineup);
        match.setTeam(awayLineup);

        simulator.simulate(match);
        match.setPlayed();

        printResults(match);

        updateStandings(match);
    }

    private void printResults(IMatch match) {
        if (!(match instanceof Match)) {
            System.out.println("Resultados da jornada:");
        }

        Match m = (Match) match;

        StringBuilder sb = new StringBuilder();
        sb.append("Jornada ").append(m.getRound()).append(" - ");
        sb.append(m.getHomeClub().getName()).append(" (").append(m.getHomeGoals()).append(") vs ");
        sb.append("(").append(m.getAwayGoals()).append(") ").append(m.getAwayClub().getName());

        System.out.println(sb.toString());


    }

    public void updateStandings(IMatch iMatch) {
        int homeIndex = clubIndex(iMatch.getHomeClub());
        int awayIndex = clubIndex(iMatch.getAwayClub());

        if (!(iMatch instanceof Match)) {
            System.out.println("\nResultados da jornada:");
        }

        Match m = (Match) iMatch;

        Standing standingHome = (Standing) standings[homeIndex];
        Standing standingAway = (Standing) standings[awayIndex];

        int homeGoals = m.getHomeGoals();
        int awayGoals = m.getAwayGoals();
        ITeam winner = m.getWinner();

        if (winner != null) {
            if (winner.equals(m.getHomeTeam())) {
                standingHome.addWinResult(homeGoals, awayGoals, pointsPerWin);
                standingAway.addLossResult(awayGoals, homeGoals, pointsPerLoss);
            } else if (winner.equals(m.getAwayTeam())) {
                standingAway.addWinResult(awayGoals, homeGoals, pointsPerWin);
                standingHome.addLossResult(homeGoals, awayGoals, pointsPerLoss);
            }
        } else {
            standingHome.addDrawResult(homeGoals, awayGoals, pointsPerDraw);
            standingAway.addDrawResult(awayGoals, homeGoals, pointsPerDraw);
        }
    }

    private void roundCompleted() {
        IMatch[] matches = getMatches(getCurrentRound());

        for (IMatch m : matches) {
            if (m == null || m.getHomeClub() == null || m.getAwayClub() == null) {
                continue;
            }

            String homeName = m.getHomeClub().getName();
            String awayName = m.getAwayClub().getName();

            if (homeName.equals(FOLGA) || awayName.equals(FOLGA)) {
                continue;
            }

            if (!m.isPlayed()) {
                return;
            }
        }

        currentRound++;

        getChampion();
    }

    @Override
    public void simulateSeason() {
        int totRounds = schedule.getNumberOfRounds();
        for (int i = currentRound; i <= totRounds; i++) {
            System.out.println("\n - JORNADA " + i);
            simulateRound();
        }

    }

    public void getChampion() {
        if(currentRound - 1  == schedule.getNumberOfRounds()) {
            ListStanding.listFinalStanding(standings,getName());
        }
    }

    @Override
    public int getCurrentRound() {
        return currentRound;
    }

    @Override
    public boolean isSeasonComplete() {
        boolean isPLayed = false;
        for (IMatch match : this.schedule.getAllMatches()) {
            isPLayed = match.isPlayed();
        }
        return isPLayed;
    }

    @Override
    public void resetSeason() {

        generateSchedule();
        currentRound = 1;
        cleanstanding();

    }

    private void cleanstanding() {
        Standing standingTemp;
        for(IStanding standing : standings) {
            if(standing != null) {
                standingTemp = (Standing) standing;
                standingTemp.reset();
            }

        }
    }

    /**
     * Exibe o resultado de uma partida, mostrando o nome do clube vencedor.
     *
     * @param iMatch partida da qual o resultado será gerado.
     * @return uma string representando o nome do clube vencedor da partida.
     */
    @Override
    public String displayMatchResult(IMatch iMatch) {
        StringBuilder sb = new StringBuilder();
        sb.append("Match Result: ");
        if (iMatch.getWinner()!=null) {
            sb.append(iMatch.getWinner().getClub().getName());
        }else {
            sb.append("Empate");
        }
        return sb.toString();
    }

    /**
     * Define a estratégia de simulação para a temporada.
     *
     * @param matchSimulatorStrategy a instância da estratégia de simulação de partidas a ser utilizada.
     */
    @Override
    public void setMatchSimulator(MatchSimulatorStrategy matchSimulatorStrategy) {
        this.matchSimulator = matchSimulator;
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
        return this.schedule.getNumberOfRounds();
    }

    @Override
    public int getCurrentMatches() {
        return calculateNumberOfMatches();
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

        String fileName = "src/Files/Season.json";
        File file = new File(fileName);
        file.createNewFile();

        FileWriter fileWriter = new FileWriter(file);
        try {
            fileWriter.write(getSeasonJson().toJSONString());
            System.out.println("Season exportado com sucesso para: " + fileName);
        } catch (IOException e) {
            System.out.println("Erro ao exportar o season para o arquivo: " + fileName);
        } finally {
            fileWriter.close();
        }
    }

    private JSONArray getStandingsJson() {
        JSONArray jsonArray = new JSONArray();

        for (int i = 0; i < this.standings.length; i++) {
            if (this.standings[i] != null) {
                jsonArray.add(((Standing) this.standings[i]).getJsonObj());
            }
        }
        return jsonArray;
    }

    public JSONObject getSeasonJson() {
        JSONObject seasonJson = new JSONObject();
        seasonJson.put("name", this.name);
        seasonJson.put("year", this.year);
        seasonJson.put("numberOfTeams", this.numClubs);
        seasonJson.put("coachingClubIndex", this.coachingClubIndex);
        seasonJson.put("pointsPerWin", this.pointsPerWin);
        seasonJson.put("pointsPerDraw", this.pointsPerDraw);
        seasonJson.put("pointsPerLoss", this.pointsPerLoss);
        seasonJson.put("currentRound", this.currentRound);
        seasonJson.put("standings", this.getStandingsJson());
        seasonJson.put("schedule", ((Schedule) this.schedule).getJsonSchedule());
        return seasonJson;
    }

    private boolean clubExist(IClub iclub) {
        for (int i = 0; i < numClubs; i++) {
            if (clubs[i].equals(iclub)) {
                return true;
            }
        }

        return false;
    }

    private int clubIndex(IClub iclub) {
        for (int i = 0; i < numClubs; i++) {
            if (clubs[i].equals(iclub)) {
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

        return this.year == other.year &&
                this.maxClubs == other.maxClubs &&
                this.numClubs == other.numClubs &&
                this.pointsPerWin == other.pointsPerWin &&
                this.pointsPerDraw == other.pointsPerDraw &&
                this.pointsPerLoss == other.pointsPerLoss &&
                this.name.equals(other.name);
    }
}
