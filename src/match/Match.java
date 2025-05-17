package match;

import com.ppstudios.footballmanager.api.contracts.event.IEvent;
import com.ppstudios.footballmanager.api.contracts.event.IEventManager;
import com.ppstudios.footballmanager.api.contracts.match.IMatch;
import com.ppstudios.footballmanager.api.contracts.team.IClub;
import com.ppstudios.footballmanager.api.contracts.team.ITeam;
import event.EventManager;

import java.io.IOException;
import java.util.Objects;

public class Match implements IMatch {

    private static final int INIT_CAP = 10;
    private static final int INCREMENT = 2;


    private IClub homeClub;
    private IClub awayClub;
    private ITeam homeTeam;
    private ITeam awayTeam;
    private boolean pleayed;
    private int round;
    private IEventManager events;
    private int homeGoals;
    private int awayGoals;

    public Match(IClub homeClub, IClub awayClub, int round) {
        this.pleayed = false;
        this.homeClub = homeClub;
        this.awayClub = awayClub;
        this.round = round;
        this.events = new EventManager();
        this.homeGoals = 0;
        this.awayGoals = 0;
    }

    private void setHomeTeam(ITeam homeTeam) {
        this.homeTeam = homeTeam;
    }

    private void setAwayTeam(ITeam awayTeam) {
        this.awayTeam = awayTeam;
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
     *
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
     *
     * @return a equipa vencedora ou null em caso de empate
     */
    @Override
    public ITeam getWinner() {
        if (homeGoals > awayGoals) {
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
        if (iTeam == null) {
            throw new NullPointerException("Team cannot be null.");
        }

        if (isPlayed()) {
            throw new IllegalStateException("Match is already played.");
        }

        if (iTeam.getClub().equals(homeClub)) {
            setHomeTeam(iTeam);
        } else if (iTeam.getClub().equals(awayClub)) {
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
        if (isInEvent(iEvent)) {
            throw new IllegalStateException("Evento já existe no jogo.");
        }
        this.events.addEvent(iEvent);

    }


    private boolean isInEvent(IEvent event) {
        for (IEvent iEvent : this.events.getEvents()) {
            if (iEvent.equals(event)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public IEvent[] getEvents() {
        IEvent[] copia = new IEvent[this.events.getEventCount()];
        for (int i = 0; i < this.events.getEventCount(); i++) {
            copia[i] = this.events.getEvents()[i];
        }
        return copia;
    }

    @Override
    public int getEventCount() {
        return this.events.getEventCount();
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


    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Match)) {
            return false;
        }
        Match match = (Match) o;
        return this.pleayed == match.pleayed &&
                this.round == match.round &&
                this.homeGoals == match.homeGoals &&
                this.awayGoals == match.awayGoals &&
                this.homeClub.equals(match.getHomeClub()) &&
                this.awayClub.equals(match.getAwayClub()) &&
                this.homeTeam.equals(match.homeTeam) &&
                this.awayTeam.equals(match.awayTeam) &&
                this.events.equals(match.events);
    }

}