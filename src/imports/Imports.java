package imports;

import com.ppstudios.footballmanager.api.contracts.player.IPlayer;
import com.ppstudios.footballmanager.api.contracts.player.IPlayerPosition;
import com.ppstudios.footballmanager.api.contracts.team.IClub;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import player.Player;
import player.PlayerPosition;
import team.Club;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;

/**
 * Nome: Emanuel Jose Teixeira Pinto
 * Número: 8230371
 * Turma: LEI1T1
 *
 * Nome: <Nome completo do colega de grupo>
 * Número: <Número mecanográfico do colega de grupo>
 * Turma: <Turma do colega de grupo>
 */


public class Imports {

    /**
     * Lê informações dos clubes de um arquivo JSON e constrói um array de objetos Club.
     * O método analisa a estrutura JSON para extrair detalhes incluindo nome do clube, código, país,
     * ano de fundação, nome do estádio e logo. Cada entrada extraída é usada para criar uma instância de Club.
     *
     * Em caso de erro na leitura ou análise do arquivo, um array vazio de Club é retornado.
     *
     * @return Um array de objetos Club carregados do arquivo JSON. Retorna um array vazio se o arquivo
     * não puder ser lido ou analisado.
     */

    public Club[] importClubs() {
        JSONParser parser = new JSONParser();
        File file = new File("src/Files/clubs.json");

        try (FileReader reader = new FileReader(file)) {
            JSONArray clubsArray = (JSONArray) parser.parse(reader);
            // Cria o array de Club com o tamanho do jsonarray
            Club[] clubs = new Club[clubsArray.size()];

            int i = 0;
            for (Object obj : clubsArray) {
                JSONObject clubJson = (JSONObject) obj;

                String name = (String) clubJson.get("name");
                String code = (String) clubJson.get("code");
                String country = (String) clubJson.get("country");
                // json-simple devolve Long
                int foundedYear = ((Long) clubJson.get("founded")).intValue();
                String stadiumName = (String) clubJson.get("stadium");
                String logo = (String) clubJson.get("logo");


                Club club = new Club(
                        name,
                        code,
                        country,
                        foundedYear,
                        stadiumName,
                        logo
                );

                clubs[i++] = club;
            }
            return clubs;

        } catch (IOException | ParseException e) {
            e.printStackTrace();
            return new Club[0];
        }
    }

    public IPlayer[] importPlayers( String fileName) {
        JSONParser parser = new JSONParser();
        File file = new File("src/Files/players/" + fileName);

        try (FileReader reader = new FileReader(file)) {
            JSONObject root = (JSONObject) parser.parse(reader);

            JSONArray playerArray = null;
            if (root.containsKey("plantel")) {
                playerArray = (JSONArray) root.get("plantel");
            } else if (root.containsKey("squad")) {
                playerArray = (JSONArray) root.get("squad");
            } else {
                System.err.println("Erro: o ficheiro não contém 'plantel' nem 'squad'.");
                return new Player[0];
            }

            IPlayer[] player = new Player[playerArray.size()];

            int i = 0;
            for (Object obj : playerArray) {
                JSONObject playerJson = (JSONObject) obj;

                String name = (String) playerJson.get("name");
                LocalDate birthDate = LocalDate.parse((String) playerJson.get("birthDate"));
                String nationality = (String) playerJson.get("nationality");
                String photo = (String) playerJson.get("photo");
                int number = ((Long) playerJson.get("number")).intValue();
                IPlayerPosition position = new PlayerPosition((String) playerJson.get("basePosition"));

                Player playerObj = new Player(
                        name,
                        birthDate,
                        nationality,
                        position,
                        photo,
                        number
                );

                player[i++] = playerObj;
            }
            return player;

        } catch (IOException | ParseException e) {
            e.printStackTrace();
            return new Player[0];
        }
    }




    public IClub[] importPlayersToClub(){

        IClub[] club = this.importClubs();

        String directoryPath = "src/Files/players";

        // Using File class create an object for specific directory
        File directory = new File(directoryPath);

        // Using listFiles method we get all the files of a directory
        // return type of listFiles is array
        File[] files = directory.listFiles();


        for (IClub c : club) {
            try {
                IPlayer[] players = this.importPlayers(checkNameFileClub(files, c.getName()));
                try {
                    for (IPlayer p : players) {
                        c.addPlayer(p);
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage() + c.getName()+"FAILED");
                }
            } catch (Exception e) {
                System.out.println(e.getMessage() );
            }

        }

        return club;
    }



    private  String checkNameFileClub(File[] files, String clubName) {
        // Divide o nome do clube em tokens (tudo maiúsculo)
        String[] tokens = removerAcentos(clubName).toUpperCase().split("\\s+");
        String lastToken = tokens[tokens.length - 1];
        String firstMatch = null;

        for (File f : files) {
            String fname = f.getName();
            int dot = fname.lastIndexOf('.');
            String base = dot > 0
                    ? fname.substring(0, dot).toUpperCase()
                    : fname.toUpperCase();

            // Para cada token, verifica se o nome-base do ficheiro contém o token
            for (String token : tokens) {
                if (base.contains(token)) {
                    // Se o token for o último, prioridade máxima
                    if (token.equals(lastToken)) {
                        return fname;
                    }
                    // Guarda o primeiro match caso ainda não exista
                    if (firstMatch == null) {
                        firstMatch = fname;
                    }
                    // não procures mais tokens neste ficheiro
                    break;
                }
            }
        }

        // Se não encontrou match no último token, retorna o primeiro match (ou null)
        return firstMatch;
    }


    /**
     * Remove acentos de uma string substituindo cada caractere acentuado
     * pelo correspondente sem acento.
     */
    public  String removerAcentos(String texto) {
        String comAcento    = "ÁÀÂÃÄáàâãäÉÈÊËéèêëÍÌÎÏíìîïÓÒÔÕÖóòôõöÚÙÛÜúùûüÇç";
        String semAcento    = "AAAAAaaaaaEEEEeeeeIIIIiiiiOOOOOoooooUUUUuuuuCc";
        StringBuilder sb = new StringBuilder(texto.length());

        for (char c : texto.toCharArray()) {
            int idx = comAcento.indexOf(c);
            if (idx != -1) {
                sb.append(semAcento.charAt(idx));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

}
