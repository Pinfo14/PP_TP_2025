package league;

import com.ppstudios.footballmanager.api.contracts.league.ISeason;

public class DemoLeague {

    public static void main(String[] args) {



                League liga = new League("Liga Portugal");

                // Adicionar várias épocas
                liga.createSeason(new Season(2023));
                liga.createSeason(new Season(2024));
                liga.createSeason(new Season(2025));

                // Percorrer todas as épocas
                for (ISeason aq : liga.getSeasons()) {

                System.out.println(aq);
                }


            }
        }



