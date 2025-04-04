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


    public int getProgress() {
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
    public static void setProgress(int progress) {
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

    

    public GameLevel.LevelData makeLevelData(String path) {
        try {
            Json json = new Json();
            json.setOutputType(JsonWriter.OutputType.json);
            FileHandle mainFile = Gdx.files.local(path);

            GameLevel.LevelData levelData = json.fromJson(GameLevel.LevelData.class, mainFile.readString());

            if (levelData == null) {
                return new GameLevel.LevelData();
            }

            return levelData;

        } catch (Exception e) {
            return null;
        }

    }

    public void writeLevelDataToLocalFile(GameLevel.LevelData levelData, String path) {
        Json json = new Json();
        json.setOutputType(JsonWriter.OutputType.json);
        FileHandle file = Gdx.files.local(path);

        levelData.metaData.levelnr = levelNumber;

        file.writeString(json.prettyPrint(levelData), false);
    }

    public void resetCheckpointFile() {
        GameLevel.LevelData levelData;
        try{
            levelData = GameFileHandler.getInstance().makeLevelData("levels/checkpoint.json");
           
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

    public static int inProgresslevelnumber(){

        GameLevel.LevelData levelData;
        try{
            levelData = GameFileHandler.getInstance().makeLevelData("levels/checkpoint.json");
           
        } catch (Exception e){
            levelData = new LevelData();
           
        }
        if (levelData == null){levelData = new LevelData();}
        
        if (levelData.metaData == null){
            return -1;
        }
        if (levelData.metaData.progress != 0){
            return levelData.metaData.levelnr;
        }
        return -1;
    }

    public static class ProgressData {
        public int progress;
    }
}


