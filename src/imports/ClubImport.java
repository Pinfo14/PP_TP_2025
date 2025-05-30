package imports;

import com.ppstudios.footballmanager.api.contracts.player.IPlayerPosition;
import com.ppstudios.footballmanager.api.contracts.player.PreferredFoot;
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

public class ClubImport {
    /**
     * Importa todos os clubes disponíveis
     */
    public static IClub[] importAllClubs() {
        // Primeiro tentar carregar do ficheiro de save
        File clubsFile = new File(SAVE_DIRECTORY + CLUBS_FILE);

        if (clubsFile.exists()) {
            try {
                return importClubsFromSaveFile();
            } catch (Exception e) {
                System.out.println("Erro ao carregar clubes do save, usando dados default: " + e.getMessage());
            }
        }

        // Fallback para dados default
        System.out.println("A carregar clubes dos saves default...");
        Imports defaultImports = new Imports();
        return defaultImports.importPlayersAndClub();
    }

    /**
     * Importa clubes do ficheiro de save
     */
    private static IClub[] importClubsFromSaveFile() throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        FileReader reader = new FileReader(SAVE_DIRECTORY + CLUBS_FILE);
        JSONArray clubsArray = (JSONArray) parser.parse(reader);
        reader.close();

        IClub[] clubs = new Club[clubsArray.size()];

        for (int i = 0; i < clubsArray.size(); i++) {
            JSONObject clubJson = (JSONObject) clubsArray.get(i);
            clubs[i] = createClubFromJson(clubJson);
        }

        return clubs;
    }

    /**
     * Cria um clube a partir do JSON
     */
    private static IClub createClubFromJson(JSONObject clubJson) {
        String name = (String) clubJson.get("name");
        String code = (String) clubJson.get("code");
        String country = (String) clubJson.get("country");
        int foundedYear = ((Long) clubJson.get("foundedYear")).intValue();
        String stadiumName = (String) clubJson.get("stadiumName");
        String logo = (String) clubJson.get("logo");
        int maxPlayers =ImportUtils.getIntValue(clubJson, "maxPlayers", 40);

        Club club = new Club(name, code, country, foundedYear, stadiumName, logo, maxPlayers);

        // Adicionar jogadores se existirem
        if (clubJson.containsKey("players")) {
            JSONArray playersArray = (JSONArray) clubJson.get("players");
            addPlayersToClub(club, playersArray);
        }

        return club;
    }

    /**
     * Adiciona jogadores a um clube
     */
    private static void addPlayersToClub(Club club, JSONArray playersArray) {
        for (Object playerObj : playersArray) {
            JSONObject playerJson = (JSONObject) playerObj;

            try {
                Player player = createPlayerFromJson(playerJson);
                club.addPlayer(player);
            } catch (Exception e) {
                System.out.println("Erro ao adicionar jogador ao clube " + club.getCode() + ": " + e.getMessage());
            }
        }
    }

    /**
     * Cria um jogador a partir do JSON
     */
    private static Player createPlayerFromJson(JSONObject playerJson) {
        String name = (String) playerJson.get("name");
        LocalDate birthDate = LocalDate.parse((String) playerJson.get("birthDate"));
        String nationality = (String) playerJson.get("nationality");
        String photo = (String) playerJson.get("photo");
        int number = ImportUtils.getIntValue(playerJson, "number", 0);

        String positionStr = (String) playerJson.get("position");
        IPlayerPosition position = new PlayerPosition(positionStr);

        PlayerAttributes attributes = createAttributesFromJson(playerJson, positionStr);

        return new Player(name, birthDate, nationality, position, photo, number, attributes);
    }

    /**
     * Cria atributos de jogador a partir do JSON
     */
    private static PlayerAttributes createAttributesFromJson(JSONObject playerJson, String position) {
        if (!playerJson.containsKey("attributes")) {
            // Gerar atributos default
            PlayerAttributes attr = new PlayerAttributes();
            return attr.generateAttributes(position);
        }

        JSONObject attributesJson = (JSONObject) playerJson.get("attributes");

        int shooting = ImportUtils.getIntValue(attributesJson, "shooting", 50);
        int passing = ImportUtils.getIntValue(attributesJson, "passing", 50);
        int stamina = ImportUtils.getIntValue(attributesJson, "stamina", 50);
        int speed = ImportUtils.getIntValue(attributesJson, "speed", 50);
        int defence = ImportUtils.getIntValue(attributesJson, "defence", 50);

        float height = ImportUtils.getFloatValue(attributesJson, "height", 1.80f);
        float weight =ImportUtils.getFloatValue(attributesJson, "weight", 75.0f);

        PreferredFoot preferredFoot = PreferredFoot.Right;
        if (attributesJson.containsKey("preferredFoot")) {
            String footStr = (String) attributesJson.get("preferredFoot");
            try {
                preferredFoot = PreferredFoot.fromString(footStr);
            } catch (Exception e) {
                // Manter default se houver erro
            }
        }

        return new PlayerAttributes(shooting, passing, stamina, speed, height, weight, defence, preferredFoot);
    }

}
