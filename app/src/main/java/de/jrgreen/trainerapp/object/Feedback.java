package de.jrgreen.trainerapp.object;

import java.util.ArrayList;

public class Feedback {
    private Trainee trainee;
    private Trainer trainer;
    private String date; // dd/mm/yyyy

    private ArrayList<Rating> ratings;
    private String freeform;

    public Feedback(String date,Trainee trainee, Trainer trainer, ArrayList<Rating> ratings, String freeform) {
        this.date = date;
        this.trainee = trainee;
        this.trainer = trainer;
        this.ratings = ratings;
        this.freeform = freeform;
    }

    public Feedback(String date,Trainee trainee, Trainer trainer, ArrayList<Rating> ratings) {
        this.date = date;
        this.trainee = trainee;
        this.trainer = trainer;
        this.ratings = ratings;
    }

    public Trainee getTrainee() {
        return trainee;
    }

    public Trainer getTrainer() {
        return trainer;
    }

    public String getDate() {
        return date;
    }

    public ArrayList<Rating> getRatings() {
        return ratings;
    }

    public String getFreeform() {
        return freeform;
    }

    @Override
    public String toString() {
        return "Feedback{" +
                "trainee=" + trainee +
                ", trainer=" + trainer +
                ", date='" + date + '\'' +
                ", ratings=" + ratings +
                ", freeform='" + freeform + '\'' +
                '}';
    }
}
