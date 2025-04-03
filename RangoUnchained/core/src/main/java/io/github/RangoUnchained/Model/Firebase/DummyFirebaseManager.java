package io.github.RangoUnchained.Model.Firebase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class DummyFirebaseManager implements FirebaseManager {
    private String currentEmail = null;
    private String currentUid = null;
    private String currentUsername = null;

    // email -> password
    private static final Map<String, String> mockUserDatabase = new HashMap<>();
    // email -> uid
    private static final Map<String, String> mockUidLookup = new HashMap<>();
    // username -> email
    private static final Map<String, String> mockUsernameRegistry = new HashMap<>();

    static {
        // Initial test user
        String email = "test@example.com";
        String password = "password123";
        String uid = UUID.randomUUID().toString();

        mockUserDatabase.put(email, password);
        mockUidLookup.put(email, uid);
    }

    @Override
    public void loadScores(Callback<List<Integer>> callback) {
        List<Integer> mockScores = new ArrayList<>();
        mockScores.add(99999);
        mockScores.add(6969);
        mockScores.add(666);
        mockScores.add(420);
        mockScores.add(69);
        mockScores.add(0);
        callback.onSuccess(mockScores);
    }

    @Override
    public void updateScore(Callback<List<Integer>> score) {
        // Not implemented in dummy
    }

    @Override
    public void logIn(String email, String password, Callback<UserInfo> callback) {
        if (mockUserDatabase.containsKey(email) && mockUserDatabase.get(email).equals(password)) {
            currentEmail = email;
            currentUid = mockUidLookup.get(email);
            currentUsername = getUsernameForEmail(email);
            callback.onSuccess(new UserInfo(currentUid, currentEmail, currentUsername));
        } else {
            callback.onError(new Exception("Invalid email or password"));
        }
    }

    @Override
    public void createUser(String email, String password, Callback<UserInfo> callback) {
        if (mockUserDatabase.containsKey(email)) {
            callback.onError(new Exception("User already exists"));
        } else {
            String uid = UUID.randomUUID().toString();
            mockUserDatabase.put(email, password);
            mockUidLookup.put(email, uid);

            currentEmail = email;
            currentUid = uid;
            currentUsername = null;

            callback.onSuccess(new UserInfo(currentUid, currentEmail, null));
        }
    }

    @Override
    public void signOut() {
        currentEmail = null;
        currentUid = null;
        currentUsername = null;
    }

    @Override
    public void checkIfUserExists(Callback<UserInfo> callback) {
        if (currentEmail == null || currentUid == null) {
            callback.onError(new Exception("No user is signed in"));
            return;
        }

        String username = getUsernameForEmail(currentEmail);
        callback.onSuccess(new UserInfo(currentUid, currentEmail, username));
    }

    @Override
    public void isUsernameAvailable(String username, Callback<Boolean> callback) {
        boolean available = !mockUsernameRegistry.containsKey(username);
        callback.onSuccess(available);
    }

    @Override
    public void createUsername(String username, Callback<UserInfo> callback) {
        if (currentEmail == null || currentUid == null) {
            callback.onError(new Exception("No user is signed in"));
            return;
        }

        if (username == null || username.isEmpty()) {
            callback.onError(new Exception("Username cannot be empty"));
            return;
        }

        if (mockUsernameRegistry.containsKey(username)) {
            callback.onError(new Exception("Username is already taken"));
            return;
        }

        // Register username
        mockUsernameRegistry.put(username, currentEmail);
        currentUsername = username;

        // Return updated UserInfo
        callback.onSuccess(new UserInfo(currentUid, currentEmail, currentUsername));
    }

    private String getUsernameForEmail(String email) {
        for (Map.Entry<String, String> entry : mockUsernameRegistry.entrySet()) {
            if (entry.getValue().equals(email)) {
                return entry.getKey();
            }
        }
        return null;
    }
}
