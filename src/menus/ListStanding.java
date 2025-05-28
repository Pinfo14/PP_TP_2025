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
        IStanding[] standingsCopy = standings.clone();


        for (int i = 0; i < standingsCopy.length - 1; i++) {
            for (int j = i + 1; j < standingsCopy.length; j++) {
                if (standingsCopy[i] != null && standingsCopy[j] != null) {
                    boolean troca = false;

                    if (standingsCopy[i].getPoints() < standingsCopy[j].getPoints()) {
                        troca = true;
                    } else if (standingsCopy[i].getPoints() == standingsCopy[j].getPoints()) {
                        if (standingsCopy[i].getGoalScored() < standingsCopy[j].getGoalScored()) {
                            troca = true;
                        } else if (standingsCopy[i].getGoalScored() == standingsCopy[j].getGoalScored()) {
                            if (standingsCopy[i].getGoalsConceded() > standingsCopy[j].getGoalsConceded()) {
                                troca = true;
                            }
                        }
                    }

                    if (troca) {
                        IStanding temp = standingsCopy[i];
                        standingsCopy[i] = standingsCopy[j];
                        standingsCopy[j] = temp;
                    }
                }
            }
        }

        System.out.println("---+----------------------------+-----+----+----+----+----+----+----+----");
        System.out.println(String.format("%-3s| %-27s| %-4s| %-3s| %-3s| %-3s| %-3s| %-3s| %-3s| %-4s",
                "POS", "EQUIPA", "PTS", "J", "V", "E", "D", "GM", "GS", "DIF"));
        System.out.println("---+----------------------------+-----+----+----+----+----+----+----+----");

        for(int i = 0; i < standingsCopy.length; i++) {
            if(standingsCopy[i] != null) {

                Standing standing = (Standing) standingsCopy[i];

                int games = standings[i].getWins() + standings[i].getLosses() + standings[i].getDraws();

                System.out.println(String.format("%-3d| %-27s| %-4d| %-3d| %-3d| %-3d| %-3d| %-3d| %-3d| %-4d",
                        i + 1, standing.getClub().getName(), standing.getPoints(), games, standing.getWins(), standing.getDraws(),
                        standing.getLosses(), standing.getGoalScored(), standing.getGoalsConceded(), standing.getGoalDifference()));
            }


        }
    }
    public static void listFinalStanding(IStanding[] standings, String name) {
        if (standings == null) {
            throw new NullPointerException("\nTabela classificativa é nula!");

        }

        if (standings.length == 0) {
            System.out.println("\nErro.");
            return;
        }

        IStanding[] standingsCopy = standings.clone();

        // Ordena a cópia
        for (int i = 0; i < standingsCopy.length - 1; i++) {
            for (int j = i + 1; j < standingsCopy.length; j++) {
                if (standingsCopy[i] != null && standingsCopy[j] != null) {
                    boolean troca = false;

                    if (standingsCopy[i].getPoints() < standingsCopy[j].getPoints()) {
                        troca = true;
                    } else if (standingsCopy[i].getPoints() == standingsCopy[j].getPoints()) {
                        if (standingsCopy[i].getGoalScored() < standingsCopy[j].getGoalScored()) {
                            troca = true;
                        } else if (standingsCopy[i].getGoalScored() == standingsCopy[j].getGoalScored()) {
                            if (standingsCopy[i].getGoalsConceded() > standingsCopy[j].getGoalsConceded()) {
                                troca = true;
                            }
                        }
                    }

                    if (troca) {
                        IStanding temp = standingsCopy[i];
                        standingsCopy[i] = standingsCopy[j];
                        standingsCopy[j] = temp;
                    }
                }
            }
        }

        System.out.println("-------------------------------------------------------------------------");
        System.out.println("VENCEDOR DA " + name + " - ");
        System.out.println("-------------------------------------------------------------------------");
        System.out.println("---+----------------------------+-----+----+----+----+----+----+----+----");
        System.out.println(String.format("%-3s| %-27s| %-4s| %-3s| %-3s| %-3s| %-3s| %-3s| %-3s| %-4s",
                "POS", "EQUIPA", "PTS", "J", "V", "E", "D", "GM", "GS", "DIF"));
        System.out.println("---+----------------------------+-----+----+----+----+----+----+----+----");

        for(int i = 0; i < standingsCopy.length; i++) {
            if(standingsCopy[i] != null) {

                Standing standing = (Standing) standingsCopy[i];

                int games = standings[i].getWins() + standings[i].getLosses() + standings[i].getDraws();

                System.out.println(String.format("%-3d| %-27s| %-4d| %-3d| %-3d| %-3d| %-3d| %-3d| %-3d| %-4d",
                        i + 1, standing.getClub().getName(), standing.getPoints(), games, standing.getWins(), standing.getDraws(),
                        standing.getLosses(), standing.getGoalScored(), standing.getGoalsConceded(), standing.getGoalDifference()));
            }

        }
    }

}
