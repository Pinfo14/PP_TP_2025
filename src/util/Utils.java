package util;

public class Utils {

    public static void waitEnter() {
        System.out.print("\nPressione ENTER para continuar...");
        try {
            while (System.in.read() != '\n');
        } catch (Exception e) {}
    }


}
