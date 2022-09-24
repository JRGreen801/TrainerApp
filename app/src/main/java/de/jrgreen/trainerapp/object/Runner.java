package de.jrgreen.trainerapp.object;

public abstract class Runner {
    private String name;
    private String employee_ID;

    public Runner(String name, String employee_ID) {
        this.name = name;
        this.employee_ID = employee_ID;
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
