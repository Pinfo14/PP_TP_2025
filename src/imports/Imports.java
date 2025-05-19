package imports;

import com.ppstudios.footballmanager.api.contracts.player.IPlayer;
import com.ppstudios.footballmanager.api.contracts.player.IPlayerPosition;
import com.ppstudios.footballmanager.api.contracts.team.IClub;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import player.Player;
import player.PlayerAttributes;
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

    private PlayerAttributes attributes ;


    public Imports() {
        this.attributes = new PlayerAttributes();
    }
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
                String playerPos =(String) playerJson.get("basePosition");

                IPlayerPosition position = new PlayerPosition(playerPos);


                  PlayerAttributes playerAttributes= attributes.generateAttributes(playerPos);


                Player playerObj = new Player(
                        name,
                        birthDate,
                        nationality,
                        position,
                        photo,
                        number,
                        playerAttributes
                );

                player[i++] = playerObj;
            }
            return player;

        } catch (IOException | ParseException e) {
            e.printStackTrace();
            return new Player[0];
        }
    }

    private File[] sortFiles(File[] files) {

        if (files == null || files.length == 0) {
            throw new IllegalArgumentException("Files esta vazio");
        }

        //  Copia para não alterar o array original
        File[] sorted = new File[files.length];
        for (int i = 0; i < files.length; i++) {
            sorted[i] = files[i];
        }

        //  Selection sort por nome
        for (int i = 0; i < sorted.length - 1; i++) {
            int minIndex = i;
            // procura o menor nome entre os restos
            for (int j = i + 1; j < sorted.length; j++) {
                if (sorted[j].getName().compareTo(sorted[minIndex].getName()) < 0) {
                    minIndex = j;
                }
            }
            // troca posição i pelo menor encontrado
            File temp = sorted[i];
            sorted[i] = sorted[minIndex];
            sorted[minIndex] = temp;
        }

        return sorted;
    }



    public IClub[] importPlayersToClub(){

        IClub[] club = this.importClubs();

        String directoryPath = "src/Files/players";

        // Using File class create an object for specific directory
        File directory = new File(directoryPath);

        // Using listFiles method we get all the files of a directory
        // return type of listFiles is array
        File[] unsortedFiles = directory.listFiles();

        File[] files = sortFiles(unsortedFiles);


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
        // Divide o nome do clube em tokens (tudo maiúsculo) [SPORT,LISBOA,E,BENFICA]
        String[] tokens = removerAcentos(clubName).toUpperCase().split("\\s+");
        String lastToken = tokens[tokens.length-1];
        String firstMatch = null;

        for (File f : files) {
            String fname = f.getName();
            int dot = fname.lastIndexOf('.');//index do . exemplo sporting.json
            String base = dot > 0
                    ? fname.substring(0, dot).toUpperCase()//sporting.json -> SPORTING
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
    public String removerAcentos(String texto) {
        // String com todos os caracteres acentuados possíveis
        String comAcento = "ÁÀÂÃÄáàâãäÉÈÊËéèêëÍÌÎÏíìîïÓÒÔÕÖóòôõöÚÙÛÜúùûüÇç";
        // String com caracteres não acentuados correspondentes
        String semAcento = "AAAAAaaaaaEEEEeeeeIIIIiiiiOOOOOoooooUUUUuuuuCc";
        // Cria um StringBuilder com tamanho inicial igual ao texto para melhor performance
        StringBuilder sb = new StringBuilder(texto.length());

        // Percorre cada caractere do texto de entrada
        for (char c : texto.toCharArray()) {
            // Procura o índice do caractere atual na string de caracteres acentuados
            int idx = comAcento.indexOf(c);
            // Se encontrou o caractere acentuado (idx diferente de -1)
            if (idx != -1) {
                // Adiciona o caractere não acentuado correspondente
                sb.append(semAcento.charAt(idx));
            } else {
                // Se não encontrou, mantém o caractere original
                sb.append(c);
            }
        }
        //  retorna o StringBuilder como String
        return sb.toString();
    }

}
