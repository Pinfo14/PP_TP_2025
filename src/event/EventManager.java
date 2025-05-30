package event;

import com.ppstudios.footballmanager.api.contracts.data.IExporter;
import com.ppstudios.footballmanager.api.contracts.event.IEvent;
import com.ppstudios.footballmanager.api.contracts.event.IEventManager;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;

/**
 * Nome: Emanuel Jose Teixeira Pinto
 * Número: 8230371
 * Turma: LEI1T1
 *
 * Nome: Roberto Cristiano Martins Faria
 * Número: 8230067
 * Turma: LEI1T2
 */


public class EventManager implements IEventManager, IExporter {

    private static final int INIT_CAP=10;
    private static final int INCREMENT=2;

    private IEvent[] events;
    private int eventCount;

    public EventManager() {
        this.events = new IEvent[INIT_CAP];
        this.eventCount = 0;
    }


    /**
     * Adiciona um evento ao manager de eventos.
     * O evento só será adicionado se não for nulo, não existir previamente no array de eventos,
     * e houver espaço suficiente para armazená-lo.
     *
     * @param iEvent o evento que será adicionado. Não pode ser null, caso contrário,
     *              será lançada uma exceção IllegalArgumentException.
     *               Caso o evento já exista (mesma descrição e minuto), será lançada uma exceção
     *                IllegalStateException.
     */
    @Override
    public void addEvent(IEvent iEvent) {
        if(iEvent == null) {
            throw new IllegalArgumentException("Event nao pode ser null");
        }
        if(isInEvent(iEvent)) {
            throw new IllegalStateException("Event "+ iEvent.getDescription()+" ao minuto "+iEvent.getMinute()+" ja existe");
        }
        if(this.eventCount == this.events.length) {
            increaseCapacity();
        }
        this.events[this.eventCount++] = iEvent;
    }

    /**
     * Retorna uma lista com todos os eventos armazenados atualmente.
     *
     * @return um array de objetos do tipo IEvent representando todos os eventos armazenados.
     */
    @Override
    public IEvent[] getEvents() {
        IEvent[] eventsTemp = new IEvent[this.eventCount];
        for(int i = 0; i < this.eventCount; i++) {
            eventsTemp[i] = this.events[i];
        }
        return eventsTemp;
    }

    /**
     * Obtém o número total de eventos registados.
     *
     * @return o número total de eventos como um valor inteiro.
     */
    @Override
    public int getEventCount() {
        return this.eventCount;
    }

    /**
     * Retorna uma representação textual deste objeto que inclui o número de eventos
     * e os detalhes de cada evento armazenado na classe.
     *
     * @return uma string representando o estado atual dos eventos
     */
    @Override
    public String toString() {
        String s = "\n";
        s += "Numero de eventos: " + this.eventCount + "\n";
        s += eventToString();
        return s;
    }


    /**
     * Converte todos os objetos de eventos armazenados numa String.
     *
     * @return Uma string que contém a representação textual de todos os eventos não nulos
     *
     */
    private String eventToString() {
     String s = "";
      for(int i = 0; i < this.eventCount; i++) {
          if(this.events[i] != null) {
              s += this.events[i].toString() + "\n";
          }
      }
      return s;
    }

    /**
     * Verifica se o evento fornecido está presente na lista de eventos armazenados.
     *
     * @param event o evento a ser verificado
     * @return true se o evento está presente na lista de eventos, caso contrário false
     */
    private boolean isInEvent(IEvent event) {
       for(int i = 0; i < this.eventCount; i++) {
           if(this.events[i].equals(event)) {
               return true;
           }
       }
       return false;
    }

    /**
     * Aumenta a capacidade do array interno que armazena os eventos.
     *
     * Este método cria um novo array com uma capacidade maior determinada
     * pelo incremento definido (INCREMENT). Os elementos existentes no array atual são
     * copiados para o novo array, e o array interno é redefinido para o novo array.
     *
     * O método é utilizado para assegurar que o armazenamento de eventos
     * na classe não atinge o limite à medida que novos eventos são adicionados.
     */
    private void increaseCapacity() {
        IEvent[] temp = new IEvent[this.events.length * INCREMENT];
        for(int i = 0; i < this.eventCount; i++) {
            temp[i] = this.events[i];
        }
        this.events = temp;
    }

    /**
     * Gera um array JSON contendo a representação de todos os eventos existentes.
     * Apenas os objetos do tipo {@link Event} serão incluídos na lista.
     *
     * @return Um objeto JSONArray que representa todos os eventos armazenados,
     *         onde cada elemento do array será um JSON de um evento específico.
     */
    private JSONArray getAllEventsJson(){
        JSONArray jsonArray = new JSONArray();

        for (int i = 0; i < this.eventCount; i++) {
            if(this.events[i] != null && this.events[i] instanceof Event) {
                jsonArray.add(((Event)this.events[i]).getEventJson());
            }
        }
        return jsonArray;
    }

    /**
     * Gera uma representação em JSON do estado atual do sistema de eventos.
     *
     * @return Um objeto JSONObject contendo dois campos: "eventCount", que representa
     *         o número total de eventos, e "events", que contém um JSONArray com os
     *         detalhes de todos os eventos registrados.
     */
    public JSONObject getEventJson(){
        JSONObject object = new JSONObject();
        object.put("eventCount", this.eventCount);
        object.put("events", getAllEventsJson());
        return object;
    }


    /**
     * Exporta os eventos do gerenciador para um arquivo JSON.
     *
     * Este método cria um arquivo JSON contendo os eventos gerenciados pela
     * instância. O arquivo será salvo no local especificado com o nome
     * "EventManager_<data_atual>.json", onde <data_atual> é a data no formato
     * AAAA-MM-DD.
     *
     * O processo de exportação inclui a geração de um objeto JSON contendo
     * as informações sobre os eventos e a escrita desse conteúdo no arquivo.
     * No final do processo, uma mensagem de sucesso será exibida no console,
     * indicando a localização do arquivo gerado. Em caso de erro, será exibida
     * uma mensagem de erro no console.
     *
     * @throws IOException se ocorrer um erro ao criar ou escrever no arquivo.
     */
    @Override
    public void exportToJson() throws IOException {
        String fileName = "src/Files/saves/events/EventManager_"+LocalDate.now()+".json";
        File file = new File(fileName);
    file.createNewFile();
        JSONObject object =  getEventJson();

        FileWriter fileWriter = new FileWriter(file);
        try {
            fileWriter.write(object.toJSONString());
            System.out.println("All Event exportado com sucesso para: " + fileName);
        }catch (IOException e){
            System.out.println("Erro ao exportar o All event para o arquivo: " + fileName);
        }finally {
            fileWriter.close();
        }

    }
}
