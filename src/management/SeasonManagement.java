package management;

import com.ppstudios.footballmanager.api.contracts.league.ISeason;
import com.ppstudios.footballmanager.api.contracts.team.IClub;
import imports.Imports;
import menus.ListClub;
import menus.SeasonMenu;
import reader.Reader;

public class SeasonManagement {

    private IClub[] clubesLoaded ;
    private ISeason season;

    public SeasonManagement() {
        Imports imports = new Imports();
        clubesLoaded  = imports.importClubs();
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


    public void run(ISeason season) {
        int option;
        Reader reader = new Reader();

        do {
            // ATENÇÃO: Troque "GGGGG" pelo nome correto da competição ou deixe um TODO
            SeasonMenu.mainSeasonMenu(season.getYear(), "GGGGG", season.getName());
            option = reader.readInt(0, 2, "Selecione uma opção: ");
            switch (option) {
                case 1:
                    int optionTemp, readerTemp;
                    SeasonMenu.managementTeamsMenu(season.getYear(), "GGGGG", season.getName());
                    do {
                        optionTemp = reader.readInt(0, 6, "Selecione uma opção: ");
                        switch (optionTemp) {

                            case 1:
                                ListClub.listClubLoaded(clubesLoaded );
                                readerTemp = reader.readInt(1, countClubsLoded(), "\nInsira o ID do Clube: ");
                                try {
                                    season.addClub(clubesLoaded [readerTemp - 1]);
                                } catch (IllegalArgumentException | IllegalStateException e) {
                                    System.out.println(e.getMessage());
                                }
                                break;
                            case 2:
                                try {
                                    season.getCurrentClubs();

                                    ListClub.listClubLoaded(season.getCurrentClubs());
                                    readerTemp = reader.readInt(1, season.getNumberOfCurrentTeams(), "\nInsira o ID do Clube: ");
                                    try {
                                        season.removeClub(season.getCurrentClubs()[readerTemp - 1]);
                                    } catch (IllegalArgumentException | IllegalStateException e) {
                                        System.out.println(e.getMessage());
                                    }
                                }catch (IllegalArgumentException | IllegalStateException e) {
                                    System.out.println(e.getMessage());
                                }

                                break;
                            default:
                                ListClub.listClubLoaded(season.getCurrentClubs());
                                break;
                        }
                    } while (optionTemp != 0);
                    break;

                case 2:
                    // TODO: Implementar lógica para selecionar o clube para treinar
                    break;

                default:
                    // TODO: Implementar lógica para importar dados salvos, se necessário
                    break;
            }
        } while (option != 0);
    }
}
