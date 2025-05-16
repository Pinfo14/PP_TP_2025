package menus;

import management.LeagueManagement;
import reader.Reader;

public class StartMenu {

    public void menu() {
        int opcao = 0;
        Reader reader = new Reader();


        do {
            displayMenu();
            opcao = reader.readInt(0, 2, "Opçao: ");
             switch (opcao) {
                 case 1:
                     LeagueManagement leagueManagement = new LeagueManagement();
                     leagueManagement.startNewGame();
                 default:
                     //import os dados guardados.
             }

        } while (opcao != 0);

    }

    private void displayMenu() {

        StringBuilder menu = new StringBuilder();

        menu.append("╔═════════════════════════════════════════════════════════════════════════════════════════\n");
        menu.append("║\n");
        menu.append("║  PP - SOCCER PARADIGMAS SIMULATOR\n");
        menu.append("║\n");
        menu.append("╠════════════════════════════════════════════════════════════════════════════════════════╗\n");
        menu.append("║                                                                                        ║\n");
        menu.append("║   1 - NOVO JOGO                                                                        ║\n");
        menu.append("║   2 - CARREGAR JOGO ANTERIOR                                                           ║\n");
        menu.append("║                                                                                        ║\n");
        menu.append("║   0 - SAIR                                                                             ║\n");
        menu.append("║                                                                                        ║\n");
        menu.append("╚════════════════════════════════════════════════════════════════════════════════════════╝\n");

        System.out.print(menu.toString());

    }








}