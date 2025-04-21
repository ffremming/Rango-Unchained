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
    // Store reference to dispose of the listener afterwards
    private ValueEventListener lobbyListener;
    private DatabaseReference lobbyRef;
    private static final long INACTIVE_TIMEOUT = 60; // In minutes
    
    @Override
    public void createLobby(UserInfo host, Boolean isPublic, int maxPlayers, Callback<LobbyInfo> callback) {
        String lobbyId = generateLobbyCode();
        LobbyInfo lobby = new LobbyInfo(lobbyId, host, isPublic, maxPlayers);

        // Add disconnect listener for the host
        addDisconnectListener(lobbyId, host);

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

        // Add disconnect listener for the player
        addDisconnectListener(lobbyId, player);

        Map<String, Object> updates = new HashMap<>();
        updates.put("players/" + player.uid, newPlayer);
        updates.put("timeInState", System.currentTimeMillis());

        lobbyRef.updateChildren(updates)
            .addOnSuccessListener(unused -> {
                // After updating, fetch the updated LobbyInfo
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
        DatabaseReference lobbyRef  = lobbiesRef.child(lobbyId);
        DatabaseReference playerRef = lobbyRef.child("players").child(player.uid);

        // Stop the queued on‑disconnect delete for this player
        playerRef.onDisconnect().cancel();

        // Remove the player immediately
        playerRef.removeValue()
            .addOnSuccessListener(unused -> {

                // If the lobby is now empty, remove the lobby
                lobbyRef.child("players").get().addOnSuccessListener(snapshot -> {
                    if (!snapshot.hasChildren()) {
                        lobbyRef.removeValue();
                    }
                });

                // Clean up local listeners
                if (this.lobbyRef != null && lobbyListener != null) {
                    this.lobbyRef.removeEventListener(lobbyListener);
                }
                if (publicLobbiesListener != null) {
                    lobbiesRef.removeEventListener(publicLobbiesListener);
                }

                callback.onSuccess(null);
            })
            .addOnFailureListener(callback::onError);
    }

    // Store reference to dispose of the listener afterwards
    private ValueEventListener publicLobbiesListener;
    @Override
    public void fetchPublicLobbies(Callback<List<LobbyInfo>> callback) {
        publicLobbiesListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                List<LobbyInfo> publicLobbies = new ArrayList<>();
                for (DataSnapshot lobbySnap : snapshot.getChildren()) {
                    LobbyInfo lobby = lobbySnap.getValue(LobbyInfo.class);

                    // Delete lobby if it has been inactive in a state for too long
                    if (shouldDeleteLobby(lobby, lobbySnap.getRef())) continue;

                    if (lobby.isPublic && lobby.players != null && lobby.players.size() < lobby.maxPlayers && "waiting".equals(lobby.status)) {
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

    @Override
    public void listenToLobby(String lobbyId, Callback<LobbyInfo> callback) {
        lobbyRef = lobbiesRef.child(lobbyId);

        lobbyListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                LobbyInfo lobby = snapshot.getValue(LobbyInfo.class);

                // Delete lobby if it has been inactive in a state for too long
                if (shouldDeleteLobby(lobby, lobbyRef)) {
                    lobby = null;
                    callback.onError(new Exception("Lobby has been inactive for too long."));
                }

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
                .addOnSuccessListener(aVoid -> {
                    // Update timeInState when ready status changes
                    lobbiesRef.child(lobbyId).child("timeInState").setValue(System.currentTimeMillis());
                    callback.onSuccess(null);
                })
                .addOnFailureListener(callback::onError);
        }).addOnFailureListener(callback::onError);
    }

    @Override
    public void setLobbyLevel(String lobbyId, int level, Callback<Void> callback) {
        DatabaseReference ref = lobbiesRef.child(lobbyId);

        Map<String, Object> updates = new HashMap<>();
        updates.put("level", level);
        // Reset timeInState when level is set
        updates.put("timeInState", System.currentTimeMillis());

        ref.updateChildren(updates)
            .addOnSuccessListener(unused -> callback.onSuccess(null))
            .addOnFailureListener(callback::onError);
    }

    @Override
    public void startGame(String lobbyId, int level, Callback<Void> callback) {
        DatabaseReference ref = lobbiesRef.child(lobbyId);

        // Update the lobby status and level
        Map<String, Object> updates = new HashMap<>();
        updates.put("status", "running");
        updates.put("level", level);
        updates.put("timeInState", System.currentTimeMillis());

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

                    Map<String, Object> updates = new HashMap<>();
                    updates.put("status", newStatus);

                    // Reset timeInState if all players have finished"
                    if ("waiting".equals(newStatus)) {
                        updates.put("timeInState", System.currentTimeMillis());
                    }

                    lobbyRef.updateChildren(updates)
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

    // Utility method to delete lobbies
    private boolean shouldDeleteLobby(LobbyInfo lobby, DatabaseReference ref) {
        // Delete lobby if it doesn't exist
        if (lobby == null) {
            ref.removeValue();
            return true;
        }

        long now = System.currentTimeMillis();

        // Check if lobby has been inactive for INACTIVE_TIME (in minutes)
        if (lobby.timeInState != null && lobby.timeInState != 0 &&
            now - lobby.timeInState > INACTIVE_TIMEOUT * 60 * 1000) {
            ref.removeValue();
            return true;
        }

        // Check if all players have disconnected
        if (lobby.players == null || lobby.players.isEmpty()) {
            ref.removeValue();
            return true;
        }

        return false;
    }

    public void addDisconnectListener(String lobbyId, UserInfo player) {
        // Define a disconnectRef for this player
        DatabaseReference playerRef = lobbiesRef
            .child(lobbyId)
            .child("players")
            .child(player.uid);

        DatabaseReference connectedRef =
            FirebaseDatabase.getInstance().getReference(".info/connected");

        connectedRef.addValueEventListener(new ValueEventListener() {
            @Override public void onDataChange(DataSnapshot snap) {
                Boolean connected = snap.getValue(Boolean.class);
                if (Boolean.TRUE.equals(connected)) {
                    // Delete player from lobby when disconnected
                    playerRef.onDisconnect().removeValue();
                }
            }
            @Override public void onCancelled(DatabaseError error) {}
        });
    }
}
