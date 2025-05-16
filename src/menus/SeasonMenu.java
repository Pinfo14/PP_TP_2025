package menus;

import reader.Reader;

public class SeasonMenu {

    public void menu() {
        int opcao = 0;
        Reader reader = new Reader();

        displayMenu(2020, "FC PORTO");

        do {
            opcao = reader.readInt(0,2,"Selecione um ano para iniciar: ");
            switch (opcao) {
                case 1:
                    //nova liga - carrega os jogos
                default:
                    //import os dados guardados.
            }

        } while (opcao != 0);

    }


    public void displayMenu(int year, String club) {

        StringBuilder menu = new StringBuilder();

        menu.append("╔════════════════════════════════════════════════════════════════════════════════════════╗\n");
        menu.append("║                                                                                        ║\n");
        menu.append("║                          PP - SOCCER PARADIGMAS SIMULATOR                              ║\n");
        menu.append("║                      BEM VINDO À LIGA PROFESSIONAL DE FUTEBOL");
        menu.append(year);
        menu.append("/");
        menu.append(year+1);
        menu.append("                         ║\n");
        menu.append("║   ");
        menu.append(club);
        menu.append("                         ║\n");
        //mostra o clube que esta selicionado e jornada
        menu.append("╠════════════════════════════════════════════════════════════════════════════════════════╣\n");
        menu.append("║                                                                                        ║\n");
        menu.append("║   1 - Gerir equipas                                                                    ║\n");
        menu.append("║   2 - Selecionar equipa para treinar                                                   ║\n");
        menu.append("║----------------------------------------------------------------------------------------║\n");
        menu.append("║   3 - Proxima Jornada                                                                  ║\n");
        menu.append("║   4 - Jogos e Rsultados                                                                ║\n");
        menu.append("║   5 - Classificacao                                                                    ║\n");
        menu.append("║                                                                                        ║\n");
        menu.append("║   0 - Sair                                                                              ║\n");
        menu.append("╚════════════════════════════════════════════════════════════════════════════════════════╝\n");

        System.out.print(menu.toString());

    }

    public void displayMenu(int year) {

        StringBuilder menu = new StringBuilder();

        menu.append("╔════════════════════════════════════════════════════════════════════════════════════════╗\n");
        menu.append("║                                                                                        ║\n");
        menu.append("║                          PP - SOCCER PARADIGMAS SIMULATOR                              ║\n");
        menu.append("║                      BEM VINDO À LIGA PROFESSIONAL DE FUTEBOL");
        menu.append(year);
        menu.append("/");
        menu.append(year+1);
        menu.append("                         ║\n");
        menu.append("╠════════════════════════════════════════════════════════════════════════════════════════╣\n");
        menu.append("║                                                                                        ║\n");
        menu.append("║   1 - Gerir equipas                                                                    ║\n");
        menu.append("║   2 - Selecionar equipa para treinar                                                   ║\n");
        menu.append("║----------------------------------------------------------------------------------------║\n");
        menu.append("║   3 - Proxima Jornada                                                                  ║\n");
        menu.append("║   4 - Jogos e Rsultados                                                                ║\n");
        menu.append("║   5 - Classificacao                                                                    ║\n");
        menu.append("║                                                                                        ║\n");
        menu.append("║  0 - Sair                                                                              ║\n");
        menu.append("╚════════════════════════════════════════════════════════════════════════════════════════╝\n");

        System.out.print(menu.toString());

    }



}
