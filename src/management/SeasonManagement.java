package management;

import com.ppstudios.footballmanager.api.contracts.league.ISeason;
import com.ppstudios.footballmanager.api.contracts.team.IClub;
import imports.Imports;
import league.Season;
import menus.ListClub;
import menus.ListMatches;
import menus.ListStanding;
import menus.SeasonMenu;
import reader.Reader;
import util.Utils;

public class SeasonManagement {

    private IClub[] clubesLoaded ;

    private boolean defaultData;


    public SeasonManagement() {
        this.defaultData = false;
        loadDefaultClubs();
    }
    public SeasonManagement(boolean useDefaultData) {
       if(useDefaultData){
          loadDefaultClubs();
       }else {
           loadSavedClubs();
       }
    }


    private void loadDefaultClubs() {
        System.out.println("A carregar dados default...");
        Imports imports = new Imports();
       this.clubesLoaded = imports.importPlayersAndClub();

    }

    private void loadSavedClubs() {
        System.out.println("A carregar jogo guardado...");
        // Aqui implementarias o loading dos saves

        // TODO: Implementar loading real dos saves

    }


    private int countClubsLoded() {
        int count = 0;
        for (IClub club : clubesLoaded ) {
            if(club != null) {
                count++;
            }
        }
        return count;
    }

    public void run(ISeason iSeason) {
        Reader reader = new Reader();
        Season season = validateAndCastSeason(iSeason);
        FormationManagement formations = new FormationManagement();

        int option;
        do {
            displayMainMenu(season);
            option = reader.readInt(0, 7, "Selecione uma opção: ");
            processMainMenuOption(option, season, formations, reader);


        } while (option != 0);
    }

    /**
     * Valida e converte ISeason para Season
     */
    private Season validateAndCastSeason(ISeason iSeason) {
        if (!(iSeason instanceof Season)) {
            throw new IllegalArgumentException("A época deve ser do tipo Season");
        }
        return (Season) iSeason;
    }

    /**
     * Apresenta o menu principal da época
     */
    private void displayMainMenu(Season season) {
        SeasonMenu.mainSeasonMenu(
                season.getYear(),
                season.getNameCoachingClub(),
                season.getName(),
                season.getCurrentRound()
        );
    }

    /**
     * Processa a opção selecionada no menu principal
     */
    private void processMainMenuOption(int option, Season season, FormationManagement formations, Reader reader) {
        switch (option) {
            case 1:
                handleTeamManagement(season, reader);
                break;
            case 2:
                handleCoachSelection(season, reader);
                break;
            case 3:
                handleNextRound(season, formations);
                break;
            case 4:
                handleListMatches(season);
                break;
            case 5:
                handleListResults();
                break;
            case 6:
                handleListStandings(season);
                break;
            case 7:
                handleSimulateRestOfSeason();
                break;
            case 0:

            default:
                // Opção inválida ou sair (0)
                break;
        }
    }

    /**
     * Gere a funcionalidade de gestão de equipas (adicionar/remover/listar)
     */
    private void handleTeamManagement(Season season, Reader reader) {
        int optionTemp;
        do {
            displayTeamManagementMenu(season);
            optionTemp = reader.readInt(0, 3, "Selecione uma opção: ");
            processTeamManagementOption(optionTemp, season, reader);
        } while (optionTemp != 0);
    }

    /**
     * Apresenta o menu de gestão de equipas
     */
    private void displayTeamManagementMenu(Season season) {
        SeasonMenu.managementSeasonMenu(
                season.getYear(),
                season.getNameCoachingClub(),
                season.getName(),
                season.getCurrentRound()
        );
    }

    /**
     * Processa as opções do menu de gestão de equipas
     */
    private void processTeamManagementOption(int option, Season season, Reader reader) {
        switch (option) {
            case 1:
                addClubToSeason(season, reader);
                break;
            case 2:
                removeClubFromSeason(season, reader);
                break;
            case 3:
                listClubsInSeason(season);
                break;
            default:
                // Voltar (0) ou opção inválida
                break;
        }
    }

    /**
     * Adiciona um clube à época
     */
    private void addClubToSeason(Season season, Reader reader) {
        ListClub.listClubLoaded(clubesLoaded);
        int clubId = reader.readInt(1, countClubsLoded(), "Insira o ID do Clube: ");

        try {
            season.addClub(clubesLoaded[clubId - 1]);
            System.out.println("Clube adicionado com sucesso.");
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("Erro ao adicionar clube: " + e.getMessage());
        }
    }

    /**
     * Remove um clube da época
     */
    private void removeClubFromSeason(Season season, Reader reader) {
        try {
            IClub[] clubes = season.getCurrentClubs();
            if (clubes == null || clubes.length == 0) {
                System.out.println("Nenhum clube disponível para remover.");
                return;
            }

            ListClub.listClubLoaded(clubes);
            int clubId = reader.readInt(1, clubes.length, "Insira o ID do Clube: ");
            season.removeClub(clubes[clubId - 1]);
            System.out.println("Clube removido com sucesso.");

        } catch (IllegalArgumentException | IllegalStateException | NullPointerException e) {
            System.out.println("Erro ao remover clube: " + e.getMessage());
        }
    }

    /**
     * Lista os clubes presentes na época
     */
    private void listClubsInSeason(Season season) {
        ListClub.listClubLoaded(season.getCurrentClubs());
        Utils.waitEnter();
    }

    /**
     * Gere a seleção do clube para treinar
     */
    private void handleCoachSelection(Season season, Reader reader) {
        IClub[] clubes = season.getCurrentClubs();
        if (clubes == null || clubes.length == 0) {
            System.out.println("Não existem clubes para treinar.");
            return;
        }

        ListClub.listClubLoaded(clubes);
        System.out.println("Selecione um clube para treinar.");
        int clubId = reader.readInt(1, season.getNumberOfCurrentTeams(), "Insira o ID do Clube para treinar: ");
        season.setCoachingClubIndex(clubId - 1);
        System.out.println("Clube selecionado para treinar: " + clubes[clubId - 1].getName());
    }

    /**
     * Gere a próxima jornada
     */
    private void handleNextRound(Season season, FormationManagement formations) {
        if (!validateCoachingSetup(season)) {
            System.out.println("Selecione um clube para treinar antes de avançar para a próxima jornada.");
            return;
        }

        RoundManagement roundManagement = new RoundManagement();
        roundManagement.run(season, formations);
    }

    /**
     * Valida se existe um clube selecionado para treinar
     */
    private boolean validateCoachingSetup(Season season) {
        return season.getCoachingClubIndex() != -1 && season.getNumberOfCurrentTeams() > 0;
    }

    /**
     * Lista todos os jogos da época
     */
    private void handleListMatches(Season season) {
        try {
            ListMatches.listMatches(season.getMatches());
            Utils.waitEnter();
        } catch (Exception e) {
            System.out.println("Erro ao listar jogos: " + e.getMessage());
        }
    }

    /**
     * Lista os resultados dos jogos (a implementar)
     */
    private void handleListResults() {
        System.out.println("Funcionalidade ainda não implementada.");
        Utils.waitEnter();
    }

    /**
     * Lista a classificação da liga
     */
    private void handleListStandings(Season season) {
        try {
            ListStanding.list(season.getLeagueStandings());
            Utils.waitEnter();
        } catch (Exception e) {
            System.out.println("Erro ao listar classificação: " + e.getMessage());
        }
    }

    /**
     * Simula o restante da época (a implementar)
     */
    private void handleSimulateRestOfSeason() {
        System.out.println("Funcionalidade ainda não implementada.");
        Utils.waitEnter();
    }



}
