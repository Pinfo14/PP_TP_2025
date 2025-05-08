package demos;

import com.ppstudios.footballmanager.api.contracts.league.ISeason;
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

        ISeason season =  liga.getSeason(2023);

        try {
            season.addClub(club1);
            season.addClub(club2);
            season.addClub(club3);
            season.addClub(club4);
            season.addClub(club5);
            season.addClub(club6);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }


        System.out.println(season.getSchedule());









    }
}