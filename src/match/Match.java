package match;

import com.ppstudios.footballmanager.api.contracts.event.IEvent;
import com.ppstudios.footballmanager.api.contracts.event.IEventManager;
import com.ppstudios.footballmanager.api.contracts.match.IMatch;
import com.ppstudios.footballmanager.api.contracts.player.IPlayer;
import com.ppstudios.footballmanager.api.contracts.team.IClub;
import com.ppstudios.footballmanager.api.contracts.team.ITeam;
import event.*;
import org.json.simple.JSONObject;
import team.Team;

import java.io.File;
import java.io.FileWriter;
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
        return this.pleayed;
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
            calculateGoalsFromEvents();
    }


    public void resetMatch(boolean reset) {
        if (reset){
            this.pleayed = false;
        }
    }

    private void calculateGoalsFromEvents() {

        IEvent[] allEvents = this.events.getEvents();

        if (allEvents == null) {
            this.homeGoals = 0;
            this.awayGoals = 0;
            return;
        }

        this.homeGoals = getTotalByEvent(GoalEvent.class, homeClub);
        this.awayGoals = getTotalByEvent(GoalEvent.class, awayClub);
    }


    @Override
    public int getTotalByEvent(Class aClass, IClub iClub) {
        int count = 0;
        IEvent[] allEvents = this.events.getEvents();

        for (IEvent event : allEvents) {
            if (aClass.isInstance(event)) {
                // Para eventos de golo, verifica se o jogador pertence ao clube
                if (event instanceof GoalEvent) {
                    GoalEvent goalEvent = (GoalEvent) event;
                    if (isPlayerFromClub(goalEvent.getPlayer(), iClub)) {
                        count++;
                    }
                }
            }
        }

        return count;
    }
    /**
     * Verifica se um jogador pertence a um clube
     */
    private boolean isPlayerFromClub(com.ppstudios.footballmanager.api.contracts.player.IPlayer player, IClub club) {
        if (club == null || player == null) {
            return false;
        }

        return club.isPlayer(player);
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
            throw new IllegalStateException("Erro. Esta equipa está no jogo errado.");
        }
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

        return sb.toString();
    }

    @Override
    public void exportToJson() throws IOException {

        String fileName = "src/Files/saves/matches/Match_"+this.homeClub.getCode()+"VS"+this.awayClub.getCode()+".json";
        File file = new File(fileName);
        file.createNewFile();

        JSONObject object =  getMatchJson();
        FileWriter fileWriter = new FileWriter(file);
        try {
            fileWriter.write(object.toJSONString());
            System.out.println("Match exportado com sucesso para: " + fileName);
        }catch (IOException e){
            System.out.println("Erro ao exportar o Match para o arquivo: " + fileName);
        }finally {
            fileWriter.close();
        }
    }
    public JSONObject getMatchJson() {
        JSONObject object = new JSONObject();
        object.put("homeClub", this.homeClub.getCode());
        object.put("awayClub",this.awayClub.getCode());
        object.put("homeGoals", this.homeGoals);
        object.put("awayGoals", this.awayGoals);
        object.put("played", this.pleayed);
        // Teams (se existirem)
        if(this.homeTeam != null) {
            object.put("homeTeam", ((Team)this.homeTeam).getJsonObj());
        }
        if(this.awayTeam != null) {
            object.put("awayTeam", ((Team)this.awayTeam).getJsonObj());
        }

        // Events
        if(this.events != null) {
            object.put("events", ((EventManager)this.events).getEventJson() );
        }

        return object;
    }

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