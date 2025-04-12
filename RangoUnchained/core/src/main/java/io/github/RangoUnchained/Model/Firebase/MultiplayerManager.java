package io.github.RangoUnchained.Model.Firebase;

import java.util.List;

import io.github.RangoUnchained.Model.Firebase.Utils.LobbyInfo;
import io.github.RangoUnchained.Model.Firebase.Utils.UserInfo;

public interface MultiplayerManager {
    interface Callback<T> {
        void onSuccess(T result);

        void onError(Exception e);
    }

    void createLobby(UserInfo host, Boolean isPublic, int maxPlayers, Callback<LobbyInfo> callback);
    void joinLobby(String lobbyId, UserInfo player, Callback<LobbyInfo> callback);
    void leaveLobby(String lobbyId, UserInfo player, Callback<Void> callback);
    void fetchPublicLobbies(Callback<List<LobbyInfo>> callback);
    void removePublicLobbiesListener();
    void listenToLobby(String lobbyId, Callback<LobbyInfo> callback);
    void removeLobbyListener();
    void toggleReadyStatus(String lobbyId, String uid, Callback<Void> callback);
    void setLobbyLevel(String lobbyId, int level, Callback<Void> callback);

    void startGame(String lobbyId, int level, Callback<Void> callback);
    void setPlayerFinishData(String lobbyId, String uid, int score, double finishTime, Callback<Void> callback);
    void endGame(String lobbyId, String uid, Callback<Void> callback);
}
