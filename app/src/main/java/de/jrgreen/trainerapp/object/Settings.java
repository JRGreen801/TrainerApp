package de.jrgreen.trainerapp.object;

import java.util.ArrayList;
import java.util.List;

/**
 * Settings class is used to get the structure and amount of feedback topics and details to display / send.
 *
 * TODO: May be deleted; and structure for feedback will be imported from the Sheet directly.
 * @see de.jrgreen.trainerapp.helper.SheetHelper settings may be done here in future.
 *
 */
public class Settings {
    private static Settings instance;

    public int topicCount;
    public int detailCount;
    public List<Topic> topics;
    public List<Detail> details;
    public String hub;

    private Settings(List<Topic> topics, List<Detail> details, String hub) {
        this.topics = topics;
        this.details = details;
        topicCount = topics.size();
        detailCount = details.size();
        this.hub = hub;
    }

    public static void set(List<Topic> topics, List<Detail> details, String hub) {
        Settings.instance = new Settings(topics, details, hub);
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
