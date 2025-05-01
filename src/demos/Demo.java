package demos;

import imports.Imports;
import team.Club;

public class Demo {
    public static void main(String[] args) {
        Imports importClubs = new Imports();

        Club[] club = importClubs.importClubs();
        for (Club c : club) {
            System.out.println(c.getName());
        }
    }
}
