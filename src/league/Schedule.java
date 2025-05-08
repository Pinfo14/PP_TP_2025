package league;

import com.ppstudios.footballmanager.api.contracts.league.ISchedule;
import com.ppstudios.footballmanager.api.contracts.match.IMatch;
import com.ppstudios.footballmanager.api.contracts.team.IClub;
import com.ppstudios.footballmanager.api.contracts.team.ITeam;
import match.Match;
import team.Club;

import java.io.IOException;



public class Schedule  implements ISchedule {

    private IMatch[][] games;
    private IClub[] clubs;
    private int numberOfClubs;
    private ITeam[] teams;
    private int numberOfRounds;

    public Schedule(IClub[] clubs, int numberOfClubs) {
        this.clubs = copyClubs(clubs, numberOfClubs);
        this.numberOfClubs = numberOfClubs;
        this.numberOfRounds = calculateRounds();
        generateGames();
    }



    @Override
    public IMatch[] getMatchesForRound(int i) {
        return new IMatch[0];
    }

    @Override
    public IMatch[] getMatchesForTeam(ITeam iTeam) {
        return new IMatch[0];
    }

    @Override
    public int getNumberOfRounds() {
        return 0;
    }

    @Override
    public IMatch[] getAllMatches() {
        return new IMatch[0];
    }

    @Override
    public void setTeam(ITeam iTeam, int i) {

        //aqui percorre a jornada e guarda a equipa que vai jogar.

    }

    @Override
    public void exportToJson() throws IOException {

    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int round = 0; round < numberOfRounds; round++) {
            sb.append("Jornada ").append(round + 1).append(":\n");
            for (IMatch jogo : games[round]) {
                String home = jogo.getHomeClub().getName();
                String away = jogo.getAwayClub().getName();
                // Opcional: se quiseres omitir a ronda de “Folga”:
                if ("FOLGA".equals(home) || "FOLGA".equals(away)) {
                    sb.append("  - ").append(
                            "Folga para " + ("FOLGA".equals(home) ? away : home)
                    ).append("\n");
                } else {
                    sb.append("  - ").append(home)
                            .append(" vs ")
                            .append(away)
                            .append("\n");
                }
            }
        }
        return sb.toString();
    }
    private IClub[] copyClubs(IClub[] clubs, int numberOfClubs) {
        IClub[] clubsTemp = new IClub[clubs.length];
        for(int i = 0; i < numberOfClubs; i++) {
            clubsTemp[i] = clubs[i];
        }
        return clubsTemp;
    }

    private int calculateRounds() {
        return (numberOfClubs - 1) * 2;
    }

    private void generateGames() {

        int totalClubs = numberOfClubs;

        if (totalClubs % 2 != 0) {
            clubs[totalClubs++] = new Club("FOLGA");
        }

        IClub clubsTemp[] = clubs;
        int roundsPerHalf = totalClubs - 1;
        this.numberOfRounds = roundsPerHalf * 2;  // ida + volta
        int matchesPerRound = totalClubs / 2;

        IMatch[][] fristTurn = new IMatch[roundsPerHalf][matchesPerRound];
        for (int round = 0; round < roundsPerHalf; round++) {
            for (int j = 0; j < matchesPerRound; j++) {
                fristTurn[round][j] = new Match(clubsTemp[j], clubsTemp[totalClubs - 1 - j]);
            }

            IClub temp = clubsTemp[totalClubs - 1];
            for (int k = totalClubs - 1; k > 1; k--) {
                clubsTemp[k] = clubsTemp[k - 1];
            }
            clubsTemp[1] = temp;
        }

        IMatch[][] secondTurn = new Match[roundsPerHalf][matchesPerRound];
        for (int round = 0; round < roundsPerHalf; round++) {
            for (int j = 0; j < matchesPerRound; j++) {
                IMatch game = fristTurn[round][j];
                secondTurn[round][j] = new Match(game.getAwayClub(), game.getHomeClub());
            }
        }

        games = new IMatch[numberOfRounds][matchesPerRound];

        for (int r = 0; r < roundsPerHalf; r++) {
            System.arraycopy(fristTurn[r],  0, games[r],0, matchesPerRound);
            System.arraycopy(secondTurn[r], 0, games[r + roundsPerHalf], 0, matchesPerRound);
        }
    }


}