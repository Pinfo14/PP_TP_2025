package imports;

import com.ppstudios.footballmanager.api.contracts.player.IPlayer;
import com.ppstudios.footballmanager.api.contracts.team.IClub;
import org.json.simple.JSONObject;
import team.Club;

import java.io.File;

/**
 * Nome: Emanuel Jose Teixeira Pinto
 * Número: 8230371
 * Turma: LEI1T1
 *
 * Nome: Roberto Cristiano Martins Faria
 * Número: 8230067
 * Turma: LEI1T2
 */
public class ImportUtils {
    /**
     * Localiza e retorna um clube no array fornecido que corresponda ao código especificado.
     * Se nenhum clube correspondente for encontrado, retorna null.
     *
     * @param clubs O array de objetos IClub onde a procura será realizada.
     * @param code O código do clube a encontrar.
     * @return O objeto IClub correspondente ao código fornecido ou null caso não seja encontrado.
     */
    public static IClub findClubByCode(IClub[] clubs, String code) {
        for (IClub club : clubs) {
            if (club.getCode().equals(code)) {
                return club;
            }
        }
        return new Club("FOLGA");
    }

    /**
     * Encontra e retorna um jogador específico em um clube com base no nome do jogador.
     * O método percorre todos os jogadores do clube e busca por um jogador que tenha o nome exato especificado.
     * Se nenhum jogador correspondente for encontrado, retorna null.
     *
     * @param club O clube onde a procura pelos jogadores será realizada.
     * @param playerName O nome do jogador a ser procurado no clube.
     * @return O objeto IPlayer correspondente ao nome fornecido ou null caso o jogador não seja encontrado no clube.
     */
    public static IPlayer findPlayerInClub(IClub club, String playerName) {
        IPlayer[] players = club.getPlayers();

        for (IPlayer player : players) {
            if (player.getName().equals(playerName)) {
                return player;
            }
        }

        return null;
    }

    /**
     * Localiza e retorna um jogador específico pelo nome dentro de uma lista de clubes.
     * O método percorre cada clube fornecido e utiliza a lógica definida para procurar pelo jogador no clube.
     * Se nenhum jogador correspondente for encontrado ao longo de todos os clubes, retorna null.
     *
     * @param clubs Um array de objetos IClub onde os jogadores serão procurados.
     * @param playerName O nome do jogador que deve ser procurado.
     * @return O objeto IPlayer correspondente ao nome fornecido ou null caso o jogador não seja encontrado em nenhum dos clubes.
     */
    public static IPlayer findPlayerByName(IClub[] clubs, String playerName) {
        if (playerName == null) {
            return null;
        }
        for (IClub club : clubs) {
            return findPlayerInClub(club,playerName);
        }

        System.out.println("Jogador não encontrado: " + playerName);
        return null;
    }
    /**
     * Obtém um valor inteiro de um objeto JSON utilizando uma chave especificada.
     * Caso a chave não exista no JSON, retorna o valor padrão fornecido.
     *
     * @param json O objeto JSON de onde o valor será extraído.
     * @param key A chave no JSON associada ao valor que deve ser obtido.
     * @param defaultValue O valor padrão a ser retornado caso a chave não esteja presente no JSON.
     * @return O valor inteiro correspondente à chave especificada no JSON, ou o valor padrão se a chave não existir.
     */
    public static int getIntValue(JSONObject json, String key, int defaultValue) {
        if (!json.containsKey(key)) {
            return defaultValue;
        }
        long value = (long)json.get(key);
        return ((Long) value).intValue();
    }

    /**
     * Obtém um valor do tipo float de um objeto JSON utilizando uma chave especificada.
     * Caso a chave não exista no JSON, retorna o valor padrão fornecido.
     *
     * @param json O objeto JSON de onde o valor será extraído.
     * @param key A chave no JSON associada ao valor que deve ser obtido.
     * @param defaultValue O valor padrão a ser retornado caso a chave não esteja presente no JSON.
     * @return O valor de ponto flutuante correspondente à chave especificada no JSON, ou o valor padrão se a chave não existir.
     */
    public static float getFloatValue(JSONObject json, String key, float defaultValue) {
        if (!json.containsKey(key)) {
            return defaultValue;
        }
        Object value = json.get(key);
        if (value == null) {
            return defaultValue;
        }

        float floatValue = ((Double) value).floatValue();

        return floatValue;
    }

    /**
     * Remove todos os caracteres acentuados de uma string de entrada e
     * substitui pelos seus equivalentes não acentuados.
     *
     * @param texto A string que contém os caracteres acentuados a serem removidos.
     * @return Uma nova string sem acentuação.
     */
    public static String removerAcentos(String texto) {

        String comAcento = "ÁÀÂÃÄáàâãäÉÈÊËéèêëÍÌÎÏíìîïÓÒÔÕÖóòôõöÚÙÛÜúùûüÇç";

        String semAcento = "AAAAAaaaaaEEEEeeeeIIIIiiiiOOOOOoooooUUUUuuuuCc";

        StringBuilder sb = new StringBuilder(texto.length());

        for (char c : texto.toCharArray()) {
            int idx = comAcento.indexOf(c);
            if (idx != -1) {
                sb.append(semAcento.charAt(idx));
            }
            else {
                sb.append(c);
            }
        }

        return sb.toString();
    }

    /**
     * Conta a quantidade de ficheiros em um array cujo nome termina com uma extensão específica.
     *
     * @param files O array de nomes de ficheiros a ser analisado.
     * @param extension A extensão que será usada como critério de correspondência.
     * @return O número de ficheiros que terminam com a extensão especificada.
     */
    public static int countFiles(String[] files, String extension) {
        int leagueCount = 0;
        for (String file : files) {
            if (file.endsWith(extension)) {
                leagueCount++;
            }
        }
        return leagueCount;
    }


    public static String checkNameFileClub(File[] files, String clubName) {

        // Divide o nome do clube em tokens (tudo maiúsculo) [SPORT,LISBOA,E,BENFICA]
        String[] tokens = removerAcentos(clubName).toUpperCase().split("\\s+");
        String lastToken = tokens[tokens.length - 1];
        String firstMatch = null;

        for (File f : files) {
            String fname = f.getName();
            int dot = fname.lastIndexOf('.');//index do . exemplo sporting.json
            String base;
            if (dot > 0) {
                base = fname.substring(0, dot).toUpperCase();//sporting.json -> SPORTING
            } else {
                base = fname.toUpperCase();
            }

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
}
