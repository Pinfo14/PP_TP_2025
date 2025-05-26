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
        int idex = 0;
        try {
            this.formations[idex++] = new Formation(5, 3, 2);
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }
        try {
            this.formations[idex++] = new Formation(4, 3, 3);
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }
        try {
            this.formations[idex++] = new Formation(4, 4, 2);
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }
        this.countFormations = idex;

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
        try {
            formations[countFormations] = new Formation(numDefenders, numMidfielders, numAttackers);
            countFormations++;
        }catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }
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
