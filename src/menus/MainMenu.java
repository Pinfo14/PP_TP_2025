package menus;

import java.util.Scanner;

public class MainMenu {

    private static final Scanner scanner = new Scanner(System.in);

    public void menu () {
        boolean sair = false;

        while (!sair) {
            System.out.println("\n=== MENU PRINCIPAL ===");
            System.out.println("1. Visualizar plantel da equipa");
            System.out.println("2. Visualizar calendário do campeonato");
            System.out.println("3. Visualizar estatísticas");
            System.out.println("4. Preparar proximo jogo ");
            System.out.println("5. Simular próxima jornada");
            System.out.println("0. Salvar e Sair");
            System.out.print("Escolha uma opção: ");

            int opcao = lerInteiro();

            switch (opcao) {
                case 1:
                    mostrarPlantel();
                    break;
                case 2:
                    mostrarCalendario();
                    break;
                case 3:
                    mostrarEstatisticas();
                    break;
                case 4:
                    PrepararProxJogo();

                    break;
                case 5:
                    simularJornada();
                    break;
                case 0:
                    sair = true;
                    salvarSair();
                    System.out.println("A sair...");
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        }
    }

    private  int lerInteiro() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (Exception e) {
            return -1;
        }
    }
    private  void salvarSair() {
        System.out.println("A salvar..");
      //ExportJsons
    }

    private  void mostrarPlantel() {
        System.out.println("\n--- Plantel da Equipa ---");
        // aqui chamo o método  para obter os jogadores e mostrar
    }

    private  void mostrarCalendario() {
        System.out.println("\n--- Calendário do Campeonato ---");
        // aqui chamo o método pra obter o Schedule
    }

    private  void mostrarEstatisticas() {
        System.out.println("\n--- Estatísticas ---");
        // aqui mostra estatísticas
    }

    private  void simularJornada() {
        System.out.println("\n--- Simular Próxima Jornada ---");
        // aqui chama simulateRound() do ISeason
    }

    private  void PrepararProxJogo() {
        System.out.println("\n--- Preparar  Jogo ---");
        // aqui usas o método displayMatchResult(match) e percorres os eventos
    }
}
