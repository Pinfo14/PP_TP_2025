package imports;

import com.ppstudios.footballmanager.api.contracts.player.IPlayer;
import com.ppstudios.footballmanager.api.contracts.player.IPlayerPosition;
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

                PlayerAttributes attributes = new PlayerAttributes();
                attributes.generateAttributes(playerPos);

                Player playerObj = new Player(
                        name,
                        birthDate,
                        nationality,
                        position,
                        photo,
                        number,
                        attributes
                );

                player[i++] = playerObj;
            }
            return player;

        } catch (IOException | ParseException e) {
            e.printStackTrace();
            return new Player[0];
        }
    }

}
