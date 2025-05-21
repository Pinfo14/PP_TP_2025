package menus;

import util.Utils;

public class RoundMenu {

    public static void topMenu(int round, String opponent, String matches) {

        System.out.println("=========================================================================");
        System.out.println("  JORNADA " + round);
        System.out.println("=========================================================================");
        System.out.println("Os jogos desta jornada são:");
        System.out.println(matches);
        System.out.println("Prepare a sua equipa para o proximo jogo contra " + opponent + ".");
        System.out.println("O proximo passo é selecionar a sua tatica e o 11 inicial.");
        Utils.waitEnter();
    }
}
