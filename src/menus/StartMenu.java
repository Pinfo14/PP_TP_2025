package menus;

import management.LeagueManagement;
import management.SaveGame;
import reader.Reader;

import java.io.IOException;

public class StartMenu {

    private SaveGame saveGame;
    private  LeagueManagement leagueManagement;

    public StartMenu() {
        this.saveGame = new SaveGame();
        this.leagueManagement = new LeagueManagement();
    }

    public void menu() {
        int opcao = 0;
        Reader reader = new Reader();


        do {
            displayMenu();
            opcao = reader.readInt(0, 2, "Opçao: ");

             switch (opcao) {
                 case 1:
                     leagueManagement.startNewGame(true);
                     break;
                 default:
                     //import os dados guardados.
             }

             if (opcao == 0){
                 try {
                     this.saveGame.saveGame(leagueManagement.getLeague());
                 } catch (IOException e) {
                    System.out.println("Erro ao salvar o jogo: " + e.getMessage());
                 }
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