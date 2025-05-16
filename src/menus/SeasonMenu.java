package menus;

import reader.Reader;

public class SeasonMenu {

    public void menu(int year, String club, String name) {
        int opcao = 0;
        Reader reader = new Reader();

        displayMenu(year, club, name);

        do {
            opcao = reader.readInt(0,2,"Selecione uma opcao: ");
            switch (opcao) {
                case 1:
                    int opcaoTemp = 0;
                    managementTeamsMenu(year,club, name);
                    do{
                        opcao = reader.readInt(0,6,"Selecione uma opcao: ");
                    } while (opcaoTemp != 0);
                case 2:
                    //selecct equipa para treinar
                default:
                    //import os dados guardados.
            }

        } while (opcao != 0);

    }


    public void displayMenu(int year, String club, String name) {

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

    public void managementTeamsMenu(int year, String club, String name) {

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
        menu.append("║   1 - Carregar Equipa - JSON FILE                                                      ║\n");
        menu.append("║   2 - Criar Equipa                                                                     ║\n");
        menu.append("║   3 - Apagar Equipa                                                                    ║\n");
        menu.append("║   4 - Listar Equipas na Liga                                                           ║\n");
        menu.append("║----------------------------------------------------------------------------------------║\n");
        menu.append("║   5 - Carregar Jogadores - JSON FILE                                                   ║\n");
        menu.append("║   6 - Criar Jogador                                                                    ║\n");
        menu.append("║                                                                                        ║\n");
        menu.append("║   0 - Voltar                                                                           ║\n");
        menu.append("╚════════════════════════════════════════════════════════════════════════════════════════╝\n");

        System.out.print(menu.toString());

    }





}
