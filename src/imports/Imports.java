package imports;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import player.Player;
import team.Club;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Nome: Emanuel Jose Teixeira Pinto
 * Número: 8230371
 * Turma: LEI1T1
 * <p>
 * Nome: <Nome completo do colega de grupo>
 * Número: <Número mecanográfico do colega de grupo>
 * Turma: <Turma do colega de grupo>
 */


public class Imports {

    /**
     * Lê informações dos clubes de um arquivo JSON e constrói um array de objetos Club.
     * O método analisa a estrutura JSON para extrair detalhes incluindo nome do clube, código, país,
     * ano de fundação, nome do estádio e logo. Cada entrada extraída é usada para criar uma instância de Club.
     * <p>
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

    /**
     * Gera o nome do arquivo para um determinado clube. O nome do arquivo é criado usando
     * a última palavra do nome do clube seguida da extensão ".json".
     *
     * @param club O objeto clube cujo nome será usado para gerar o nome do arquivo.
     * @return Uma string representando o nome do arquivo para o clube fornecido.
     */
    private String fileName(Club club) {
        String[] partes = club.getName().split("\\s+");//divide a string do nome em partes usando espaços em branco como separador
// \\s+ é uma expressão regex que significa "um ou mais espaços em branco" 
        return partes[partes.length - 1] + ".json";
    }

    //posso fazer agora um import player que recebe o path
    //e com base no nome do club e do ficheiro importa os players e instancia Player returnando um array de players
    //esse array e dps usado no import clubs que adiciona os players ao club.

}
