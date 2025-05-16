package match;

import com.ppstudios.footballmanager.api.contracts.event.IEvent;
import com.ppstudios.footballmanager.api.contracts.match.IMatch;
import com.ppstudios.footballmanager.api.contracts.team.IClub;
import com.ppstudios.footballmanager.api.contracts.team.ITeam;

import java.io.IOException;

public class Match implements IMatch {

    private static final int INIT_CAP = 10;
    private static final int INCREMENT = 2;


    private IClub homeClub;
    private IClub awayClub;
    private ITeam homeTeam;
    private ITeam awayTeam;
    private boolean pleayed;
    private int round;
    private IEvent[] events;
    private int eventCount;
    private int homeGoals;
    private int awayGoals;
    private boolean coaching;

    public Match(IClub homeClub, IClub awayClub, int round) {
        this.pleayed = false;
        this.coaching = false;
        this.homeClub = homeClub;
        this.awayClub = awayClub;
        this.round = round;
        this.events = new IEvent[INIT_CAP];
        this.eventCount = 0;
        this.homeGoals = 0;
        this.awayGoals = 0;
    }

    private void setHomeTeam(ITeam homeTeam) {
        this.homeTeam = homeTeam;
    }

    private void setAwayTeam(ITeam awayTeam) {
        this.awayTeam = awayTeam;
    }

    public void setCoaching() {
        this.coaching = true;
    }

    public boolean isCoaching() {
        return this.coaching;
    }

    @Override
    public IClub getHomeClub() {
        return homeClub;
    }

    @Override
    public IClub getAwayClub() {
        return awayClub;
    }

    @Override
    public boolean isPlayed() {
        return false;
    }

    @Override
    public ITeam getHomeTeam() {
        return this.homeTeam;
    }

    @Override
    public ITeam getAwayTeam() {
        return awayTeam;
    }

    @Override
    public void setPlayed() {
        pleayed = true;
    }

    @Override
    public int getTotalByEvent(Class aClass, IClub iClub) {
        return 0;
    }

    /**
     * Verifica se as equipas e as teams são nulas e se existe uma equipa com o nome FOLGA
     * Verifica se a cada equipa corresponde a cada club.
     * Caso se verifique alguma destas situações retorna false, caso contrario retorna true
     * @return true se a partida for valida e false caso não seja.
     */
    @Override
    public boolean isValid() {
        if (homeClub == null || awayClub == null || homeTeam == null || awayTeam == null) {
            return false;
        }
        if ("FOLGA".equals(homeClub.getName()) || "FOLGA".equals(awayClub.getName())) {
            return false;
        }
        if (homeClub.equals(awayClub) || homeTeam.equals(awayTeam)) {
            return false;
        }
        return true;
    }

    /**
     * Devolve a equipa Team vencedora do jogo.
     * @return a equipa vencedora ou null em caso de empate
     */
    @Override
    public ITeam getWinner() {
        if(homeGoals > awayGoals) {
            return homeTeam;
        } else if (awayGoals > homeGoals) {
            return awayTeam;
        } else {
            return null;
        }
    }

    @Override
    public int getRound() {
        return round;
    }

    @Override
    public void setTeam(ITeam iTeam) {
        if(iTeam == null) {
            throw new NullPointerException("Team cannot be null.");
        }

        if(isPlayed()) {
            throw new IllegalStateException("Match is already played.");
        }

        if(iTeam.getClub().equals(homeClub)) {
            setHomeTeam(iTeam);
        } else if(iTeam.getClub().equals(awayClub)) {
            setAwayTeam(iTeam);
        } else {
            throw new IllegalStateException("Team does not belong to the club.");
        }
    }

    @Override
    public void exportToJson() throws IOException {

    }

    @Override
    public void addEvent(IEvent iEvent) {
        if (iEvent == null) {
            throw new IllegalArgumentException("Evento não pode ser nulo.");
        }
        if (isInEvent(iEvent)){
            throw new IllegalStateException("Evento já existe no jogo.");
        }

        if (this.eventCount == this.events.length) {
            expandCapacity();
        }

        this.events[this.eventCount++] = iEvent;
    }

    private void expandCapacity() {
        IEvent[] temp = new IEvent[this.events.length * INCREMENT];
        for (int i = 0; i < this.eventCount; i++) {
            temp[i] = this.events[i];
        }
        this.events = temp;
    }

    private boolean isInEvent(IEvent event) {
        for(int i = 0; i < this.eventCount; i++) {
            if(this.events[i].equals(event)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public IEvent[] getEvents() {
        IEvent[] copia = new IEvent[this.eventCount];
        for (int i = 0; i < this.eventCount; i++) {
            copia[i] = this.events[i];
        }
        return copia;
    }

    @Override
    public int getEventCount() {
        return this.eventCount;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(homeClub.getName());
        sb.append(" vs ");
        sb.append(awayClub.getName());
        sb.append(" valida? :").append(isValid());

        return sb.toString();

    }

    //TODO FAZER O EQUALS
}