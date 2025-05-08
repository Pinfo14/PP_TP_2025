package league;

import com.ppstudios.footballmanager.api.contracts.league.ISeason;
import com.ppstudios.footballmanager.api.contracts.team.IClub;
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

        ISeason season = (Season) liga.getSeason(2023);

        try {
            season.addClub(club1);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        try {
            season.addClub(club2);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        try {
            season.addClub(club3);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        try {
            season.addClub(club4);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        try {
            season.addClub(club5);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        try {
            season.addClub(club6);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }


        for (IClub cb : season.getCurrentClubs()) {
            System.out.println(cb.toString());
        }

        System.out.println(season.getSchedule());









    }
}



