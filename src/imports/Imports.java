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

import static imports.ImportUtils.*;

/**
 * Nome: Emanuel Jose Teixeira Pinto
 * Número: 8230371
 * Turma: LEI1T1
 *
 * Nome: Roberto Cristiano Martins Faria
 * Número: 8230067
 * Turma: LEI1T2
 */


public class Imports {

    private PlayerAttributes attributes = new PlayerAttributes();

    /**
     * Lê informações dos clubes de um arquivo JSON e constrói um array de  Club.
     * O método le o ficheiro JSON para extrair detalhes do club para criar uma instância de Club.
     * <p>
     * Em caso de erro na leitura ou análise do arquivo, um array vazio de Club é retornado.
     *
     * @return Um array de objetos Club carregados do arquivo JSON. Retorna um array vazio se o arquivo
     * não puder ser lido ou analisado.
     */

    public Club[] importClubs() {
        JSONParser parser = new JSONParser();
        File file = new File("src/Files/clubs.json");

        try  {
            FileReader reader = new FileReader(file);
            JSONArray clubsArray = (JSONArray) parser.parse(reader);

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

    /**
     * Importa jogadores a partir de um ficheiro JSON especificado.
     * O método lê os dados do ficheiro JSON para extrair objetos de jogador
     * e cria instâncias de jogadores com base nos atributos fornecidos.
     * Retorna um array de jogadores ou um array vazio em caso de erro.
     *
     * @param fileName O nome do ficheiro que contém os dados dos jogadores a serem importados.
     *                 O ficheiro deve estar na localização "src/Files/players/".
     * @return Um array de objetos  IPlayer representando os jogadores importados.
     *         Retorna um array vazio caso ocorra algum erro durante a leitura  do ficheiro.
     */
    public IPlayer[] importPlayers(String fileName) {
        JSONParser parser = new JSONParser();
        File file = new File("src/Files/players/" + fileName);

        try  {
            FileReader reader = new FileReader(file);
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
                Object numberObj = playerJson.get("number");
                int number = 0;
                if (numberObj != null) {
                    number = ((Long) numberObj).intValue();
                }

                String playerPos = (String) playerJson.get("basePosition");

                IPlayerPosition position = new PlayerPosition(playerPos);

                PlayerAttributes playerAttributes = attributes.generateAttributes(playerPos);

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




    /**
     * Importa informações de clubes e jogadores associando-os corretamente.
     *
     * Em caso de erro ao importar jogadores, o programa tenta continuar com os próximos clubes
     * e exibe mensagens de erro relevantes.
     *
     * @return Um array de objetos IClub representando os clubes com seus jogadores associados.
     *         Se ocorrerem erros ao importar clubes ou jogadores, os clubes serão retornados
     *         com os jogadores que puderem ser associados.
     */
    public IClub[] importPlayersAndClub() {

        IClub[] club = this.importClubs();

        String directoryPath = "src/Files/players";
        File directory = new File(directoryPath);
        File[] files = directory.listFiles();

        for (IClub c : club) {
            try {
                IPlayer[] players = this.importPlayers(checkNameFileClub(files, c.getName()));
                try {
                    for (IPlayer p : players) {
                        c.addPlayer(p);
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage() + c.getName() + "FAILED");
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

        }
        return club;
    }
}
