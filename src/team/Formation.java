package team;

import com.ppstudios.footballmanager.api.contracts.team.IFormation;



/**
 * Nome: Emanuel Jose Teixeira Pinto
 * Número: 8230371
 * Turma: LEI1T1
 * <p>
 * Nome: Roberto Cristiano Martins Faria
 * Número: 8230067
 * Turma: LEI1T2
 */
public class Formation implements IFormation {

    private String name;
    private int numDefenders;
    private int numMidfielders;
    private int numAttackers;

    public Formation(int numDefenders, int numMidfielders, int numAttackers) {
        if (!isValidFormation(numDefenders, numMidfielders, numAttackers)) {
            throw new IllegalArgumentException("Invalid formation");
        }
        this.name = numDefenders + "-" + numMidfielders + "-" + numAttackers;
        this.numDefenders = numDefenders;
        this.numMidfielders = numMidfielders;
        this.numAttackers = numAttackers;
    }

    private boolean isValidFormation(int numDefenders, int numMidfielders, int numAttackers) {
        if(numAttackers+numDefenders+numMidfielders !=10 ){
            return false;
        }
        return true;
    }

    @Override
    public int getTacticalAdvantage(IFormation iFormation) {

        if(iFormation == null){
            throw new IllegalStateException("formation nao pode ser nula");
        }

        Formation form = (Formation) iFormation;

        if(this.numMidfielders < form.numMidfielders){
            return 5;//percentagem
        }
        if(this.numMidfielders > form.numMidfielders){
            return -5;
        }
        return 0;
    }

    @Override
    public String getDisplayName() {
        return this.name;
    }

    public int getNumAttackers() {
        return this.numAttackers;
    }
    public int getNumDefenders() {
        return this.numDefenders;
    }
    public int getNumMidfielders() {
        return this.numMidfielders;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Formation )){
            return false;
        }
        Formation form = (Formation) o;

        return this.numAttackers == form.numAttackers &&
                this.numDefenders == form.numDefenders &&
                this.numMidfielders == form.numMidfielders &&
                this.name.equals(form.name);
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append(this.numDefenders);
        str.append("-");
        str.append(this.numMidfielders);
        str.append("-");
        str.append(this.numAttackers);

        return str.toString();
    }


}
