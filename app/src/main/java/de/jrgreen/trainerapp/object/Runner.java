package de.jrgreen.trainerapp.object;

import android.util.Log;

import java.util.Map;

/**
 * Describes a basic Runner without role specification, extended by Trainer and Trainee.
 * @see Trainer Object describing a Trainer
 * @see Trainee Object describing a Trainee
 */
public abstract class Runner {
    private String name;
    private String employee_ID;

    /**
     *
     * @param name name of the Runner
     * @param employee_ID employee_ID of the Runner
     */
    public Runner(String name, String employee_ID) {
        this.name = name;
        this.employee_ID = employee_ID;
    }

    public static Runner fromMap(Map map){
        if (map.get("role").equals("trainer")){
            Log.e("MAP TRAINER", map.get("employee_id").toString());
            return new Trainer(map.get("name").toString(), map.get("employee_id").toString());
        } else
        if (map.get("role").equals("trainee")){
            return new Trainee(map.get("name").toString(), map.get("employee_id").toString());
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmployee_ID() {
        return employee_ID;
    }

    public void setEmployee_ID(String employee_ID) {
        this.employee_ID = employee_ID;
    }

    @Override
    public String toString() {
        return name;
    }
}
