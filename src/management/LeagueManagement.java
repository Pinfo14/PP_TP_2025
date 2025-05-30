package management;

import com.ppstudios.footballmanager.api.contracts.league.ILeague;
import com.ppstudios.footballmanager.api.contracts.league.ISeason;
import imports.ImportSaveGame;
import league.League;
import league.Season;
import reader.Reader;


import static imports.ImportUtils.listAvailableLeagues;


public class LeagueManagement {

    private League league;// Inicializado


    public void startNewGame(boolean useDefaultData) {

        Reader reader = new Reader();
        int year;
        String leagueName;
        ISeason season;

        initialMessage();

        leagueName = reader.readString("Insira o nome da liga: ");
        year = reader.readInt(2020, 2040, "Insira o ano que pretende iniciar (entre 2020 e 2040): ");


        league = new League(leagueName);
        season = new Season(leagueName, year);
        league.createSeason(season);

        if (useDefaultData) {
            SeasonManagement seasonManagement = new SeasonManagement(true);
            seasonManagement.run(league.getSeason(year));
        }
    }

    public void loadGame() {

        String[] availableLeagues = listAvailableLeagues();

        if (availableLeagues.length == 0) {
            System.out.println("\n=== NENHUM JOGO SALVO ENCONTRADO ===");
            System.out.println("Para carregar um jogo, coloque o ficheiro JSON na pasta:");
            System.out.println("src/Files/SaveGames/[nome_da_liga]_league.json");
            System.out.println("\nPor exemplo: src/Files/SaveGames/ola_league.json");
            return;
        }

        // Mostrar saves disponíveis
        System.out.println("\n=== JOGOS SALVOS DISPONÍVEIS ===");
        for (int i = 0; i < availableLeagues.length; i++) {
            System.out.println((i + 1) + " - " + availableLeagues[i]);
        }

        Reader reader = new Reader();
        int choice = reader.readInt(1, availableLeagues.length, "Escolha o jogo a carregar: ");

        String selectedLeague = availableLeagues[choice - 1];

        try {
            System.out.println("\nCarregando liga: " + selectedLeague + "...");

            ImportSaveGame importSaveGame = new ImportSaveGame();
            ILeague loadedLeague = importSaveGame.importLeague(selectedLeague);

            if (loadedLeague == null) {
                System.out.println("ERRO: Não foi possível carregar a liga!");
                return;
            }


            this.league = (League) loadedLeague;


            ISeason[] seasons = verifySeasons();

            // Usar a última temporada (mais recente)
            ISeason currentSeason = seasons[seasons.length - 1];

            System.out.println("\n=== LIGA CARREGADA COM SUCESSO ===");
            System.out.println("Liga: " + league.getName());
            System.out.println("Temporadas encontradas: " + seasons.length);
            System.out.println("Temporada atual: " + currentSeason.getName() + " (" + currentSeason.getYear() + ")");
            System.out.println("Clubes na temporada: " + currentSeason.getNumberOfCurrentTeams());

            // Iniciar gestão da temporada
            SeasonManagement seasonManagement = new SeasonManagement(true);
            seasonManagement.run(currentSeason);

        } catch (Exception e) {
            System.out.println("ERRO ao carregar o jogo: " + e.getMessage());
            System.out.println("Verifique se o ficheiro JSON está correto.");
        }
    }

    private ISeason[] verifySeasons() {
        // Obter temporadas da liga
        ISeason[] seasons = this.league.getSeasons();
        if (seasons.length == 0) {
            throw new IllegalStateException("Nao existem seasons na liga");
        }
        return seasons;

    }

    public ILeague getLeague() {
        return this.league;
    }

    private void initialMessage() {
        StringBuilder sb = new StringBuilder();

        sb.append("\n");
        sb.append("Bem-vindo ao PP Soccer Manager. Para iniciar um novo jogo tem de indicar o ano em que\n");
        sb.append("pretende começar. Também tem de colocar o nome da sua Liga.\n");
        sb.append("Após indicar o nome e o ano, será redirecionado para a página de gestão da sua liga.\n");

        System.out.println(sb);
    }


}