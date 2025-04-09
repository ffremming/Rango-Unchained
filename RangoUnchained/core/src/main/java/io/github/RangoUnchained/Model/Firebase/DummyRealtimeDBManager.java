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

    @Override
    public void createLobby(UserInfo host, Boolean isPublic, int maxPlayers, Callback<LobbyInfo> callback) {
        String lobbyId = generateLobbyCode();
        LobbyInfo lobbyInfo = new LobbyInfo(lobbyId, host, isPublic, maxPlayers);
        lobbies.put(lobbyId, lobbyInfo);
        callback.onSuccess(lobbyInfo);
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
        callback.onSuccess(lobby); // Return updated lobby
    }

    @Override
    public void leaveLobby(String lobbyId, UserInfo player, Callback<Void> callback) {
        LobbyInfo lobby = lobbies.get(lobbyId);
        if (lobby != null) {
            lobby.players.remove(player.uid);
            callback.onSuccess(null);
        } else {
            callback.onError(new Exception("Lobby not found"));
        }
    }

    @Override
    public void fetchPublicLobbies(Callback<List<LobbyInfo>> callback) {
        List<LobbyInfo> publicLobbies = new ArrayList<>();
        for (LobbyInfo lobby : lobbies.values()) {
            if (lobby.isPublic && !lobby.isLobbyFull()) {
                publicLobbies.add(lobby);
            }
        }
        callback.onSuccess(publicLobbies);
    }

    @Override
    public void listenToLobby(String lobbyId, Callback<LobbyInfo> callback) {
        LobbyInfo lobby = lobbies.get(lobbyId);
        if (lobby != null) {
            callback.onSuccess(lobby);
        } else {
            callback.onError(new Exception("Lobby not found"));
        }
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
    }

    @Override
    public void startGame(String lobbyId, Callback<Void> callback) {
        LobbyInfo lobby = lobbies.get(lobbyId);
        if (lobby == null) {
            callback.onError(new Exception("Lobby not found"));
            return;
        }

        lobby.status = "playing";
        callback.onSuccess(null);
    }

    private String generateLobbyCode() {
        return UUID.randomUUID().toString().substring(0, 5).replace("-", "");
    }
}
