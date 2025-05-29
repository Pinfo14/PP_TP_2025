package imports;

import com.ppstudios.footballmanager.api.contracts.player.IPlayer;
import com.ppstudios.footballmanager.api.contracts.team.IClub;
import org.json.simple.JSONObject;

public class ImportUtils {
    /**
     * Encontra um clube pelo código
     */
    public static IClub findClubByCode(IClub[] clubs, String code) {
        for (IClub club : clubs) {
            if (club.getCode().equals(code)) {
                return club;
            }
        }
        return null;
    }
    public static IPlayer findPlayerInClub(IClub club, String playerName) {
        IPlayer[] players = club.getPlayers();

        for (IPlayer player : players) {
            if (player.getName().equals(playerName)) {
                return player;
            }
        }

        return null; // Não encontrado
    }

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
     * Obtém um valor inteiro do JSON com fallback
     */
    public static int getIntValue(JSONObject json, String key, int defaultValue) {
        if (!json.containsKey(key)) {
            return defaultValue;
        }
        long value = (long)json.get(key);
        return ((Long) value).intValue();
    }

    /**
     * Obtém um valor float do JSON com fallback
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
     * Remove acentos de uma string substituindo cada caractere acentuado
     * pelo correspondente sem acento.
     */
    public static String removerAcentos(String texto) {
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

    public static int countFiles(String[] files, String extension) {
        int leagueCount = 0;
        for (String file : files) {
            if (file.endsWith(extension)) {
                leagueCount++;
            }
        }
        return leagueCount;
    }
}
