package imports;

import com.ppstudios.footballmanager.api.contracts.event.IEvent;
import com.ppstudios.footballmanager.api.contracts.player.IPlayer;
import com.ppstudios.footballmanager.api.contracts.team.IClub;
import event.*;
import match.Match;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import static imports.ImportUtils.*;


public class EventImporter {



    /**
     * Carrega os eventos associados a uma partida a partir de um objeto JSON fornecido.
     *
     * O método verifica se a chave "events" está presente no JSON da partida e, se presente,
     * percorre a lista de eventos para criar e associar instâncias de eventos à partida.
     * Caso a chave "events" não esteja presente, o método termina sem realizar alterações.
     *
     * @param match Objeto da partida à qual os eventos serão associados.
     * @param matchJson Objeto JSON que contém os dados da partida, incluindo eventos.
     */
    public static void loadEventsForMatch(Match match, JSONObject matchJson,IClub[] allClubs) {
        if (!matchJson.containsKey("events")) {
            return;
        }

        JSONObject eventsJson = (JSONObject) matchJson.get("events");
        JSONArray eventsArray = (JSONArray) eventsJson.get("events");

        for (Object eventObj : eventsArray) {
            JSONObject eventJson = (JSONObject) eventObj;

            IEvent event = createEventFromJson(eventJson,allClubs);
            if (event != null) {
                match.addEvent(event);
            }
        }
    }

    /**
     * Cria um evento a partir de um objeto JSON.
     * O evento é interpretado com base no tipo fornecido no JSON ("Goal", "Foul", "PassEvent",
     * "GoalKick" ou "HalftimeEvent"). Caso o tipo seja válido e todas as informações necessárias
     * estejam presentes e corretas, o correspondente IEvent é criado.
     * Caso contrário, retorna null.
     *
     * @param eventJson Objeto JSON contendo os dados do evento, incluindo tipo, minuto,
     *                  descrição e outras informações necessárias dependendo do tipo
     *                  do evento.
     * @return Instância de IEvent correspondente ao tipo de evento e dados fornecidos,
     *         ou null se o tipo for desconhecido ou houver erro na criação.
     */
    private static IEvent createEventFromJson(JSONObject eventJson, IClub[] allClubs) {
        String type = (String) eventJson.get("type");
        int minute = ((Long) eventJson.get("minute")).intValue();
        String description = (String) eventJson.get("description");

        switch (type) {
            case "Goal":
                String autorName = (String) eventJson.get("autor");
                IPlayer player = ImportUtils.findPlayerByName(allClubs, autorName);
                if (player != null) {
                    return new GoalEvent(player, minute, description);
                }
                break;

            case "Foul":
                String autorFoul = (String) eventJson.get("autor");
                String victimName = (String) eventJson.get("victim");
                IPlayer autor = ImportUtils.findPlayerByName(allClubs, autorFoul);
                IPlayer victim =ImportUtils.findPlayerByName(allClubs, victimName);
                if (autor != null && victim != null) {
                    return new FoulEvent(description, minute, autor, victim);
                }
                break;

            case "PassEvent":
                String autorPass = (String) eventJson.get("autor");
                IPlayer autorPlayer = ImportUtils.findPlayerByName(allClubs, autorPass);
                if (autorPlayer != null) {
                    return new PassEvent(description, minute, autorPlayer);
                }
                break;

            case "GoalKick":
                String autorKick = (String) eventJson.get("autor");
                IPlayer kickPlayer = ImportUtils.findPlayerByName(allClubs, autorKick);
                if (kickPlayer != null) {
                    return new GoalKickEvent(minute, description, kickPlayer);
                }
                break;

            case "ShotEvent":
                String autorShot = (String) eventJson.get("autor");
                IPlayer shotPlayer = ImportUtils.findPlayerByName(allClubs, autorShot);
                if (shotPlayer != null) {
                    return new ShotEvent(description,minute ,shotPlayer );
                }
                break;

            case "HalftimeEvent":
                return new HalftimeEvent(minute);

            default:
                logger.writeLog("Tipo de evento desconhecido: " + type);
                break;
        }
        return null;
    }

}
