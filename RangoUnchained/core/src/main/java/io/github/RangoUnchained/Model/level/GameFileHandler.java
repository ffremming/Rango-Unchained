package io.github.RangoUnchained.Model.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;

import io.github.RangoUnchained.Model.level.GameLevel.LevelData;
import io.github.RangoUnchained.Model.level.GameLevel.LevelData.MetaData;

public class GameFileHandler {

    private static GameFileHandler fileHandler;

    private int levelNumber;

    private GameFileHandler() {
    }

    public static GameFileHandler getInstance() {
        if (fileHandler == null) {
            fileHandler = new GameFileHandler();
        }
        return fileHandler;
    }

    public void setLevelNumber(int levelNumber) {
        this.levelNumber = levelNumber;
    }


    public synchronized int getProgress() {
        try {
            // Create a new Json instance.
            Json json = new Json();

            // Open the JSON file from the local file system.
            FileHandle progressFile = Gdx.files.internal("levels/progress.json");

            // Check if the file exists; if not, return default progress.
            if (!progressFile.exists()) {
                return 0; // Default progress if file not found
            }

            // Read the file's contents into a String.
            String jsonString = progressFile.readString();

            // Parse the JSON into a ProgressData object.
            ProgressData progressData = json.fromJson(ProgressData.class, jsonString);

            // Return the progress value.
            return progressData.progress;
        } catch (Exception e) {
            e.printStackTrace();
            return 0; // Default progress in case of an error
        }
    }


    /**
     * Updates the progress value and saves it to a JSON file.
     * <p>
     * This method creates a {@code ProgressData} object with the specified progress value
     * and writes it to a JSON file located at {@code levels/progress.json}.
     * If an error occurs during the process, the exception is caught and its stack trace is printed.
     * </p>
     *
     * @param progress the progress value to be saved
     */
    public synchronized static void setProgress(int progress) {
        try {
            // Create a new Json instance.
            Json json = new Json();
            json.setOutputType(JsonWriter.OutputType.json);

            // Create a ProgressData object and set the progress value.
            ProgressData progressData = new ProgressData();
            progressData.progress = progress;

            // Write the progress data to the JSON file.
            FileHandle progressFile = Gdx.files.local("levels/progress.json");
            progressFile.writeString(json.prettyPrint(progressData), false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    /**
     * Reads level data from a file in the internal (read-only) assets directory.
     * Used for loading predefined levels like level1.json.
     */
    public GameLevel.LevelData readLevelDataFromAssets(String path) {
        try {
            Json json = new Json();
            json.setOutputType(JsonWriter.OutputType.json);
            FileHandle file = Gdx.files.internal(path);

            GameLevel.LevelData levelData = json.fromJson(GameLevel.LevelData.class, file.readString());

            if (levelData == null) return new GameLevel.LevelData();
            return levelData;

        } catch (Exception e) {
            Gdx.app.error("FILE_HANDLER", "Failed to read internal level file: " + path, e);
            return null;
        }
    }

    /**
     * Reads checkpoint or other modifiable level data from local storage.
     */
    public GameLevel.LevelData readLevelDataFromLocal(String path) {
        try {
            Json json = new Json();
            json.setOutputType(JsonWriter.OutputType.json);
            FileHandle file = Gdx.files.local(path);
            Gdx.app.log("DEBUG", "Writing to: " + file.path());
            System.out.println(file.exists());
            if (!file.exists()) return null;

            GameLevel.LevelData levelData = json.fromJson(GameLevel.LevelData.class, file.readString());

            if (levelData == null) return new GameLevel.LevelData();
            return levelData;

        } catch (Exception e) {
            Gdx.app.error("FILE_HANDLER", "Failed to read local file: " + path, e);
            return null;
        }
    }

    /**
     * Writes level data (typically checkpoint or save file) to local storage.
     */
    public void writeLevelDataToLocalFile(GameLevel.LevelData levelData, String path) {
        try {
            Json json = new Json();
            json.setOutputType(JsonWriter.OutputType.json);
            FileHandle file = Gdx.files.local(path);

            if (levelData.metaData == null) {
                levelData.metaData = new MetaData();
            }

            levelData.metaData.levelnr = levelNumber;

            file.writeString(json.prettyPrint(levelData), false);
        } catch (Exception e) {
            Gdx.app.error("FILE_HANDLER", "Failed to write local file: " + path, e);
        }
    }


    public synchronized void resetCheckpointFile() {
        GameLevel.LevelData levelData;
        try{
            levelData = GameFileHandler.getInstance().readLevelDataFromLocal("levels/checkpoint.json");


        } catch (Exception e){
            levelData = new LevelData();

        }
        if (levelData == null){levelData = new LevelData();}
        levelData.metaData = new MetaData();
        levelData.metaData.progress = 0;
        levelData.metaData.levelnr = levelNumber;
        // Reset progress to 0 for user ended game.


        // Write reset data to checkpoint
        GameFileHandler.getInstance().writeLevelDataToLocalFile(levelData, "levels/checkpoint.json");
        GameFileHandler.getInstance().writeLevelDataToLocalFile(levelData, "levels/checkpointBackup.json");
    }

    public synchronized static int inProgresslevelnumber(){

        GameLevel.LevelData levelData;
        try{
            levelData = GameFileHandler.getInstance().readLevelDataFromLocal("levels/checkpoint.json");
        } catch (Exception e){
            return -1;
        }
        if (levelData == null){return -1;}

        if (levelData.metaData == null){
            return -1;
        }
        if (levelData.metaData.progress == 1){
            return levelData.metaData.levelnr;
        }
        return -1;
    }

    public static class ProgressData {
        public int progress;
    }
}


