package league;

import com.ppstudios.footballmanager.api.contracts.league.ISchedule;
import com.ppstudios.footballmanager.api.contracts.match.IMatch;
import com.ppstudios.footballmanager.api.contracts.team.IClub;
import com.ppstudios.footballmanager.api.contracts.team.ITeam;
import match.Match;
import team.Club;

import java.io.IOException;



public class Schedule  implements ISchedule {

    private IMatch[] games;
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

    private int calculateRounds() {
        return (numberOfClubs - 1) * 2;
    }

    private int calculateMatchesPerRound() {
        return numberOfClubs / 2;
    }

    private void generateGames() {

        int totalClubs = numberOfClubs;
        if (totalClubs % 2 != 0) {
            clubs[totalClubs++] = new Club("FOLGA");
        }

        int n = totalClubs;
        int halfRounds = n - 1;
        this.numberOfRounds = halfRounds * 2;
        int matchesPerRound = n / 2;
        int totalMatches = numberOfRounds * matchesPerRound;

        games = new IMatch[totalMatches];

       // IClub[] rot = Arrays.copyOf(clubs, n); //verificar se se posso usar
        IClub[] rot = copyClubs(this.clubs,n);

        for (int r = 0; r < halfRounds; r++) {
            for (int j = 0; j < matchesPerRound; j++) {
                IClub home = rot[j];
                IClub away = rot[n - 1 - j];
                int idx    = r * matchesPerRound + j;
                games[idx] = new Match(home, away, (r + 1));
            }

            IClub last = rot[n - 1];
            System.arraycopy(rot, 1, rot, 2, n - 2);
            rot[1] = last;
        }

        for (int r = 0; r < halfRounds; r++) {
            for (int j = 0; j < matchesPerRound; j++) {

                int firstIdx  = r * matchesPerRound + j;
                IMatch m1      = games[firstIdx];

                int secondIdx = (r + halfRounds) * matchesPerRound + j;

                games[secondIdx] = new Match(m1.getAwayClub(), m1.getHomeClub(),halfRounds + r + 1);
            }
        }
    }


    @Override
    public IMatch[] getMatchesForRound(int i) {

        IMatch[] matches = new IMatch[calculateMatchesPerRound()];
        int idx = 0;

        for(IMatch match : games) {
            if(match.getRound() == i){
                matches[idx++] = match;
            }
        }

        return matches;
    }

    @Override
    public IMatch[] getMatchesForTeam(ITeam iTeam) {


        IMatch[] matches = new IMatch[calculateMatchesPerRound()];


        int idx = 0;
        for(IMatch match : games) {
            if(match.getRound() == idx){
                matches[idx++] = match;
            }
        }

        return matches;
    }

    @Override
    public int getNumberOfRounds() {
        return this.numberOfRounds;
    }

    @Override
    public IMatch[] getAllMatches() {
        IMatch[] matches = new IMatch[this.games.length];
        for(int i = 0; i < this.games.length; i++) {
            matches[i] = this.games[i];
        }
        return matches;
    }

    @Override
    public void setTeam(ITeam iTeam, int i) {

        //aqui percorre a jornada e guarda a equipa que vai jogar.

    }

    @Override
    public void exportToJson() throws IOException {

    }
    private IClub[] copyClubs(IClub[] clubs, int numberOfClubs) {
        IClub[] clubsTemp = new IClub[clubs.length];
        for(int i = 0; i < numberOfClubs; i++) {
            clubsTemp[i] = clubs[i];
        }
        return clubsTemp;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (int round = 1; round <= numberOfRounds; round++) {
            sb.append("Jornada ").append(round).append(":\n");

            for (IMatch jogo : games) {
                if(jogo.getRound() == round) {
                    sb.append(String.format("\t%s\n", jogo.toString()));
                }
            }
        }

        return sb.toString();
    }


}