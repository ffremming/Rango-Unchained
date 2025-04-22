package io.github.RangoUnchained.Views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Timer;

import java.util.ArrayList;
import java.util.List;

import io.github.RangoUnchained.Controllers.GameController;
import io.github.RangoUnchained.Model.Firebase.MultiplayerManager;
import io.github.RangoUnchained.Model.Firebase.Utils.LobbyInfo;
import io.github.RangoUnchained.Model.Firebase.Utils.PlayerInLobby;
import io.github.RangoUnchained.Views.Utils.BaseScreen;
import io.github.RangoUnchained.Views.Utils.ButtonFactory;
import io.github.RangoUnchained.Views.Utils.LabelFactory;

public class MultiplayerScoreboardView extends BaseScreen {
    private final LobbyInfo lobby;
    private final MultiplayerManager dbManager = GameController.getInstance().getMultiplayerManager();
    private Table scoreboardTable;
    private Timer.Task lobbyPingTask;

    public MultiplayerScoreboardView(LobbyInfo lobby) {
        super(GameController.getInstance());
        this.lobby = lobby;
    }

    @Override
    public void show() {
        super.show();
        createUI();
        addLobbyListener();
    }

    private void createUI() {
        scoreboardTable = new Table();
        scoreboardTable.setFillParent(true);
        scoreboardTable.top().padTop(50);
        stage.addActor(scoreboardTable);

        updateScoreboard(lobby);
    }

    private void updateScoreboard(LobbyInfo lobby) {
        scoreboardTable.clear();

        Label title = LabelFactory.createLabel("Results for lobby: " + lobby.lobbyId, getSkin(), "rioGrandeFont", null);
        scoreboardTable.add(title).colspan(2).padBottom(30).row();

        List<PlayerInLobby> players = new ArrayList<>(lobby.players.values());

        players.sort((a, b) -> {
            double aScore = a.finishScore != null ? a.finishScore : 0;
            double bScore = b.finishScore != null ? b.finishScore : 0;
            if (aScore == bScore) {
                return 0;
            }
            return aScore < bScore ? 1 : -1;
        });

        int rank = 1;
        for (PlayerInLobby p : players) {
            String name = rank + ". " + p.displayName;
            String scoreText;

            if (p.finishScore == null || p.finishTime == null) {
                // Player hasn't finished yet
                scoreText = p.displayName + " is still playing...";
            } else {
                // Player has finished, format normally
                String time = String.format("%.1f", p.finishTime);
                scoreText = "Score: " + p.finishScore + ", Time: " + time + " s";
            }

            LabelFactory.createLabel(name, getSkin(), "rioGrandeFont", null, 10, scoreboardTable);
            LabelFactory.createLabel(scoreText, getSkin(), "rioGrandeFont", null, 10, scoreboardTable);
            rank++;
        }

        scoreboardTable.row().padTop(40);
        ButtonFactory.createDefaultButton("Back to lobby", () -> game.setView(new GameLobbyWaitingView(lobby)), scoreboardTable);

        ButtonFactory.createDefaultButton("Back to menu", this::backToMenu, scoreboardTable);
    }

    private void addLobbyListener() {
        // Ensure any existing task is cancelled
        if (lobbyPingTask != null) {
            lobbyPingTask.cancel();
            lobbyPingTask = null;
        }

        // Periodic task to refresh the lobby
        lobbyPingTask = Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                dbManager.listenToLobby(lobby.lobbyId, new MultiplayerManager.Callback<LobbyInfo>() {
                    @Override
                    public void onSuccess(LobbyInfo updatedLobby) {
                        Gdx.app.postRunnable(() -> updateScoreboard(updatedLobby));
                    }

                    @Override
                    public void onError(Exception e) {
                        System.err.println("Error listening to lobby: " + e.getMessage());
                        backToMenu();
                    }
                });
            }
            // First delay, then interval in seconds
        }, 0, 60);
    }

    private void backToMenu() {
        dbManager.leaveLobby(lobby.lobbyId, game.getCurrentUser(), new MultiplayerManager.Callback<Void>() {
            @Override
            public void onSuccess(Void result) {
                Gdx.app.postRunnable(() -> game.setView(new MainMenuView()));
            }

            @Override
            public void onError(Exception e) {
                System.err.println("Failed to leave lobby: " + e.getMessage());
            }
        });
    }

    @Override
    public void dispose() {
        super.dispose();
        dbManager.removeLobbyListener();
    }

    @Override
    public void hide() {
        super.hide();
        if (lobbyPingTask != null) {
            lobbyPingTask.cancel();
        }
    }
}
