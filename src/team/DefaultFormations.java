package team;

import com.ppstudios.footballmanager.api.contracts.team.IFormation;

public class DefaultFormations {

    private static final int ARRAY_INIT = 10;
    private static final int INCREMENT = 2;

    private Formation[] formations;
    private int formationCount ;

    public DefaultFormations() {
        this.formations = new Formation[ARRAY_INIT];
        setDefaults();
    }

    public Formation[] getFormations() {
       Formation[] formation = new Formation[this.formationCount];
       for(int i = 0; i < this.formationCount; i++){
           formation[i] = this.formations[i];
       }
       return formation;
    }

    private void setDefaults() {
        this.formations[0] = new Formation("4-3-2-1", 4, 3, 2,1);
        this.formations[1] = new Formation("4-3-3", 4, 3, 3,0);
        this.formations[2] = new Formation("4-4-2", 4, 4, 2,0);
        this.formationCount=3;
    }

    public void addFormation(IFormation formation) {
        if(formation == null){
            throw new IllegalArgumentException("formation nao pode ser nula");
        }
        if(contains(formation)){
            throw new IllegalStateException("formation ja existe");
        }
        Formation form = (Formation) formation;
        if(this.formationCount == this.formations.length){
            increaseArraySize();
        }
        this.formations[this.formationCount] = form;
    }

    private boolean contains(IFormation formation) {
        for(int i = 0; i < this.formationCount; i++){
            if(this.formations[i].equals(formation)){
                return true;
            }
        }
        return false;
    }

    private void increaseArraySize() {
        Formation[] newFormations = new Formation[this.formations.length * INCREMENT];
        for(int i = 0; i < this.formations.length; i++){
            newFormations[i] = this.formations[i];
        }
        this.formations = newFormations;
    }
}
