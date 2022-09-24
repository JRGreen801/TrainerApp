package de.jrgreen.trainerapp.object;

import java.util.ArrayList;

public class SelectedRatings extends ArrayList<Rating> {

    private boolean editMode;

    private int topicCount;
    private int detailCount;

    public SelectedRatings(boolean loadSettrings, boolean editMode) {
        topicCount = 0;
        detailCount = 0;
        this.editMode = editMode;
        if (loadSettrings) {
            for (Topic t :
                    Settings.get().topics) {
                add(new Rating(t.get_short(), (short) -1));
                topicCount++;
            }
            for (Detail d :
                    Settings.get().details) {
                add(new Rating(d.get_short(), (short) -1));
                detailCount++;
            }
        }
    }
    public SelectedRatings(ArrayList<Rating> ratings, boolean editMode) {
        this.editMode = editMode;
        clear();
        addAll(ratings);


    }

    public ArrayList<Rating> getTopicRatings(){
        ArrayList<Rating> result = new ArrayList<Rating>();
        result.addAll(this.subList(0, topicCount-1));
        return result;
    }
    public ArrayList<Rating> getDetailRatings(){
        ArrayList<Rating> result = new ArrayList<Rating>();
        result.addAll(this.subList(topicCount, detailCount-1));
        return result;
    }

    public boolean isEditMode(){
        return editMode;
    }
}
