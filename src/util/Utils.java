package util;

import player.Player;

public class Utils {

    public static void waitEnter() {
        System.out.print("\nPressione ENTER para continuar...");
        try {
            while (System.in.read() != '\n');
        } catch (Exception e) {}
    }

    public static boolean arrayEquals(Player[] a, Player[] b) {

        if (a == null || b == null){
            return false;
        }
        for (int i = 0; i < a.length; i++) {
            if (!a[i].equals(b[i])) {
                return false;
            }
        }
        return true;
    }

}
