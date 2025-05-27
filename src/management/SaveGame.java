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

        String leaguePath = SAVE_DIRECTORY +league.getName()+LEAGUE_FILE;
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



}