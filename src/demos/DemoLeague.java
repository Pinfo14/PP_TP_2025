package demos;

import com.ppstudios.footballmanager.api.contracts.league.ISeason;
import com.ppstudios.footballmanager.api.contracts.league.IStanding;
import com.ppstudios.footballmanager.api.contracts.match.IMatch;
import com.ppstudios.footballmanager.api.contracts.team.IClub;
import league.League;
import league.Season;
import team.Club;

public class DemoLeague {

    public static void main(String[] args) {


        League liga = new League("Liga Portugal");

        Season season1 = new Season("Liga Portugal", 2023);

        Club club1 = new Club("Porto");
        Club club2 = new Club("Boavista");
        Club club3 = new Club("AVS");
        Club club4 = new Club("Moreirense");
        Club club5 = new Club("Vitoria SC");
        Club club6 = new Club("Braga");

        try{
            liga.createSeason(season1);
        } catch (Exception e){
            System.out.println(e.getMessage());
        }

        ISeason season = liga.getSeason(2023);

        IClub[] clubs = {club1, club2, club3, club4, club5, club6};

        for(IClub c : clubs) {
            try {
                season.addClub(c);
            }catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }


        try {
            IStanding[] standing = season.getLeagueStandings();

            if (standing == null || standing.length == 0) {
                System.out.println("Nenhuma classficacao encontrada.");
            } else {
                for (IStanding  classificacao : standing) {
                    if(classificacao != null) {
                        System.out.println(classificacao.toString());
                    }
                    // aqui dificilmente throwará IllegalArgumentException / IllegalStateException,
                    // então não precisa de try por partida, a menos que match.toString()
                    // também possa lançar algo.

                }
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Round é inválido: " + e.getMessage());
        } catch (IllegalStateException e) {
            System.out.println("Não há partidas: season não inicializada ou rodada não foi definida.");
        }

        //;
        try {
            IMatch[] matches = season.getMatches();

            if (matches == null || matches.length == 0) {
                System.out.println("Nenhuma partida encontrada para essa rodada.");
            } else {
                for (IMatch match : matches) {
                    // aqui dificilmente throwará IllegalArgumentException / IllegalStateException,
                    // então não precisa de try por partida, a menos que match.toString()
                    // também possa lançar algo.
                    System.out.println(match);
                }
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Round é inválido: " + e.getMessage());
        } catch (IllegalStateException e) {
            System.out.println("Não há partidas: season não inicializada ou rodada não foi definida.");
        }


    }
}


