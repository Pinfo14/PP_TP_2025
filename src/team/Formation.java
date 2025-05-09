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
    private int numStrikers;

    public Formation(String name, int numDefenders, int numMidfielders, int numAttackers, int numStrikers) {
        this.name = name;
        this.numDefenders = numDefenders;
        this.numMidfielders = numMidfielders;
        this.numAttackers = numAttackers;
        this.numStrikers = numStrikers;
    }

    @Override
    public int getTacticalAdvantage(IFormation iFormation) {

        if(iFormation == null){
            throw new IllegalStateException("formation nao pode ser nula");
        }

        Formation form = (Formation) iFormation;

        if(this.numMidfielders < form.numMidfielders){
            return 1;
        }
        if(this.numMidfielders > form.numMidfielders){
            return -1;
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
    public int getNumStrikers() {
        return this.numStrikers;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Formation )){
            return false;
        }
        Formation form = (Formation) o;

        return this.numAttackers == form.numAttackers &&
                this.numDefenders == form.numDefenders &&
                this.numMidfielders == form.numMidfielders &&
                this.numStrikers == form.numStrikers &&
                this.name.equals(form.name);
  }


}
