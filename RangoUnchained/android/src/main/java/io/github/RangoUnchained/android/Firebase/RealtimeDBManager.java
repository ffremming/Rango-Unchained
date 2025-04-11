package io.github.RangoUnchained.android.Firebase;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.HashMap;
import java.util.Map;

import io.github.RangoUnchained.Model.Firebase.MultiplayerManager;
import io.github.RangoUnchained.Model.Firebase.Utils.LobbyInfo;
import io.github.RangoUnchained.Model.Firebase.Utils.PlayerInLobby;
import io.github.RangoUnchained.Model.Firebase.Utils.UserInfo;

public class RealtimeDBManager implements MultiplayerManager {
    private final DatabaseReference lobbiesRef = FirebaseDatabase.getInstance().getReference().child("lobbies");
    @Override
    public void createLobby(UserInfo host, Boolean isPublic, int maxPlayers, Callback<LobbyInfo> callback) {
        String lobbyId = generateLobbyCode();
        LobbyInfo lobby = new LobbyInfo(lobbyId, host, isPublic, maxPlayers);

        lobbiesRef
            .child(lobbyId)
            .setValue(lobby)
            .addOnSuccessListener(unused -> callback.onSuccess(lobby))
            .addOnFailureListener(callback::onError);
    }

    @Override
    public void joinLobby(String lobbyId, UserInfo player, Callback<LobbyInfo> callback) {
        PlayerInLobby newPlayer = new PlayerInLobby(player);

        DatabaseReference lobbyRef = lobbiesRef.child(lobbyId);

        lobbyRef.child("players")
            .child(player.uid)
            .setValue(newPlayer)
            .addOnSuccessListener(unused -> {
                // After successfully adding the player, fetch the updated LobbyInfo
                lobbyRef.get().addOnSuccessListener(snapshot -> {
                    LobbyInfo updatedLobby = snapshot.getValue(LobbyInfo.class);
                    if (updatedLobby != null) {
                        callback.onSuccess(updatedLobby);
                    } else {
                        callback.onError(new Exception("Failed to fetch updated lobby info."));
                    }
                }).addOnFailureListener(callback::onError);
            })
            .addOnFailureListener(callback::onError);
    }

    @Override
    public void leaveLobby(String lobbyId, UserInfo player, Callback<Void> callback) {
        lobbiesRef
            .child(lobbyId)
            .child("players")
            .child(player.uid)
            .removeValue()
            .addOnSuccessListener(unused -> {
                if (lobbyRef != null && lobbyListener != null) {
                    lobbyRef.removeEventListener(lobbyListener);
                    lobbiesRef.removeEventListener(publicLobbiesListener);
                }
                callback.onSuccess(null);
            })
            .addOnFailureListener(callback::onError);
    }

    // Store reference to dispose of the listener afterwards
    private ValueEventListener publicLobbiesListener;
    public void fetchPublicLobbies(Callback<List<LobbyInfo>> callback) {
        publicLobbiesListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                List<LobbyInfo> publicLobbies = new ArrayList<>();
                for (DataSnapshot lobbySnap : snapshot.getChildren()) {
                    LobbyInfo lobby = lobbySnap.getValue(LobbyInfo.class);
                    if (lobby != null && lobby.isPublic && lobby.players != null &&
                        lobby.players.size() < lobby.maxPlayers && "waiting".equals(lobby.status)) {
                        publicLobbies.add(lobby);
                    }
                }
                callback.onSuccess(publicLobbies);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                callback.onError(error.toException());
            }
        };

        lobbiesRef.addValueEventListener(publicLobbiesListener);
    }

    @Override
    public void removePublicLobbiesListener() {
        // Disposes of the listener when it's no longer needed
        if (publicLobbiesListener != null) {
            lobbiesRef.removeEventListener(publicLobbiesListener);
        }
    }

    // Store reference to dispose of the listener afterwards
    private ValueEventListener lobbyListener;
    private DatabaseReference lobbyRef;
    @Override
    public void listenToLobby(String lobbyId, Callback<LobbyInfo> callback) {
        lobbyRef = lobbiesRef.child(lobbyId);

        lobbyListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                LobbyInfo lobby = snapshot.getValue(LobbyInfo.class);
                if (lobby != null) {
                    callback.onSuccess(lobby);
                } else {
                    callback.onError(new Exception("Lobby data is null"));
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                callback.onError(error.toException());
            }
        };

        lobbyRef.addValueEventListener(lobbyListener);
    }

    @Override
    public void removeLobbyListener() {
        // Disposes of the listener when it's no longer needed
        if (lobbyRef != null && lobbyListener != null) {
            lobbyRef.removeEventListener(lobbyListener);
        }
    }

    @Override
    public void toggleReadyStatus(String lobbyId, String uid, Callback<Void> callback) {
        DatabaseReference ref = lobbiesRef
            .child(lobbyId)
            .child("players")
            .child(uid)
            .child("isReady");

        ref.get().addOnSuccessListener(snapshot -> {
            Boolean currentStatus = snapshot.getValue(Boolean.class);
            boolean newStatus = currentStatus == null || !currentStatus;

            ref.setValue(newStatus)
                .addOnSuccessListener(aVoid -> callback.onSuccess(null))
                .addOnFailureListener(callback::onError);
        }).addOnFailureListener(callback::onError);
    }

    @Override
    public void setLobbyLevel(String lobbyId, int level, Callback<Void> callback) {
        DatabaseReference ref = lobbiesRef
            .child(lobbyId).
            child("level");

        ref.setValue(level)
            .addOnSuccessListener(unused -> callback.onSuccess(null))
            .addOnFailureListener(e -> callback.onError(e));
    }

    @Override
    public void startGame(String lobbyId, int level, Callback<Void> callback) {
        DatabaseReference ref = lobbiesRef.child(lobbyId);

        // Update the lobby status and level
        Map<String, Object> updates = new HashMap<>();
        updates.put("status", "playing");
        updates.put("level", level);

        ref.updateChildren(updates)
            .addOnSuccessListener(unused -> callback.onSuccess(null))
            .addOnFailureListener(callback::onError);
    }

    @Override
    public void setPlayerFinishData(String lobbyId, String uid, int score, double finishTime, Callback<Void> callback) {
        Map<String, Object> updates = new HashMap<>();
        updates.put("finishScore", score);
        updates.put("finishTime", finishTime);

        DatabaseReference ref = lobbiesRef
            .child(lobbyId)
            .child("players")
            .child(uid);

        ref.updateChildren(updates)
            .addOnSuccessListener(unused -> callback.onSuccess(null))
            .addOnFailureListener(callback::onError);
    }

    @Override
    public void endGame(String lobbyId, String uid, Callback<Void> callback) {
        DatabaseReference lobbyRef = lobbiesRef.child(lobbyId);

        // Set the player's isReady status to false when they finish the game
        lobbyRef.child("players").child(uid).child("isReady").setValue(false)
            .addOnSuccessListener(unused -> {
                // Check if all players have finished the game
                lobbyRef.child("players").get().addOnSuccessListener(snapshot -> {
                    boolean allFinished = false;

                    if (snapshot.exists()) {
                        for (DataSnapshot playerSnapshot : snapshot.getChildren()) {
                            Boolean isReady = playerSnapshot.child("isReady").getValue(Boolean.class);
                            if (Boolean.TRUE.equals(isReady)) {
                                allFinished = true;
                                break;
                            }
                        }
                    }

                    // Update the lobby status based on whether all players have finished
                    String newStatus = !allFinished ? "waiting" : "running";

                    lobbyRef.child("status")
                        .setValue(newStatus)
                        .addOnSuccessListener(done -> callback.onSuccess(null))
                        .addOnFailureListener(callback::onError);

                }).addOnFailureListener(callback::onError);
            })
            .addOnFailureListener(callback::onError);
    }

    // Utility method to generate a short lobby code (5 chars)
    private String generateLobbyCode() {
        return UUID.randomUUID().toString().substring(0, 5).replace("-", "");
    }

}
