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
       this.clubesLoaded = imports.importPlayersToClub();

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
        int option, optionTemp, readerTemp;
        Reader reader = new Reader();

        Season season = null;
        if(iSeason instanceof Season) {
            season = (Season) iSeason;
        }

        do {
            SeasonMenu.mainSeasonMenu(season.getYear(), season.getNameCoachingClub(), season.getName(), season.getCurrentRound());
            option = reader.readInt(0, 7, "Selecione uma opção: ");
            switch (option) {

                case 1:
                    do {
                        SeasonMenu.managementSeasonMenu(season.getYear(), season.getNameCoachingClub(), season.getName(), season.getCurrentRound());
                        optionTemp = reader.readInt(0, 3, "Selecione uma opção: ");
                        switch (optionTemp) {

                            case 1:
                                ListClub.listClubLoaded(clubesLoaded );
                                readerTemp = reader.readInt(1, countClubsLoded(), "Insira o ID do Clube: ");
                                try {
                                    //clonar o objecto quando passamos para aqui ?
                                    season.addClub(clubesLoaded [readerTemp - 1]);
                                    System.out.println("Clube adicionado com sucesso.");
                                } catch (IllegalArgumentException | IllegalStateException e) {
                                    System.out.println(e.getMessage());
                                }
                                break;
                            case 2:
                                try {
                                    IClub[] clubes = season.getCurrentClubs();
                                    if (clubes == null || clubes.length == 0) {
                                        System.out.println("Nenhum clube disponível para remover.");
                                        break;
                                    }
                                    ListClub.listClubLoaded(clubes);

                                    readerTemp = reader.readInt(1, clubes.length, "Insira o ID do Clube: ");
                                    season.removeClub(clubes[readerTemp - 1]);
                                    System.out.println("Clube removido com sucesso.");
                                } catch (IllegalArgumentException | IllegalStateException | NullPointerException e) {
                                    System.out.println(e.getMessage());
                                }
                                break;
                            case 3:
                                ListClub.listClubLoaded(season.getCurrentClubs());
                                Utils.waitEnter();
                                break;
                        }
                    } while (optionTemp != 0);
                    break;

                case 2:
                    IClub[] clube = season.getCurrentClubs();
                    if (clube == null || clube.length == 0) {
                        System.out.println("Não existem clubes para treinar.");
                        break;
                    }
                    ListClub.listClubLoaded(season.getCurrentClubs());
                    System.out.println("Selecione um clube para treinar.");
                    optionTemp = reader.readInt(1,season.getNumberOfCurrentTeams(), "Insira o ID do Clube para treinar: ");
                    season.setCoachingClubIndex(optionTemp-1);
                    break;

                case 3:
                    if(season.getCoachingClubIndex() != -1 && season.getNumberOfCurrentTeams() > 0) {
                        RoundManagement roundManagement = new RoundManagement();
                        System.out.println(season.getCurrentRound());
                        roundManagement.run(season);

                    }else{
                        System.out.println("Selecione um clube para treinar.");
                    }
                    break;
                case 4:
                    ListMatches.listMatches(season.getMatches());
                    Utils.waitEnter();
                case 5:
                    break;
                case 6:
                    ListStanding.list(season.getLeagueStandings());
                    Utils.waitEnter();




                default:
                    // TODO: Implementar lógica para salvar dados, se necessário
                    break;
            }
            SaveGame saveGame = new SaveGame();

        } while (option != 0);
    }

}
