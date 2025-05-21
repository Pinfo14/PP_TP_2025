package menus;

import com.ppstudios.footballmanager.api.contracts.league.IStanding;
import com.ppstudios.footballmanager.api.contracts.team.IClub;
import league.Standing;

public class ListStanding {

    public static void list(IStanding[] standings) {
        if (standings == null) {
            throw new NullPointerException("\nTabela classificativa é nula!");

        }

        if (standings.length == 0) {
            System.out.println("\nErro.");
            return;
        }

        int count = 0;
        for (int i = 0; i < standings.length - 1; i++) {
            for (int j = i + 1; j < standings.length; j++) {
                if(standings[i] != null && standings[j] != null) {
                    boolean troca = false;

                    if (standings[i].getPoints() < standings[j].getPoints()) {
                        troca = true;
                    } else if (standings[i].getPoints() == standings[j].getPoints()) {
                        if (standings[i].getGoalScored() < standings[j].getGoalScored()) {
                            troca = true;
                        } else if (standings[i].getGoalScored() == standings[j].getGoalScored()) {
                            if (standings[i].getGoalsConceded() > standings[j].getGoalsConceded()) {
                                troca = true;
                            }
                        }
                    }

                    if (troca) {
                        IStanding temp = standings[i];
                        standings[i] = standings[j];
                        standings[j] = temp;
                    }
                }
            }
        }

        System.out.println("---+----------------------------+-----+----+----+----+----+----+----+----");
        System.out.println(String.format("%-3s| %-27s| %-4s| %-3s| %-3s| %-3s| %-3s| %-3s| %-3s| %-4s",
                "POS", "EQUIPA", "PTS", "J", "V", "E", "D", "GM", "GS", "DIF"));
        System.out.println("---+----------------------------+-----+----+----+----+----+----+----+----");

        for(int i = 0; i < standings.length; i++) {
            if(standings[i] != null) {

                Standing standing = (Standing) standings[i];

                int games = standings[i].getWins() + standings[i].getLosses() + standings[i].getDraws();

                System.out.println(String.format("%-3d| %-27s| %-4d| %-3d| %-3d| %-3d| %-3d| %-3d| %-3d| %-4d",
                        i + 1, standing.getClub().getName(), standing.getPoints(), games, standing.getWins(), standing.getDraws(),
                        standing.getLosses(), standing.getGoalScored(), standing.getGoalsConceded(), standing.getGoalDifference()));
            }

        }
    }

}
