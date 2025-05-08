package match;

import com.ppstudios.footballmanager.api.contracts.event.IEvent;
import com.ppstudios.footballmanager.api.contracts.match.IMatch;
import com.ppstudios.footballmanager.api.contracts.team.IClub;
import com.ppstudios.footballmanager.api.contracts.team.ITeam;

import java.io.IOException;

public class Match implements IMatch {

    private IClub homeClub;
    private IClub awayClub;
    private ITeam homeTeam;
    private ITeam awayTeam;
    private boolean pleayed;

    public Match(IClub homeClub, IClub awayClub) {
        this.pleayed = false;
        this.homeClub = homeClub;
        this.awayClub = awayClub;
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
        return homeTeam;
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

    @Override
    public boolean isValid() {
        return false;
    }

    @Override
    public ITeam getWinner() {
        return null;
    }

    @Override
    public int getRound() {
        return 0;
    }

    @Override
    public void setTeam(ITeam iTeam) {

    }

    @Override
    public void exportToJson() throws IOException {

    }

    @Override
    public void addEvent(IEvent iEvent) {

    }

    @Override
    public IEvent[] getEvents() {
        return new IEvent[0];
    }

    @Override
    public int getEventCount() {
        return 0;
    }
}
