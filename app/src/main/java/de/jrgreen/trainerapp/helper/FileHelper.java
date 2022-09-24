package de.jrgreen.trainerapp.helper;

import android.content.Context;
import android.os.Environment;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import de.jrgreen.trainerapp.R;
import de.jrgreen.trainerapp.object.Runner;
import de.jrgreen.trainerapp.object.RunnerList;
import de.jrgreen.trainerapp.object.Settings;
import de.jrgreen.trainerapp.object.Feedback;
import de.jrgreen.trainerapp.object.TraineeList;
import de.jrgreen.trainerapp.ui.main.MainActivity;

public class FileHelper {

    /**
     * Loads Settings from a resource file, converts raw File data to a Json String
     * and creates a new Settings object from the JsonString
     *
     * @param context
     * @return Settings instance
     */
    public static Settings convertJsonToSettings(Context context){
        InputStream inputStream = context.getResources().openRawResource(R.raw.settings);
        String jsonString = "";
        try {
            byte[] data = new byte[inputStream.available()];
            inputStream.read(data);
            inputStream.close();

            jsonString = new String(data, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new Gson().fromJson(jsonString, new TypeToken<Settings>(){}.getType());
    }

    public static Object convertJsonToObject(Context context, String fileName, TypeToken typeToken){
        File folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        File file = new File(folder, "trainerapp/" + fileName + ".json");
        file.getParentFile().mkdirs();

        FileInputStream inputStream = null;
        String jsonString = "";
        try {
            inputStream = new FileInputStream(file);
            byte[] data = new byte[inputStream.available()];
            inputStream.read(data);
            inputStream.close();

            jsonString = new String(data, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Gson().fromJson(jsonString, typeToken.getType());
    }
    public static ArrayList<Runner> convertJsonToRunners(Context context){
        File folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        File file = new File(folder, "trainerapp/runners.json");
        file.getParentFile().mkdirs();

        String jsonString = "";
        ArrayList<Runner> runnerArrayList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))){
            while ((jsonString = br.readLine()) != null){
                runnerArrayList.add(new Gson().fromJson(jsonString, new TypeToken<Runner>(){}.getType()));
            }
        } catch (IOException e) {
            e.printStackTrace();

        }
        return runnerArrayList;
    }

    public static ArrayList<Feedback> convertJsonToFeedbackList(Context context){
        File folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        File file = new File(folder, "trainerapp/feedback.json");
        file.getParentFile().mkdirs();

        String jsonString = "";
        ArrayList<Feedback> feedbackArrayList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))){
            while ((jsonString = br.readLine()) != null){
                feedbackArrayList.add(new Gson().fromJson(jsonString, new TypeToken<Feedback>(){}.getType()));
            }
        } catch (IOException e) {
            e.printStackTrace();

        }
        return feedbackArrayList;
    }


    public static void saveToJson(Context context, Object data){
        File folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        File file = new File(folder, "trainerapp/feedback.json");
        file.getParentFile().mkdirs();
        FileOutputStream fileOutputStream = null;

        String jsonString = new Gson().toJson(data, new TypeToken<Feedback>(){}.getType()) + "\n";

        try {
            file.createNewFile();
            fileOutputStream = new FileOutputStream(file, true);
            fileOutputStream.write(jsonString.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
        }
    }
    public static void saveTrainees(Context context, Object data){
        File folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        File file = new File(folder, "trainerapp/trainees.json");
        file.getParentFile().mkdirs();
        FileOutputStream fileOutputStream = null;

        String jsonString = new Gson().toJson(data, new TypeToken<TraineeList>(){}.getType());

        try {
            file.createNewFile();
            fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(jsonString.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
        }
    }

    public static void saveTrainers(Context context, Object data){
        File folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        File file = new File(folder, "trainerapp/trainers.json");
        file.getParentFile().mkdirs();
        FileOutputStream fileOutputStream = null;

        String jsonString = new Gson().toJson(data, new TypeToken<TraineeList>(){}.getType());

        try {
            file.createNewFile();
            fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(jsonString.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
        }
    }

    public static void clearFIle(String fileName){
        File folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        File file = new File(folder, "trainerapp/" + fileName + ".json");
        file.getParentFile().mkdirs();
        if (file.exists()){
            file.delete();
        }
    }

    public static <T> void backupToJson(String fileName, ArrayList<T> objects) {
        clearFIle(fileName);
        File folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        File file = new File(folder, "trainerapp/"+ fileName +".json");
        file.getParentFile().mkdirs();
        FileOutputStream fileOutputStream = null;

        try {
            file.createNewFile();
            fileOutputStream = new FileOutputStream(file, false);

            for (int i=0; i < objects.size(); i++) {
                String jsonString = new Gson().toJson(objects.get(i), new TypeToken<T>(){}.getType()) + "\n";
                fileOutputStream.write(jsonString.getBytes(StandardCharsets.UTF_8));
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
