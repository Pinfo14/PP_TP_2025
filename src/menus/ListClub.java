package menus;

import com.ppstudios.footballmanager.api.contracts.team.IClub;

public class ListClub {

    public static void listClubLoaded(IClub[] clubs) {

        if (clubs == null) {
            throw new NullPointerException("Lista de clubes é nula!");
        }
        if (clubs.length == 0) {
            System.out.println("Nenhum clube foi carregado.");
            return;
        }

        int counter = 1;

        System.out.println("----+-------------------------------------+--------+-----------");
        System.out.println(String.format("%-3s | %-35s | %-6s | %-10s", "ID", "NOME", "SIGLA", "PAIS"));
        System.out.println("----+-------------------------------------+--------+-----------");

        for (IClub cl : clubs) {
            System.out.println(String.format("%-3d | %-35s | %-6s | %-10s", counter++, cl.getName(), cl.getCode(), cl.getCountry()));
        }
    }
}
