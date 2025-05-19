package management;

import com.ppstudios.footballmanager.api.contracts.league.ISeason;
import com.ppstudios.footballmanager.api.contracts.team.IClub;
import imports.Imports;
import league.Season;
import menus.ListClub;
import menus.SeasonMenu;
import reader.Reader;
import util.Utils;

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
        int option, optionTemp, readerTemp;
        Reader reader = new Reader();

        do {
            // ATENÇÃO: Troque "GGGGG" pelo nome correto da competição ou deixe um TODO
            SeasonMenu.mainSeasonMenu(season.getYear(), "GGGGG", season.getName());
            option = reader.readInt(0, 2, "Selecione uma opção: ");
            switch (option) {

                case 1:
                    do {
                        SeasonMenu.managementSeasonMenu(season.getYear(), "GGGGG", season.getName());
                        optionTemp = reader.readInt(0, 6, "Selecione uma opção: ");
                        switch (optionTemp) {

                            case 1:
                                ListClub.listClubLoaded(clubesLoaded );
                                readerTemp = reader.readInt(1, countClubsLoded(), "Insira o ID do Clube: ");
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
                                    readerTemp = reader.readInt(1, season.getNumberOfCurrentTeams(), "Insira o ID do Clube: ");
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
                                Utils.waitEnter();
                                break;
                        }
                    } while (optionTemp != 0);
                    break;

                case 2:
                    System.out.println("Selecione um clube para treinar.");
                    ListClub.listClubLoaded(season.getCurrentClubs());
                    optionTemp = reader.readInt(1,season.getNumberOfCurrentTeams(), "Insira o ID do Clube para treinar: ");

                    season.setCoachingClubIndex(optionTemp-1);
                    //Criar metodo na seson para escolher o


                    break;

                default:
                    // TODO: Implementar lógica para importar dados salvos, se necessário
                    break;
            }
        } while (option != 0);
    }

    public static void limparConsole() {
        try {
            System.out.print("\033[H\033[2J");
            System.out.flush();
        } catch (Exception e) {
            // Se não funcionar, imprime várias linhas
            for (int i = 0; i < 50; ++i) System.out.println();
        }
    }
}
