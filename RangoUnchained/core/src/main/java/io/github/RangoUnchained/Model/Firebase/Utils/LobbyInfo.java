package io.github.RangoUnchained.Model.Firebase.Utils;


import java.util.HashMap;
import java.util.Map;

public class LobbyInfo {
    public String lobbyId;
    public String hostUid;
    public String status;
    public boolean isPublic;
    public int maxPlayers = 4;
    public Map<String, PlayerInLobby> players = new HashMap<>();

    // Default constructor for Firebase deserialization
    public LobbyInfo() {
        this.players = new HashMap<>();
        this.status = "waiting";
    }
    public LobbyInfo(String lobbyId, UserInfo host, boolean isPublic, int maxPlayers) {
        this.lobbyId = lobbyId;
        this.hostUid = host.uid;
        this.status = "waiting";
        this.isPublic = isPublic;
        this.maxPlayers = maxPlayers;
        this.players.put(host.uid, new PlayerInLobby(host));
    }

    public boolean isLobbyFull() {
        return players != null && players.size() >= maxPlayers;
    }

    public boolean isHost(String uid) {
        return uid != null && uid.equals(hostUid);
    }

    public boolean isAllPlayersReady() {
        for (PlayerInLobby p : this.players.values()) {
            if (p.isReady == null || !p.isReady) {
                return false;
            }
        }
        return true;
    }
}
