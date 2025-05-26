package management;

import com.ppstudios.footballmanager.api.contracts.league.ILeague;
import com.ppstudios.footballmanager.api.contracts.team.IClub;
import imports.Imports;
import league.League;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import team.Club;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


/**
 * Nome: Emanuel Jose Teixeira Pinto
 * Número: 8230371
 * Turma: LEI1T1
 *
 * Nome: Roberto Cristiano Martins Faria
 * Número: 8230067
 * Turma: LEI1T2
 */
public class SaveGame {

    private static final String SAVE_DIRECTORY = "src/Files/SaveGames/";
    private static final String CLUBS_FILE = "all_clubs.json";
    private static final String LEAGUE_FILE = "_league.json";

    public SaveGame() {
        // Criar diretório de saves se não existir
        createSaveDirectory();
    }

    /**
     * Cria o diretório de saves se não existir
     */
    private void createSaveDirectory() {
        File directory = new File(SAVE_DIRECTORY);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    /**
     * Guarda um jogo completo (liga com todas as épocas)
     */
    public void saveGame(ILeague league) throws IOException {
        if (league == null) {
            throw new IllegalArgumentException("Liga não pode ser nula");
        }

        System.out.println("A guardar jogo: " + league.getName() + "...");

        // Guardar todos os clubes com jogadores
        saveAllClubs();

        // Guardar a liga
        saveLeague(league);

        System.out.println("Jogo guardado com sucesso: " + league.getName());
    }



    /**
     * Método auxiliar para obter int do JSON com valor padrão
     */
    private int getIntFromJson(JSONObject json, String key, int defaultValue) {
        if (json.containsKey(key)) {
            return ((Long) json.get(key)).intValue();
        }
        return defaultValue;
    }

    /**
     * Encontra um clube pelo código
     */
    private IClub findClubByCode(IClub[] clubs, String code) {
        if (clubs == null || code == null) {
            return null;
        }

        for (IClub club : clubs) {
            if (club != null && club.getCode().equals(code)) {
                return club;
            }
        }
        return null;
    }


    /**
     * Guarda todos os clubes com os seus jogadores
     */
    private void saveAllClubs() throws IOException {
        // Importar todos os clubes com jogadores
        Imports imports = new Imports();
        IClub[] clubs = imports.importPlayersAndClub();

        JSONArray clubsJson = new JSONArray();
        for (IClub club : clubs) {
            if (club instanceof Club) {
                Club c = (Club) club;
                clubsJson.add(c.getJson());
            }
        }

        String clubsPath = SAVE_DIRECTORY + CLUBS_FILE;
        FileWriter writer = new FileWriter(clubsPath);
        try {
            writer.write(clubsJson.toJSONString());
            System.out.println("Todos os clubes exportados para: " + clubsPath);
        } catch (IOException e) {
            System.out.println("Erro ao exportar os clubes para o arquivo: " + clubsPath);
            throw e;
        } finally {
            writer.close();
        }
    }

    /**
     * Guarda uma liga
     */
    private void saveLeague(ILeague league) throws IOException {
        JSONObject leagueJson = ((League) league).getLeagueJson();

        String leaguePath = SAVE_DIRECTORY + league.getName()+ LEAGUE_FILE;
        FileWriter writer = new FileWriter(leaguePath);
        try {
            writer.write(leagueJson.toJSONString());
            System.out.println("Liga exportada para: " + leaguePath);
        } catch (IOException e) {
            System.out.println("Erro ao exportar a liga para o arquivo: " + leaguePath);
            throw e;
        } finally {
            writer.close();
        }
    }

    /**
     * Lista todos os jogos guardados
     */
    public String[] listSavedGames() {
        File directory = new File(SAVE_DIRECTORY);

        if (!directory.exists() || !directory.isDirectory()) {
            System.out.println("Diretório de saves não existe");
            return new String[0];
        }

        String[] files = directory.list();

        if (files == null || files.length == 0) {
            System.out.println("Não existem jogos guardados");
            return new String[0];
        }

        // Filtrar apenas ficheiros de liga
        String[] leagueFiles = new String[files.length];
        int count = 0;

        for (String file : files) {
            if (file.endsWith(LEAGUE_FILE)) {
                // Remover a extensão para mostrar apenas o nome da liga
                String leagueName = file.substring(0, file.length() - LEAGUE_FILE.length());
                leagueFiles[count++] = leagueName;
            }
        }

        // Criar array do tamanho correto
        String[] result = new String[count];
        for (int i = 0; i < count; i++) {
            result[i] = leagueFiles[i];
        }

        return result;
    }

}