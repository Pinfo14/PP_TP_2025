package menus;

import team.Team;

public class ListTeams
{

    private static void  header(String name) {
        System.out.println("----+----------------------+-------------+-----+-----------------");
        System.out.println("   11 Inicial " + name);
        System.out.println("----+----------------------+-------------+-----+-----------------");
    }

    public static void list(Team team1, Team team2) {

        if (team1 == null || team2 == null) {
            System.out.println("Não há equipas.");
        }

        header(team1.getClub().getName());
        for(int i = 0; i < 11; i++) {
            if(team1.getPlayers()[i] != null) {
                System.out.println(team1.getPlayers()[i].getPosition().getDescription() + " - " + team1.getPlayers()[i].getName() +
                        "(" + team1.getPlayers()[i].getNumber() + ")");
            }
        }
        header(team2.getClub().getName());
        for(int i = 0; i < 11; i++) {
            if(team2.getPlayers()[i] != null) {
                System.out.println(team2.getPlayers()[i].getPosition().getDescription() + " - " + team2.getPlayers()[i].getName() +
                        "(" + team2.getPlayers()[i].getNumber() + ")");
            }
        }

    }
}
