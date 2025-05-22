package demos;

import management.FormationManagement;

public class FormationDemo {

    public static void main(String[] args) {
        FormationManagement fm = new FormationManagement();

        fm.addFormation(4,4,2);
        fm.addFormation(5,2,1);
        fm.addFormation(4,3,3);
        fm.addFormation(2,1,4);
        fm.addFormation(4,5,1);
        fm.addFormation(3,4,2);

        fm.listFormations();

    }
}
