package io.github.RangoUnchained.Model.Firebase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import io.github.RangoUnchained.Model.Firebase.Utils.LobbyInfo;
import io.github.RangoUnchained.Model.Firebase.Utils.PlayerInLobby;
import io.github.RangoUnchained.Model.Firebase.Utils.UserInfo;

public class DummyRealtimeDBManager implements MultiplayerManager {

    private final Map<String, LobbyInfo> lobbies = new HashMap<>();

    // Listeners
    private Callback<List<LobbyInfo>> publicLobbiesCallback;
    private String listeningLobbyId = null;
    private Callback<LobbyInfo> lobbyCallback;

    @Override
    public void createLobby(UserInfo host, Boolean isPublic, int maxPlayers, Callback<LobbyInfo> callback) {
        String lobbyId = generateLobbyCode();
        LobbyInfo lobbyInfo = new LobbyInfo(lobbyId, host, isPublic, maxPlayers);
        lobbies.put(lobbyId, lobbyInfo);
        callback.onSuccess(lobbyInfo);

        // Notify any listeners
        notifyPublicLobbiesListeners();
    }

    @Override
    public void joinLobby(String lobbyId, UserInfo player, Callback<LobbyInfo> callback) {
        LobbyInfo lobby = lobbies.get(lobbyId);
        if (lobby == null) {
            callback.onError(new Exception("Lobby not found"));
            return;
        }
        if (lobby.isLobbyFull()) {
            callback.onError(new Exception("Lobby is full"));
            return;
        }

        lobby.players.put(player.uid, new PlayerInLobby(player));
        callback.onSuccess(lobby);

        // Notify listeners
        notifyLobbyListener(lobbyId);
        notifyPublicLobbiesListeners();
    }

    @Override
    public void leaveLobby(String lobbyId, UserInfo player, Callback<Void> callback) {
        LobbyInfo lobby = lobbies.get(lobbyId);
        if (lobby != null) {
            lobby.players.remove(player.uid);
            callback.onSuccess(null);

            notifyLobbyListener(lobbyId);
            notifyPublicLobbiesListeners();
        } else {
            callback.onError(new Exception("Lobby not found"));
        }
    }

    @Override
    public void fetchPublicLobbies(Callback<List<LobbyInfo>> callback) {
        // Save callback so we can notify it later if something endres
        publicLobbiesCallback = callback;
        notifyPublicLobbiesListeners();
    }

    private void notifyPublicLobbiesListeners() {
        if (publicLobbiesCallback != null) {
            List<LobbyInfo> publicLobbies = new ArrayList<>();
            for (LobbyInfo lobby : lobbies.values()) {
                if (lobby.isPublic && !lobby.isLobbyFull() && "waiting".equals(lobby.status)) {
                    publicLobbies.add(lobby);
                }
            }
            publicLobbiesCallback.onSuccess(publicLobbies);
        }
    }

    @Override
    public void removePublicLobbiesListener() {
        publicLobbiesCallback = null;
    }

    @Override
    public void listenToLobby(String lobbyId, Callback<LobbyInfo> callback) {
        LobbyInfo lobby = lobbies.get(lobbyId);
        if (lobby != null) {
            this.listeningLobbyId = lobbyId;
            this.lobbyCallback = callback;
            callback.onSuccess(lobby);
        } else {
            callback.onError(new Exception("Lobby not found"));
        }
    }

    private void notifyLobbyListener(String lobbyId) {
        if (lobbyId.equals(this.listeningLobbyId) && lobbyCallback != null) {
            LobbyInfo lobby = lobbies.get(lobbyId);
            if (lobby != null) {
                lobbyCallback.onSuccess(lobby);
            }
        }
    }

    @Override
    public void removeLobbyListener() {
        this.listeningLobbyId = null;
        this.lobbyCallback = null;
    }

    @Override
    public void toggleReadyStatus(String lobbyId, String uid, Callback<Void> callback) {
        LobbyInfo lobby = lobbies.get(lobbyId);
        if (lobby == null) {
            callback.onError(new Exception("Lobby not found"));
            return;
        }

        PlayerInLobby player = lobby.players.get(uid);
        if (player == null) {
            callback.onError(new Exception("Player not found in lobby"));
            return;
        }

        player.isReady = player.isReady == null ? true : !player.isReady;
        callback.onSuccess(null);

        notifyLobbyListener(lobbyId);
    }

    @Override
    public void setLobbyLevel(String lobbyId, int level, Callback<Void> callback) {
        LobbyInfo lobby = lobbies.get(lobbyId);
        if (lobby != null) {
            lobby.level = level;
            callback.onSuccess(null);
            notifyLobbyListener(lobbyId);
        } else {
            callback.onError(new Exception("Lobby not found"));
        }
    }


    @Override
    public void startGame(String lobbyId, int level, Callback<Void> callback) {
        LobbyInfo lobby = lobbies.get(lobbyId);
        if (lobby == null) {
            callback.onError(new Exception("Lobby not found"));
            return;
        }

        lobby.status = "playing";
        lobby.level = level;
        callback.onSuccess(null);

        notifyLobbyListener(lobbyId);
        notifyPublicLobbiesListeners();
    }

    @Override
    public void setPlayerFinishData(String lobbyId, String uid, int score, double finishTime, Callback<Void> callback) {
        LobbyInfo lobby = lobbies.get(lobbyId);
        if (lobby != null && lobby.players.containsKey(uid)) {
            PlayerInLobby player = lobby.players.get(uid);
            player.finishScore = score;
            player.finishTime = finishTime;
            callback.onSuccess(null);
            notifyLobbyListener(lobbyId);
        } else {
            callback.onError(new Exception("Lobby or player not found"));
        }
    }

    @Override
    public void endGame(String lobbyId, String uid, Callback<Void> callback) {
        LobbyInfo lobby = lobbies.get(lobbyId);
        if (lobby == null) {
            callback.onError(new Exception("Lobby not found"));
            return;
        }

        PlayerInLobby player = lobby.players.get(uid);
        if (player == null) {
            callback.onError(new Exception("Player not found"));
            return;
        }

        player.isReady = false;

        boolean allFinished = true;
        for (PlayerInLobby p : lobby.players.values()) {
            if (Boolean.TRUE.equals(p.isReady)) {
                allFinished = false;
                break;
            }
        }

        lobby.status = allFinished ? "waiting" : "running";

        callback.onSuccess(null);
        notifyLobbyListener(lobbyId);
        notifyPublicLobbiesListeners();
    }

    private String generateLobbyCode() {
        return UUID.randomUUID().toString().substring(0, 5).replace("-", "");
    }
}
