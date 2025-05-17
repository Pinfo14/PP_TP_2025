package simulation;

import com.ppstudios.footballmanager.api.contracts.event.IEvent;
import com.ppstudios.footballmanager.api.contracts.player.IPlayer;
import player.Player;
import event.*;

public class EventFactory {


    public IEvent generatePassEvent(IPlayer autor, IPlayer receptor, int minuto, boolean isHome) {
        String desc;

        int vision   = autor.getPassing();
        int recive = receptor.getPassing();
        double prob  = (double) vision / (vision + recive);

        boolean sucesso = Math.random() < prob;
        if (!sucesso) {
            return null;  // passe falhou: não gera evento
        }
         desc = (isHome ? "[Casa] " : "[Fora] ")
                + "Passe bem-sucedido de " + autor.getName()
                + " para " + receptor.getName();
        return new PassEvent(desc,minuto);
    }




    public IEvent generateShotEvent(IPlayer autor,IPlayer gk ,int minute, boolean isHome) {
        Player goolKepeer = (Player) gk;
        String desc;

        int shooting= autor.getShooting();
        int defence = goolKepeer.getDefence();
        double prob = (double) shooting / (shooting+defence);

        boolean sucesso = Math.random() < prob;
        if (!sucesso) {
             desc = (isHome ? "[Casa] " : "[Fora] ")
                    + "Remate defendido por " + goolKepeer.getName();
             return new GoalKickEvent(minute,desc);
        }

        desc = "GOLOOO para equipa da "+ (isHome?"casa":"fora");
        return new GoalEvent(autor,minute,desc);
    }


    /**
     * Gera um evento de falta.
     * Retorna FoulEvent se a falta for assinalada,
     * ou null se for perdoada.
     *
     * @param maker jogador que comete a falta
     * @param victim   jogador sobre quem a falta é cometida
     * @param min   minuto do jogo
     * @param isHome   se a equipa é a da casa
     * @return FaltaEvent ou null
     */
    public IEvent generateFoulEvent(IPlayer maker, IPlayer victim, int min, boolean isHome) {

        int velocidadeInfrator = maker.getSpeed();
        int velocidadeVitima   = victim.getSpeed();
        double probFalta = (double) velocidadeInfrator
                / (velocidadeInfrator + velocidadeVitima);

        boolean marcada = Math.random() < probFalta;
        if (!marcada) {
            return null;  // falta perdoada: não gera evento
        }

        String desc = (isHome ? "[Casa] " : "[Fora] ")
                + "Falta de " + maker.getName()
                + " sobre " + victim.getName()
                + " (assinalada)";
        return new FoulEvent(desc , min);
    }


}
