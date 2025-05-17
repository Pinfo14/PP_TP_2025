package menus;

import reader.Reader;

public class SeasonMenu {

    public static void mainSeasonMenu(int year, String club, String name) {

        displayMenu(year, club, name);

    }


    private static void displayMenu(int year, String club, String name) {

        StringBuilder menu = new StringBuilder();

        menu.append("╔═════════════════════════════════════════════════════════════════════════════════════════\n");
        menu.append("║\n");
        menu.append("║  PP - SOCCER PARADIGMAS SIMULATOR\n");
        menu.append("║  ");
        menu.append(year);
        menu.append("-");
        menu.append(year + 1);
        menu.append(" - ");
        menu.append(name);
        menu.append("\n");
        //aqui colocar a identificacao do clube a treinar
        menu.append("║  Esta a treinar o ");
        menu.append(club);
        menu.append("\n");
        //clube a treinar.
        menu.append("║\n");
        menu.append("╠════════════════════════════════════════════════════════════════════════════════════════╗\n");
        menu.append("║                                                                                        ║\n");
        menu.append("║   1 - Gerir equipas                                                                    ║\n");
        menu.append("║   2 - Selecionar equipa para treinar                                                   ║\n");
        menu.append("║----------------------------------------------------------------------------------------║\n");
        menu.append("║   3 - Proxima Jornada                                                                  ║\n");
        menu.append("║   4 - Listar Jogos                                                                     ║\n");
        menu.append("║   4 - Listar Rsultados                                                                 ║\n");
        menu.append("║   5 - Classificacao                                                                    ║\n");
        menu.append("║   6 - Simular o restante epoca                                                         ║\n");
        menu.append("║                                                                                        ║\n");
        menu.append("║   0 - Sair                                                                             ║\n");
        menu.append("╚════════════════════════════════════════════════════════════════════════════════════════╝\n");

        System.out.print(menu.toString());

    }

    public static void managementTeamsMenu(int year, String club, String name) {

        StringBuilder menu = new StringBuilder();

        menu.append("╔═════════════════════════════════════════════════════════════════════════════════════════\n");
        menu.append("║\n");
        menu.append("║  PP - SOCCER PARADIGMAS SIMULATOR\n");
        menu.append("║  ");
        menu.append(year);
        menu.append("-");
        menu.append(year + 1);
        menu.append(" - ");
        menu.append(name);
        menu.append("\n");
        //aqui colocar a identificacao do clube a treinar
        menu.append("║  Esta a treinar o ");
        menu.append(club);
        menu.append("\n");
        //clube a treinar.
        menu.append("║\n");
        menu.append("╠════════════════════════════════════════════════════════════════════════════════════════╗\n");
        menu.append("║                                                                                        ║\n");
        menu.append("║   1 - Adicionar Equipas à Liga                                                         ║\n");
        menu.append("║   2 - Apagar Equipa da Liga                                                            ║\n");
        menu.append("║   3 - Listar Equipas na Liga                                                           ║\n");
        menu.append("║----------------------------------------------------------------------------------------║\n");
        menu.append("║   5 - Carregar Jogadores - JSON FILE                                                   ║\n");
        menu.append("║   6 - Criar Jogador                                                                    ║\n");
        menu.append("║                                                                                        ║\n");
        menu.append("║   0 - Voltar                                                                           ║\n");
        menu.append("╚════════════════════════════════════════════════════════════════════════════════════════╝\n");

        System.out.print(menu.toString());

    }





}
