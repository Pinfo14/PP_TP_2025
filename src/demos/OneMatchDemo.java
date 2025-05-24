package demos;

import com.ppstudios.footballmanager.api.contracts.event.IEvent;
import com.ppstudios.footballmanager.api.contracts.league.ILeague;
import com.ppstudios.footballmanager.api.contracts.match.IMatch;
import com.ppstudios.footballmanager.api.contracts.team.IClub;
import com.ppstudios.footballmanager.api.contracts.team.ITeam;
import imports.Imports;
import league.League;
import league.Season;
import simulation.GenerateTeams;
import simulation.MatchSimulator;

public class OneMatchDemo {
    public static void main(String[] args) {
        Imports importClubs = new Imports();
        IClub[] clubs = importClubs.importPlayersToClub();

        ILeague league = new League("Liga Portugal");

        Season season = new Season(league.getName(), 2023);
        for (IClub c : clubs) {
            try {
                season.addClub(c);
            } catch (Exception e) {
                System.out.println("Erro ao adicionar " + c.getName() + ": " + e.getMessage());
            }
        }


        try {
            season.generateSchedule();
        } catch (Exception e) {
            System.out.println("Não foi possível gerar fixtures: " + e.getMessage());
            return;
        }
        MatchSimulator simulador = new MatchSimulator();
        IMatch[] matches = season.getMatches();

        GenerateTeams generateTeams = new GenerateTeams();

        IClub casaClub = matches[0].getHomeClub();
        IClub foraClub = matches[0].getAwayClub();

        // Gera 11 iniciais aleatórios para cada lado
        ITeam casaLineup = generateTeams.randomTeam(casaClub);
        ITeam foraLineup = generateTeams.randomTeam(foraClub);

        // Atribui as equipas ao próprio objeto de partida
        matches[0].setTeam(casaLineup);
        matches[0].setTeam(foraLineup);

        System.out.println("=== Eventos dos Jogos Simulados ===");

        System.out.println("Jogo: " + matches[0].getHomeTeam().getClub().getName()+" vs "+ matches[0].getAwayTeam().getClub().getName()
        );

        simulador.simulate(matches[0]);
        if (matches[0].getEvents().length == 0) {
            System.out.println("  (sem eventos)");
        } else {
            for (IEvent ev : matches[0].getEvents()) {
                System.out.println("Evento: " + ev);
            }
        }
        System.out.println("RESULTADO: "+simulador.getHomeGoals() + " : " + simulador.getAwayGoals());

        System.out.println("Vencedor: " + matches[0].getWinner());
        System.out.println("-----------------------------------");
    }

    }

