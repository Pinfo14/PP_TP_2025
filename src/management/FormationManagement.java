package management;

import com.ppstudios.footballmanager.api.contracts.team.IFormation;
import team.Formation;

public class FormationManagement {

    private static final int INITIAL_FORMATIONS = 10;
    private static final int INCREMENT_FACTOR = 2;

    private IFormation[] formations;
    private int countFormations;

    public FormationManagement() {
        formations = new Formation[INITIAL_FORMATIONS];
        this.formations[0] = new Formation( 4, 3, 2);
        this.formations[1] = new Formation(4, 3, 3);
        this.formations[2] = new Formation(4, 4, 2);
        this.countFormations=3;
    }

    public IFormation getFormation(int index) {
        return formations[index];
    }

    private void increaseFormationArray() {
        IFormation[] formartionsTemp = new IFormation[formations.length + INCREMENT_FACTOR];

        System.arraycopy(formations, 0, formartionsTemp, 0, formations.length);
        formations = formartionsTemp;
    }

    public void addFormation(int numDefenders, int numMidfielders, int numAttackers) {
        formations[countFormations] = new Formation(numDefenders, numMidfielders, numAttackers);
        countFormations++;
    }

    public void listFormations() {
        int counter = 0;

        for(IFormation form : formations) {
            if(form != null) {
                counter++;
                System.out.println(counter + " -> " + form.toString());
            }
        }

    }

    public int getNumFormations() {
        return countFormations;
    }



}
