package menus;

public class SeasonMenu {

    public static void mainSeasonMenu(int year, String club, String name, int round) {

        HeaderMenu.print(year,round, club, name);

        System.out.println("╔═════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                                                                         ║");
        System.out.println("║   1 - Gerir equipas                                                     ║");
        System.out.println("║   2 - Selecionar equipa para treinar                                    ║");
        System.out.println("║-------------------------------------------------------------------------║");
        System.out.println("║   3 - Proxima Jornada                                                   ║");
        System.out.println("║   4 - Listar Jogos                                                      ║");
        System.out.println("║   5 - Listar Resultados                                                 ║");
        System.out.println("║   6 - Classificacao                                                     ║");
        System.out.println("║   7 - Simular o restante epoca                                          ║");
        System.out.println("║                                                                         ║");
        System.out.println("║   0 - Sair                                                              ║");
        System.out.println("╚═════════════════════════════════════════════════════════════════════════╝");
    }


    public static void managementSeasonMenu(int year, String club, String name, int round) {

        HeaderMenu.print(year, round, club, name); // Mostra o cabeçalho no formato caixa

        System.out.println("╔═════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                                                                         ║");
        System.out.println("║   1 - Adicionar Equipas à Liga                                          ║");
        System.out.println("║   2 - Apagar Equipa da Liga                                             ║");
        System.out.println("║   3 - Listar Equipas na Liga                                            ║");
        System.out.println("║                                                                         ║");
        System.out.println("║   0 - Voltar                                                            ║");
        System.out.println("╚═════════════════════════════════════════════════════════════════════════╝");

    }





}
