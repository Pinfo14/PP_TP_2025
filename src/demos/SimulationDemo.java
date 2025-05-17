package demos;

import com.ppstudios.footballmanager.api.contracts.event.IEvent;
import com.ppstudios.footballmanager.api.contracts.league.ISeason;
import com.ppstudios.footballmanager.api.contracts.match.IMatch;
import com.ppstudios.footballmanager.api.contracts.player.IPlayer;
import com.ppstudios.footballmanager.api.contracts.team.IClub;
import com.ppstudios.footballmanager.api.contracts.team.IPlayerSelector;
import com.ppstudios.footballmanager.api.contracts.team.ITeam;
import imports.Imports;
import league.League;
import league.Season;
import player.PlayerPositionManage;
import simulation.MatchSimulator;
import team.Club;
import team.Formation;
import team.RandomPlayerSelector;
import team.Team;

public class SimulationDemo {

    public static void main(String[] args) {
        // 1) Importa clubes e jogadores
        Imports importClubs = new Imports();
        Club[] clubes = importClubs.importClubs();  // ex.: [Benfica, Porto, …]

        IPlayer[] benficaPlayers = importClubs.importPlayers("Benfica.json");
        IPlayer[] portoPlayers = importClubs.importPlayers("Porto.json");

        // 2) Adiciona jogadores aos clubes correspondentes
        //    assumindo que clube[0] é Benfica e clube[1] é Porto
        for (IPlayer p : benficaPlayers) {
            try {
                clubes[0].addPlayer(p);
            } catch (Exception e) {
                System.out.println("Benfica: " + e.getMessage());
            }
        }
        for (IPlayer p : portoPlayers) {
            try {
                clubes[1].addPlayer(p);
            } catch (Exception e) {
                System.out.println("Porto: " + e.getMessage());
            }
        }


        IPlayer[] porto11 = new IPlayer[11];
        int portoIdx = 0;
        ITeam teamPorto;
        ITeam teamBenfica;
        int benficaIdx = 0;
        IPlayer[] benfica11 = new IPlayer[11];

        PlayerPositionManage positionManager = new PlayerPositionManage();
        IPlayerSelector playerSelector = new RandomPlayerSelector();

        Formation formation433 = new Formation("4-3-3", 4, 3, 3, 0);
        Formation formation442 = new Formation("4-4-2", 4, 4, 2, 0);
        while ( portoIdx < 11) {
            for (int def = 0; def < formation433.getNumDefenders(); def++) {
                IPlayer  player = playerSelector.selectPlayer(clubes[1], positionManager.getPositionByDescription("Defender"));
                while (veryfiPlayerInTeam(porto11, player)){
                    player = playerSelector.selectPlayer(clubes[1], positionManager.getPositionByDescription("Defender"));
                }
                porto11[portoIdx++] = player;
            }

        for (int mid = 0; mid < formation433.getNumMidfielders(); mid++) {
            IPlayer player = playerSelector.selectPlayer(clubes[1], positionManager.getPositionByDescription("Midfielder"));;
             while (veryfiPlayerInTeam(porto11, player)){
                 player = playerSelector.selectPlayer(clubes[1], positionManager.getPositionByDescription("Midfielder"));
             }
            porto11[portoIdx++] = player;
        }
        for (int att = 0; att < formation433.getNumAttackers(); att++) {
            IPlayer player = playerSelector.selectPlayer(clubes[1], positionManager.getPositionByDescription("Forward"));
            while (veryfiPlayerInTeam(porto11, player)){
                player = playerSelector.selectPlayer(clubes[1], positionManager.getPositionByDescription("Forward"));
            }
            porto11[portoIdx++] = player;
        }
        porto11[portoIdx++] = playerSelector.selectPlayer(clubes[1], positionManager.getPositionByDescription("Goalkeeper"));
    }

            teamPorto = new Team(clubes[1]);
            teamPorto.setFormation(formation433);
            for (IPlayer p : porto11) {
                teamPorto.addPlayer(p);
            }



        while (benficaIdx < 11){
            for (int def = 0; def < formation442.getNumDefenders(); def++) {
                IPlayer  player = playerSelector.selectPlayer(clubes[0], positionManager.getPositionByDescription("Defender"));
                while (veryfiPlayerInTeam(benfica11, player)){
                    player =   playerSelector.selectPlayer(clubes[0], positionManager.getPositionByDescription("Defender"));
                }
                benfica11[benficaIdx++] = player;
            }
            for (int mid = 0; mid < formation442.getNumMidfielders(); mid++) {

                IPlayer  player = playerSelector.selectPlayer(clubes[0], positionManager.getPositionByDescription("Midfielder"));
                while (veryfiPlayerInTeam(benfica11, player)){
                    player =  playerSelector.selectPlayer(clubes[0], positionManager.getPositionByDescription("Midfielder"));
                }
                benfica11[benficaIdx++] = player;

            }
            for (int att = 0; att < formation442.getNumAttackers(); att++) {
                IPlayer  player = playerSelector.selectPlayer(clubes[0], positionManager.getPositionByDescription("Forward"));
                while (veryfiPlayerInTeam(benfica11, player)){
                    player =  playerSelector.selectPlayer(clubes[0], positionManager.getPositionByDescription("Forward"));
                }
                benfica11[benficaIdx++] = player;

            }
            benfica11[benficaIdx++] = playerSelector.selectPlayer(clubes[0],positionManager.getPositionByDescription("Goalkeeper"));
        }


            teamBenfica = new Team(clubes[0]);
            teamBenfica.setFormation(formation442);
            for (IPlayer p : benfica11) {
                teamBenfica.addPlayer(p);
            }


        // 3) Cria liga e temporada e adiciona clubes à temporada
        League liga = new League("Liga Portugal");
        Season temporada2023 = new Season("Liga Portugal", 2023);
        try { liga.createSeason(temporada2023); }
        catch (Exception e) { System.out.println("Erro ao criar temporada: " + e.getMessage()); return; }

        ISeason season = liga.getSeason(2023);
        for (IClub c : clubes) {
            try { season.addClub(c); }
            catch (Exception e) { System.out.println("Erro ao adicionar " + c.getName() + ": " + e.getMessage()); }
        }

        // 4) Gera calendário (round-robin)
        try { season.generateSchedule(); }
        catch (Exception e) { System.out.println("Não foi possível gerar fixtures: " + e.getMessage()); return; }

        // 5) Simula cada partida e imprime eventos
        MatchSimulator simulador = new MatchSimulator();
        IMatch[] partidas = season.getMatches();
        for (IMatch partida : partidas) {
            partida.setTeam(teamPorto);
            partida.setTeam(teamBenfica);
        }

        System.out.println("=== Eventos dos Jogos Simulados ===");
        for (IMatch partida : partidas) {
            System.out.printf("Jogo: %s x %s%n",
                    partida.getHomeTeam().getClub().getName(),
                    partida.getAwayTeam().getClub().getName()
            );

            simulador.simulate(partida);


            if (partida.getEvents().length == 0) {
                System.out.println("  (sem eventos)");
            } else {
                for (IEvent ev : partida.getEvents()) {
                    System.out.println("Evento: "+
                            ev.toString()
                    );
                }
            }
            System.out.println(partida.getWinner());

            System.out.println("-----------------------------------");
        }
    }

    /**
     * Verifica se o jogador já está no array do 11.
     */
    private static boolean veryfiPlayerInTeam(IPlayer[] team, IPlayer player) {
        for (int i = 0; i < team.length; i++) {
            if (team[i] != null && team[i].equals(player)) {
                return true;
            }
        }
        return false;
    }

}
