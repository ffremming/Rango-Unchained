package io.github.RangoUnchained.Model.Firebase.Utils;

public class PlayerInLobby {
    public String uid;
    public String displayName;
    public Boolean isReady;
    public Integer finishScore;
    public Long finishTime;

    // Default constructor for Firebase deserialization
    public PlayerInLobby() {
        this.isReady = false;
        this.finishScore = null;
        this.finishTime = null;
    }

    public PlayerInLobby(UserInfo userInfo) {
        this.uid = userInfo.uid;
        this.displayName = userInfo.getDisplayName();
        this.isReady = false;
        this.finishScore = null;
        this.finishTime = null;
    }
}

