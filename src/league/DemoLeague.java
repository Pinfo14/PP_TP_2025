package league;

import com.ppstudios.footballmanager.api.contracts.league.ISeason;

public class DemoLeague {

    public static void main(String[] args) {



                League liga = new League("Liga Portugal");

                // Adicionar várias épocas
                liga.createSeason(new Season(liga.getName(), 2023));
                liga.createSeason(new Season(liga.getName(), 2024));
                liga.createSeason(new Season(liga.getName(), 2025));



                try{
                    liga.removeSeason(2021);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }


        // Percorrer todas as épocas
            for (ISeason aq : liga.getSeasons()) {

                System.out.println(aq.getName());
            }


            }
        }



