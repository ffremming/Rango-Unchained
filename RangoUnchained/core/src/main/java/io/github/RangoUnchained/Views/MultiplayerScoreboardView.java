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

        Label title = new Label("Results for lobby: " + lobby.lobbyId, getSkin());
        scoreboardTable.add(title).colspan(2).padBottom(30).row();

        List<PlayerInLobby> players = new ArrayList<>(lobby.players.values());

        players.sort((a, b) -> {
            int aScore = a.finishScore != null ? a.finishScore : 0;
            int bScore = b.finishScore != null ? b.finishScore : 0;
            int scoreCompare = Integer.compare(bScore, aScore);
            if (scoreCompare != 0) return scoreCompare;
            double aTime = a.finishTime != null ? a.finishTime : Long.MAX_VALUE;
            double bTime = b.finishTime != null ? b.finishTime : Long.MAX_VALUE;
            return Double.compare(aTime, bTime);
        });

        int rank = 1;
        for (PlayerInLobby p : players) {
            String name = rank + ". " + p.displayName;
            String scoreText = "Score: " + (p.finishScore != null ? p.finishScore : "N/A");
            String time = String.format("%.1f s", p.finishTime);
            String timeText = "Time: " + (p.finishTime != null ? time + "s" : "N/A");

            scoreboardTable.add(new Label(name, getSkin())).padRight(20);
            scoreboardTable.add(new Label(scoreText + ", " + timeText, getSkin())).row();
            rank++;
        }

        scoreboardTable.row().padTop(40);
        ButtonFactory.createButton("Back to lobby", 300, 60, getSkin(), game,
            () -> game.setView(new GameLobbyWaitingView(lobby)), "customLoginStyle", scoreboardTable);

        ButtonFactory.createButton("Back to menu", 300, 60, getSkin(), game,
            this::backToMenu, "customLoginStyle", scoreboardTable);
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
