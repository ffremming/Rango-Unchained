package io.github.RangoUnchained.Model.Firebase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import io.github.RangoUnchained.Model.Firebase.Utils.ScoreInfo;
import io.github.RangoUnchained.Model.Firebase.Utils.UserInfo;

public class DummyFirebaseManager implements FirebaseManager {
    private String currentEmail = null;
    private String currentUid = null;
    private String currentUsername = null;

    // Mocked firestore data structures
    // Map<email, password>
    private static final Map<String, String> mockUserDatabase = new HashMap<>();
    // Map<email, uid>
    private static final Map<String, String> mockUidLookup = new HashMap<>();
    // Map<username, email>
    private static final Map<String, String> mockUsernameRegistry = new HashMap<>();
    // Map<uid, { level, score }>
    private static final Map<String, Map<String, Integer>> levelScores = new HashMap<>();
    // Map<level, Map<uid, {score, username, email}>>
    private static final Map<String, Map<String, Map<String, Object>>> leaderboard = new HashMap<>();


    static {
        // Initial test user
        String email = "test@example.com";
        String password = "password123";
        String uid = UUID.randomUUID().toString();

        mockUserDatabase.put(email, password);
        mockUidLookup.put(email, uid);
    }

    @Override
    public void loadScores(int level, Callback<List<ScoreInfo>> callback) {
        String levelKey = "level_" + level;
        List<ScoreInfo> mockScores = new ArrayList<>();

        Map<String, Map<String, Object>> levelEntries = leaderboard.get(levelKey);
        if (levelEntries != null) {
            for (Map<String, Object> userEntry : levelEntries.values()) {
                String username = (String) userEntry.get("username");
                String email = (String) userEntry.get("email");
                Number score = (Number) userEntry.get("score");

                if (score != null) {
                    String name = (username != null && !username.isEmpty()) ? username : email;
                    mockScores.add(new ScoreInfo(name, score.intValue()));
                }
            }
        }

        // Sort descending
        mockScores.sort((a, b) -> Integer.compare(b.score, a.score));
        callback.onSuccess(mockScores);
    }



    @Override
    public void updateScoreForLevel(UserInfo userInfo, int levelNumber, int newScore, Callback<Boolean> callback) {
        if (userInfo == null || userInfo.uid == null) {
            callback.onError(new Exception("Invalid user info"));
            return;
        }

        String uid = userInfo.uid;
        String levelKey = String.valueOf(levelNumber);
        levelScores.putIfAbsent(uid, new HashMap<>());
        int oldScore = levelScores.get(uid).getOrDefault(levelKey, 0);

        if (newScore > oldScore) {
            levelScores.get(uid).put(levelKey, newScore);
            uploadScoreToLeaderboard(userInfo, levelNumber, newScore, new Callback<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    callback.onSuccess(true);
                }

                @Override
                public void onError(Exception e) {
                    callback.onError(e);
                }
            });
        } else {
            callback.onSuccess(false);
        }
    }

    @Override
    public void uploadScoreToLeaderboard(UserInfo userInfo, int level, int score, Callback<Void> callback) {
        if (userInfo == null || userInfo.uid == null || userInfo.email == null) {
            callback.onError(new Exception("User info is incomplete"));
            return;
        }

        String levelKey = "level_" + level;
        String uid = userInfo.uid;

        Map<String, Object> userEntry = new HashMap<>();
        userEntry.put("score", score);
        userEntry.put("username", userInfo.getDisplayName());
        userEntry.put("email", userInfo.email);

        leaderboard.putIfAbsent(levelKey, new HashMap<>());
        leaderboard.get(levelKey).put(uid, userEntry);

        callback.onSuccess(null);
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

        mockUsernameRegistry.put(username, currentEmail);
        currentUsername = username;

        for (Map<String, Map<String, Object>> level : leaderboard.values()) {
            if (level.containsKey(currentUid)) {
                Map<String, Object> entry = level.get(currentUid);
                entry.put("username", username);
            }
        }

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
