package team;

import com.ppstudios.footballmanager.api.contracts.team.IFormation;

public class Formation implements IFormation {

    private String name;
    private int numDefenders;
    private int numMidfielders;
    private int numAttackers;

    @Override
    public int getTacticalAdvantage(IFormation iFormation) {
        return 0;
    }

    @Override
    public String getDisplayName() {
        return this.name;
    }
}
