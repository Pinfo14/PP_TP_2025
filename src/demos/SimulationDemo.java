package demos;

import com.ppstudios.footballmanager.api.contracts.event.IEvent;
import com.ppstudios.footballmanager.api.contracts.league.ISeason;
import com.ppstudios.footballmanager.api.contracts.league.IStanding;
import com.ppstudios.footballmanager.api.contracts.match.IMatch;
import com.ppstudios.footballmanager.api.contracts.player.IPlayer;
import com.ppstudios.footballmanager.api.contracts.team.IClub;
import com.ppstudios.footballmanager.api.contracts.team.IPlayerSelector;
import com.ppstudios.footballmanager.api.contracts.team.ITeam;
import imports.Imports;
import league.League;
import league.Season;
import simulation.GenerateTeams;
import simulation.MatchSimulator;


public class SimulationDemo {

    public static void main(String[] args) {
        // 1) Importa clubes e jogadores
     /*   Imports importClubs = new Imports();
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
*/
        Imports importClubs = new Imports();
        IClub[] clubes = importClubs.importPlayersToClub();

// criar uma classe para gerar aleatoriamente todas as equipas passando a formacao como argomento talve
// apenas cria team aleatoria pra todos os clubes menos para aquele que o user decidiu dar coach no caso dele ele decide gerar aleatoriamente uma team(11 inicial) ou criala ele msm


        // 3) Cria liga e temporada e adiciona clubes à temporada
        League liga = new League("Liga Portugal");
        Season temporada2023 = new Season("Liga Portugal", 2023);
        try {
            liga.createSeason(temporada2023);
        } catch (Exception e) {
            System.out.println("Erro ao criar temporada: " + e.getMessage());
            return;
        }

        ISeason season = liga.getSeason(2023);
        for (IClub c : clubes) {
            try {
                season.addClub(c);
            } catch (Exception e) {
                System.out.println("Erro ao adicionar " + c.getName() + ": " + e.getMessage());
            }
        }

        // 4) Gera calendário (round-robin)
        try {
            season.generateSchedule();
        } catch (Exception e) {
            System.out.println("Não foi possível gerar fixtures: " + e.getMessage());
            return;
        }

        // 5) Simula cada partida e imprime eventos
        MatchSimulator simulador = new MatchSimulator();
        IMatch[] partidas = season.getMatches();


        GenerateTeams generateTeams = new GenerateTeams();

        for (IMatch partida : partidas) {
            // Obtém os clubes donos da casa e visitantes
            IClub casaClub = partida.getHomeClub();
            IClub foraClub = partida.getAwayClub();

            // Gera 11 iniciais aleatórios para cada lado
            ITeam casaLineup = generateTeams.randomTeam(casaClub);
            ITeam foraLineup = generateTeams.randomTeam(foraClub);

            // Atribui as equipas ao próprio objeto de partida
            partida.setTeam(casaLineup);
            partida.setTeam(foraLineup);
        }

// Agora simula e imprime eventos
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
                    System.out.println("Evento: " + ev);
                }
            }
            System.out.println("Vencedor: " + partida.getWinner());
            System.out.println("-----------------------------------");
        }

      for (IStanding standing: season.getLeagueStandings() ) {
          System.out.println(standing.toString());
      }

    }
}
