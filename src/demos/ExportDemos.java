package demos;

import league.League;

public class ExportDemos {
    public static void main(String[] args) {
        League league = new League("Liga Portugal");

       try {
           league.exportToJson();
       } catch (Exception e) {
           System.out.println(e.getMessage());
       }
    }
}
