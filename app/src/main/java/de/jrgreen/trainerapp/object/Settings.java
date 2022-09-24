package de.jrgreen.trainerapp.object;

import java.util.ArrayList;
import java.util.List;

public class Settings {
    private static Settings instance;

    public int topicCount;
    public int detailCount;
    public List<Topic> topics;
    public List<Detail> details;

    private Settings(){}

    private Settings(List<Topic> topics, List<Detail> details) {
        this.topics = topics;
        this.details = details;
        topicCount = topics.size();
        detailCount = details.size();
    }

    public static void set(List<Topic> topics, List<Detail> details) {
        Settings.instance = new Settings(topics, details);
    }

    public static void set(Settings settingsInstance) {
        Settings.instance = settingsInstance;
        instance.topicCount = settingsInstance.topics.size();
        instance.detailCount = settingsInstance.details.size();
    }

    public static Settings get() {
        if (instance != null) {
            return instance;
        }
        return null;
    }

    public static ArrayList<String> getRatingShorts(){
        ArrayList<String> ratingShorts = new ArrayList<>();
        for (Topic t:
                instance.topics) {
            ratingShorts.add(t.get_short());
        }
        for (Detail d:
                instance.details) {
            ratingShorts.add(d.get_short());
        }
        return ratingShorts;
    }
}
