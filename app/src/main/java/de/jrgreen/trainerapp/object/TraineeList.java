package de.jrgreen.trainerapp.object;

import java.util.ArrayList;

public class TraineeList extends ArrayList<Trainee> {

    private static TraineeList instance;

    private TraineeList() {
    }

    public static void set(ArrayList<Trainee> traineeList){
        if (instance == null) {
            instance = new TraineeList();
        }
        instance.clear();
        instance.addAll(traineeList);
    }

    public static TraineeList get(){
        if (instance != null) {
            return instance;
        }
        instance = new TraineeList();
        return instance;
    }

    public static Trainee getTraineeByID(String employee_ID){
        for (Trainee t:
                instance) {
            if (t.getEmployee_ID().equals(employee_ID)){
                return t;
            }
        }
        return null;
    }

    public static int getIndexByID(String employee_ID){
        for (Trainee t:
                instance) {
            if (t.getEmployee_ID().equals(employee_ID)){
                return instance.indexOf(t);
            }
        }
        return -1;
    }
}
