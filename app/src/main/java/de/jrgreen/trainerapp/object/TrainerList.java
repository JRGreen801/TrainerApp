package de.jrgreen.trainerapp.object;

import java.util.ArrayList;

public class TrainerList extends ArrayList<Trainer> {

    private static TrainerList instance;

    private TrainerList() {
    }

    public static void set(ArrayList<Trainer> trainerList){
        if (instance == null) {
            instance = new TrainerList();
        }
        instance.clear();
        instance.addAll(trainerList);
    }

    public static TrainerList get(){
        if (instance != null) {
            return instance;
        }
        instance = new TrainerList();
        return instance;
    }

    public static Trainer getTrainerByID(String employee_ID){
        for (Trainer t:
             instance) {
            if (t.getEmployee_ID().equals(employee_ID)){
                return t;
            }
        }
        return null;
    }

    public static int getIndexByID(String employee_ID){
        for (Trainer t:
                instance) {
            if (t.getEmployee_ID().equals(employee_ID)){
                return instance.indexOf(t);
            }
        }
        return -1;
    }
}
