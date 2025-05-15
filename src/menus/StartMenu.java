package menus;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

public class StartMenu {

    private static final int MENU_WIDTH = 40;               // largura total do box
    private static final String TITLE = "PPS - Paradigmas Player Soccer";
    private static final int CONTENT_WIDTH = MENU_WIDTH - 2; // largura interna (sem bordas)
    private static final int OPTION_PAD = CONTENT_WIDTH - 5;// espaços reservados para o texto da opção

    public static void main(String[] args) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        boolean executar = true;

        while (executar) {
            String border = repeat("=", MENU_WIDTH);

            // monta o menu
            StringBuilder menu = new StringBuilder();
            menu.append("\n")
                    .append(border).append("\n")
                    .append("|").append(centerText(TITLE, CONTENT_WIDTH)).append("|\n")
                    .append(border).append("\n")
                    .append(String.format("| %2d - %-"+ OPTION_PAD +"s |\n", 1, "Novo jogo"))
                    .append(String.format("| %2d - %-"+ OPTION_PAD +"s |\n", 2, "Carregar jogo"))
                    .append(String.format("| %2d - %-"+ OPTION_PAD +"s |\n", 0, "Sair"))
                    .append(border).append("\n")
                    .append("Escolha uma opção: ");

            System.out.print(menu.toString());

            try {
                String opcao = reader.readLine().trim();
                switch (opcao) {
                    case "1":
                        System.out.println("Iniciando um novo jogo...");
                        // novoJogo();
                        break;
                    case "2":
                        System.out.println("Carregando jogo salvo...");
                        // carregarJogo();
                        break;
                    case "0":
                        System.out.println("Saindo. Até logo!");
                        executar = false;
                        break;
                    default:
                        System.out.println("Opção inválida! Tente novamente.");
                }
            } catch (IOException e) {
                System.err.println("Erro ao ler entrada: " + e.getMessage());
                executar = false;
            }
        }

        // fecha o reader
        try {
            reader.close();
        } catch (IOException ignored) { }
    }

    // repete uma string 's' 'times' vezes
    private static String repeat(String s, int times) {
        return new String(new char[times]).replace("\0", s);
    }

    // centraliza 'text' em um campo de largura 'width'
    private static String centerText(String text, int width) {
        if (text.length() >= width) {
            return text.substring(0, width);
        }
        int padding = (width - text.length()) / 2;
        int remainder = width - text.length() - padding;
        return String.format("%" + padding + "s%s%" + remainder + "s", "", text, "");
    }

    // stubs para lógica de jogo:
    /*
    private static void novoJogo() {
        // implementar nova partida
    }

    private static void carregarJogo() {
        // implementar carregamento de partida
    }
    */
}