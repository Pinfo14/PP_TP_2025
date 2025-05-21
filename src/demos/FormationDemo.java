package demos;

import management.FormationManagement;

public class FormationDemo {

    public static void main(String[] args) {
        FormationManagement fm = new FormationManagement();

        fm.addFormation(4,4,2, 0);
        fm.addFormation(5,2,1, 2);
        fm.addFormation(4,3,3, 0);
        fm.addFormation(2,1,4, 3);
        fm.addFormation(4,5,1, 0);
        fm.addFormation(3,4,2, 1);

        fm.listFormations();

    }
}
