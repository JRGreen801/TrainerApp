package de.jrgreen.trainerapp.object;

import java.util.ArrayList;
import java.util.Map;

/**
 * Describes a Row in the Feedback sheet as an Object.
 * @see de.jrgreen.trainerapp.helper.SheetHelper post / get
 * @see Rating
 */
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

    public static Feedback fromMap(Map map){
        ArrayList<Rating> ratings = new ArrayList<>();
        ratings.add(new Rating("CS", Math.toIntExact((Long) map.get("CS"))));
        ratings.add(new Rating("DP", Math.toIntExact((Long) map.get("DP"))));
        ratings.add(new Rating("DS", Math.toIntExact((Long) map.get("DS"))));
        ratings.add(new Rating("TA", Math.toIntExact((Long) map.get("TA"))));
        ratings.add(new Rating("TP", Math.toIntExact((Long) map.get("TP"))));

        ratings.add(new Rating("FO", Math.toIntExact((Long) map.get("FO"))));
        ratings.add(new Rating("LT", Math.toIntExact((Long) map.get("LT"))));
        ratings.add(new Rating("PF", Math.toIntExact((Long) map.get("PF"))));
        ratings.add(new Rating("ST", Math.toIntExact((Long) map.get("ST"))));

        return new Feedback(
                map.get("date").toString(),
                TraineeList.getTraineeByID(map.get("trainee_id").toString()),
                TrainerList.getTrainerByID(map.get("trainer_id").toString()),
                ratings,
                map.get("freeform").toString()
                );

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
