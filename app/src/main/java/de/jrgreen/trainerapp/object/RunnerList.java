package de.jrgreen.trainerapp.object;

import android.util.Log;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class RunnerList extends ArrayList<Runner> {

    private static RunnerList instance;

    private RunnerList() {
    }

    public static void set(ArrayList<Runner> runnerList){
        if (instance == null) {
            instance = new RunnerList();
        }
        instance.clear();
        instance.addAll(runnerList);
    }

    public static RunnerList get(){
        if (instance != null) {
            return instance;
        }
        instance = new RunnerList();
        return instance;
    }

    public static ArrayList<Trainer> getTrainers(){
        Log.e("GET TRAINERS", instance.stream().filter(runner -> runner instanceof Trainer).map(runner -> (Trainer) runner).collect(Collectors.toList()).toString());
        Log.e("RUNNERS", instance.toString());
        return (ArrayList<Trainer>) instance.stream().filter(runner -> runner instanceof Trainer).map(runner -> (Trainer) runner).collect(Collectors.toList());
    }
    public static ArrayList<Trainee> getTrainees(){
        return (ArrayList<Trainee>) instance.stream().filter(runner -> runner instanceof Trainee).map(runner -> (Trainee) runner).collect(Collectors.toList());
    }


    public static Runner getRunnerByID(String employee_ID){
        for (Runner r:
                instance) {
            if (r.getEmployee_ID().equals(employee_ID)){
                return r;
            }
        }
        return null;
    }

    public static int getIndexByID(String employee_ID){
        for (Runner r:
                instance) {
            if (r.getEmployee_ID().equals(employee_ID)){
                return instance.indexOf(r);
            }
        }
        return -1;
    }
}
