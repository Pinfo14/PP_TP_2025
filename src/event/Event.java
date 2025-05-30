package event;

import com.ppstudios.footballmanager.api.contracts.event.IEvent;
import org.json.simple.JSONObject;

/**
 * Nome: Emanuel Jose Teixeira Pinto
 * Número: 8230371
 * Turma: LEI1T1
 *
 * Nome: Roberto Cristiano Martins Faria
 * Número: 8230067
 * Turma: LEI1T2
 */

public abstract class Event implements IEvent {

    private String description;
    private int minute;

    public Event(String description, int minute) {
        this.description = description;
        this.minute = minute;
    }

    /**
     * Obtém a descrição do evento.
     *
     * @return Uma string representando a descrição do evento.
     */
    @Override
    public String getDescription() {
        return this.description;
    }

    /**
     * Obtém o minuto associado ao evento.
     *
     * @return o minuto (como um valor inteiro) associado ao evento.
     */
    @Override
    public int getMinute() {
        return this.minute;
    }



   /**
    * Gera uma representação do evento em formato JSON.
    *
    * @return um objeto JSONObject contendo todos os detalhes do evento,
    *         como tipo, minuto, descrição e outras informações específicas do evento.
    */
   public abstract JSONObject getEventJson();
    /**
     * Obtém o nome do evento associado.
     *
     * @return Uma string representando o nome do evento.
     */
    public abstract String getEventName();

    /**
     * Verifica se o objeto fornecido é igual a esta instância. Dois objetos são considerados
     * iguais se se o minuto e a descrição forem iguais.
     *
     * @param o o objeto a ser comparado com esta instância
     * @return true se o objeto fornecido for igual a esta instância, caso contrário false
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Event)) {
            return false;
        }
        Event ev = (Event) o;
        return this.minute == ev.getMinute() && this.description.equals(ev.getDescription());
    }

    /**
     * Retorna uma representação textual do evento, contendo a descrição
     * e o minuto em que ocorreu.
     *
     * @return uma string que combina a descrição do evento e o minuto em formato legível.
     */
    @Override
    public String toString() {
        return  this.description + " ao minuto " + this.minute;
    }
}
