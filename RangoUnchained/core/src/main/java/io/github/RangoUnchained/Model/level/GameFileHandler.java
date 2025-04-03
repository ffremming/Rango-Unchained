package io.github.RangoUnchained.Model.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;

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

        file.delete();
        file.writeString(json.prettyPrint(levelData), false);
    }

    public void resetCheckpointFile() {
        GameLevel.LevelData levelData = GameFileHandler.getInstance().makeLevelData("levels/checkpoint.json");

        // Reset progress to 0 for user ended game.
        GameLevel.LevelData.MetaData metaData = new GameLevel.LevelData.MetaData();
        levelData.metaData.progress = 0;
        levelData.metaData.levelnr = levelNumber;

        // Write reset data to checkpoint
        GameFileHandler.getInstance().writeLevelDataToLocalFile(levelData, "levels/checkpoint.json");
        GameFileHandler.getInstance().writeLevelDataToLocalFile(levelData, "levels/checkpointBackup.json");
    }
}
