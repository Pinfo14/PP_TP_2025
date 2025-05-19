package reader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Reader
{

    public int readInt(int minOption, int maxOption, String message) {
        try {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(System.in)
            );
            int numero = -1;
            do{
                System.out.print(message);
                String linha = reader.readLine();
                try{
                    numero = Integer.parseInt(linha);
                }catch (NumberFormatException e){
                    System.out.println("Por vafor introduza um numero");
                }
                if(numero < minOption || numero > maxOption){
                    System.out.println("Por favor digite um numero valido.");
                }
            } while (numero < minOption || numero > maxOption);
            return numero;
        } catch (IOException e) {
            System.out.println("Erro de entrada.");
        }
        return -1;
    }

    public String readString(String message) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.print(message);
        try {
            return reader.readLine();
        } catch (IOException e) {
            System.out.println("Erro ao ler a entrada.");
            return "";
        }
    }


}
