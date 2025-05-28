package league;

import com.ppstudios.footballmanager.api.contracts.league.ILeague;
import com.ppstudios.footballmanager.api.contracts.league.ISeason;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

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

public class League implements ILeague {

    private static final int INITIAL_SEASONS = 2;
    private static final int INCREMENT_FACTOR = 2;
    private String leagueName;
    private ISeason[] seasons;
    private int numberOfSeasons;


    public League(String leagueName) {
        this.leagueName = leagueName;
        this.numberOfSeasons = 0;
        this.seasons = new Season[INITIAL_SEASONS];
    }

    /**
     * Retorna o nome da liga.
     *
     * @return o nome da liga como uma String.
     */
    @Override
    public String getName() {
        return this.leagueName;
    }

    /**
     * Retorna um array contendo todas as temporadas disponíveis na liga.
     * <p>
     * Este método cria um novo array contendo as temporadas atualmente armazenadas
     * na instância da liga. O array retornado é uma cópia do array interno.
     *
     * @return um array de objetos ISeason representando as temporadas da liga.
     */
    @Override
    public ISeason[] getSeasons() {
        ISeason[] seasonsTemp = new ISeason[this.numberOfSeasons];

        System.arraycopy(this.seasons, 0, seasonsTemp, 0, this.numberOfSeasons);

        return seasonsTemp;
    }

    /**
     * Aumenta o tamanho do array de temporadas (seasons) da liga.
     * <p>
     * Este método cria um novo array com um tamanho aumentado por um fator
     * pré-definido (INCREMENT_FACTOR) e copia os elementos do array existente
     * para o novo array. Após a cópia, o array interno da classe é substituído
     * pelo novo array ampliado.
     */
    private void incrementSizeToSeasons() {
        ISeason[] seasonsTemp = new Season[this.seasons.length * INCREMENT_FACTOR];

        System.arraycopy(this.seasons, 0, seasonsTemp, 0, this.seasons.length);

        this.seasons = seasonsTemp;
    }

    /**
     * Verifica se uma temporada especificada existe no array de temporadas da liga.
     *
     * @param iSeason a temporada que será verificada no array de temporadas.
     * @return true se a temporada especificada existir no array de temporadas,
     * false caso contrário.
     */
    private boolean seasonsExist(ISeason iSeason) {

        for (int i = 0; i < this.numberOfSeasons; i++) {
            if (this.seasons[i].equals(iSeason)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Cria uma nova temporada e adiciona ao array de temporadas da liga.
     * <p>
     * Este método verifica se a temporada fornecida não é nula e se não existe
     * no array de temporadas. Caso o array de temporadas esteja cheio, o método
     * aumenta dinamicamente o tamanho do array antes de adicionar a nova temporada.
     *
     * @param iSeason a temporada a ser adicionada à liga.
     * @return true se a temporada foi criada e adicionada com sucesso.
     * @throws IllegalArgumentException se a temporada for nula ou já existir no array.
     */
    @Override
    public boolean createSeason(ISeason iSeason) {
        if (iSeason == null) {
            throw new IllegalArgumentException("Season cannot be null");
        }

        if (seasonsExist(iSeason)) {
            throw new IllegalArgumentException("Season already exists");
        }

        if (this.numberOfSeasons == this.seasons.length) {
            incrementSizeToSeasons();
        }

        this.seasons[this.numberOfSeasons++] = iSeason;

        return true;
    }

    /**
     * Localiza o índice de uma temporada específica com base no ano fornecido.
     *
     * @param year o ano da temporada que deve ser localizada.
     * @return o índice da temporada correspondente ao ano, ou -1 se a temporada não for encontrada.
     */
    private int FindSeasonIndexByYear(int year) {

        for (int j = 0; j < this.numberOfSeasons; j++) {
            if (this.seasons[j].getYear() == year) {
                return j;
            }
        }

        return -1;

    }

    /**
     * Remove uma temporada da liga com base no ano especificado.
     * <p>
     * Este método localiza a temporada correspondente ao ano fornecido,
     * remove-a do array de temporadas e reorganiza os elementos restantes
     * Caso a temporada não seja encontrada,uma exceção é lançada.
     *
     * @param i o ano da temporada que deve ser removida.
     * @return a temporada removida como um objeto ISeason.
     * @throws IllegalArgumentException se a temporada não for encontrada.
     */
    @Override
    public ISeason removeSeason(int i) {

        int seasonIndex = FindSeasonIndexByYear(i);

        if (seasonIndex == -1) {
            throw new IllegalArgumentException("Season does not found");
        }

        ISeason season = this.seasons[seasonIndex];

        for (int j = seasonIndex; j < this.numberOfSeasons - 1; j++) {
            this.seasons[j] = this.seasons[j + 1];
        }
        this.seasons[--this.numberOfSeasons] = null;

        return season;

    }

    /**
     * Retorna a temporada correspondente ao ano especificado.
     * <p>
     * Este método devolve uma ISeason da liga através do ano
     *
     * @param year o ano da temporada que deve ser localizada.
     * @return a temporada correspondente ao ano especificado como um objeto ISeason.
     * @throws IllegalArgumentException se a temporada correspondente ao ano não for encontrada.
     */
    @Override
    public ISeason getSeason(int year) {

        int seasonIndex = FindSeasonIndexByYear(year);

        if (seasonIndex == -1) {
            throw new IllegalArgumentException("Season does not found");
        }

        return this.seasons[seasonIndex];
    }


    /**
     * Gera e retorna um array JSON contendo informações sobre todas as temporadas
     * armazenadas no objeto da liga. Cada temporada presente no array de temporadas
     * da liga é convertida para JSON e adicionada ao array.
     *
     * @return um JSONArray contendo representações em JSON de todas as temporadas
     * da liga. Caso não existam temporadas, retorna um JSONArray vazio.
     */
    private JSONArray getSeasonJson() {
        JSONArray seasonJson = new JSONArray();

        for (int i = 0; i < this.numberOfSeasons; i++) {
            if (this.seasons[i] != null) {
                seasonJson.add(((Season) this.seasons[i]).getSeasonJson());
            }
        }
        return seasonJson;
    }

    /**
     * Gera e retorna um objeto JSON contendo informações sobre a liga.
     * <p>
     * Este método cria um objeto JSON que inclui o nome da liga,
     * o número de temporadas e um array JSON representando as temporadas
     * associadas à liga.
     *
     * @return um objeto JSONObject contendo os detalhes da liga: nome da liga,
     * número de temporadas e informações sobre as temporadas. Caso não
     * existam temporadas, o array "seasons" será vazio.
     */
    public JSONObject getLeagueJson() {
        JSONObject leagueJson = new JSONObject();
        leagueJson.put("name", this.leagueName);
        leagueJson.put("numberOfSeasons", this.numberOfSeasons);
        leagueJson.put("seasons", this.getSeasonJson());
        return leagueJson;
    }


    /**
     * Exporta os dados da liga para um arquivo JSON.
     *
     * Este método gera um arquivo JSON que contém as informações da liga,
     * incluindo o nome, número de temporadas e detalhes das temporadas associadas.
     * O arquivo gerado é salvo no diretório "src/Files/saves/league" com o nome
     * da liga como identificação. Caso ocorra um erro ao salvar o arquivo,
     * uma mensagem será exibida e a exceção será lançada.
     *
     * @throws IOException se ocorrer um erro durante a escrita no arquivo.
     */
    @Override
    public void exportToJson() throws IOException {
        //cria o save file e dps chama o export do season e sempre assim em cascada fazendo append
        JSONObject leagueJson = getLeagueJson();

        String fileName = "src/Files/saves/league/" + this.leagueName + ".json";
        FileWriter fileWriter = new FileWriter(fileName);

        try {
            fileWriter.write(leagueJson.toJSONString());
            System.out.println("League exportado com sucesso para: " + fileName);
        } catch (IOException e) {
            System.out.println("Erro ao exportar a league para o arquivo: " + fileName);
        } finally {

            fileWriter.close();
        }

    }



}
