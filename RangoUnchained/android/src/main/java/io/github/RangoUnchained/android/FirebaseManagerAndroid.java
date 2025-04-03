package io.github.RangoUnchained.android;

import com.badlogic.gdx.Gdx;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.github.RangoUnchained.Model.Firebase.FirebaseManager;
import io.github.RangoUnchained.Model.Firebase.ScoreInfo;
import io.github.RangoUnchained.Model.Firebase.UserInfo;

public class FirebaseManagerAndroid implements FirebaseManager {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    public void loadScores(int level, Callback<List<ScoreInfo>> callback) {
        String levelDocId = "level_" + level;

        db.collection("scores").document(levelDocId)
            .get()
            .addOnSuccessListener(doc -> {
                List<ScoreInfo> scores = new ArrayList<>();

                if (doc.exists()) {
                    Map<String, Object> userScores = doc.getData();
                    if (userScores != null) {
                        for (Map.Entry<String, Object> entry : userScores.entrySet()) {
                            if (entry.getValue() instanceof Map) {
                                Map<String, Object> scoreData = (Map<String, Object>) entry.getValue();
                                String username = (String) scoreData.get("username");
                                if (username == null || username.isEmpty()) {
                                    username = (String) scoreData.get("email");
                                }
                                Number score = (Number) scoreData.get("score");
                                if (score != null) {
                                    scores.add(new ScoreInfo(username, score.intValue()));
                                }
                            }
                        }
                    }
                }

                scores.sort((a, b) -> Integer.compare(b.score, a.score));
                Gdx.app.postRunnable(() -> callback.onSuccess(scores));
            })
            .addOnFailureListener(e -> Gdx.app.postRunnable(() -> callback.onError(e)));
    }

    @Override
    public void updateScoreForLevel(UserInfo userInfo, int levelNumber, int newScore, Callback<Boolean> callback) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Gdx.app.postRunnable(() -> callback.onError(new Exception("No user is signed in")));
            return;
        }

        String uid = user.getUid();
        String levelKey = String.valueOf(levelNumber);

        db.collection("users").document(uid)
            .get()
            .addOnSuccessListener(doc -> {
                Map<String, Object> levelScores = (Map<String, Object>) doc.get("levelScores");
                int oldScore = 0;

                if (levelScores != null && levelScores.containsKey(levelKey)) {
                    Object storedScore = levelScores.get(levelKey);
                    if (storedScore instanceof Number) {
                        oldScore = ((Number) storedScore).intValue();
                    }
                }

                // Checks if new score is a high score
                if (newScore > oldScore) {
                    Map<String, Object> updates = new HashMap<>();
                    updates.put("levelScores." + levelKey, newScore);

                    db.collection("users").document(uid)
                        .update(updates)
                        .addOnSuccessListener(unused -> {
                            uploadScoreToLeaderboard(userInfo, levelNumber, newScore, new Callback<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Gdx.app.postRunnable(() -> callback.onSuccess(true));
                                }

                                @Override
                                public void onError(Exception e) {
                                    Gdx.app.postRunnable(() -> callback.onError(e));
                                }
                            });
                        })
                        .addOnFailureListener(e -> Gdx.app.postRunnable(() -> callback.onError(e)));
                } else {
                    // Do not update if new score is not a high score
                    Gdx.app.postRunnable(() -> callback.onSuccess(false));
                }
            })
            .addOnFailureListener(e -> Gdx.app.postRunnable(() -> callback.onError(e)));
    }

    @Override
    public void uploadScoreToLeaderboard(UserInfo userInfo, int level, int score, Callback<Void> callback) {
        if (userInfo == null || userInfo.uid == null || userInfo.email == null) {
            Gdx.app.postRunnable(() -> callback.onError(new Exception("User info is incomplete")));
            return;
        }

        String levelDocId = "level_" + level;
        String uid = userInfo.uid;

        Map<String, Object> userScore = new HashMap<>();
        userScore.put("score", score);
        userScore.put("username", userInfo.getDisplayName());
        userScore.put("email", userInfo.email);

        Map<String, Object> updateMap = new HashMap<>();
        updateMap.put(uid, userScore);

        db.collection("scores").document(levelDocId)
            .set(updateMap, com.google.firebase.firestore.SetOptions.merge())
            .addOnSuccessListener(unused -> Gdx.app.postRunnable(() -> callback.onSuccess(null)))
            .addOnFailureListener(e -> Gdx.app.postRunnable(() -> callback.onError(e)));
    }




    @Override
    public void logIn(String email, String password, Callback<UserInfo> callback) {
        if (email == null || email.isEmpty()) {
            callback.onError(new IllegalArgumentException("Email cannot be empty."));
            return;
        }
        if (password == null || password.isEmpty()) {
            callback.onError(new IllegalArgumentException("Password must be at least 6 characters."));
            return;
        }

        FirebaseAuth.getInstance()
            .signInWithEmailAndPassword(email, password)
            .addOnSuccessListener(authResult -> {
                checkIfUserExists(new Callback<UserInfo>() {
                    @Override
                    public void onSuccess(UserInfo userInfo) {
                        Gdx.app.postRunnable(() -> callback.onSuccess(userInfo));
                    }

                    @Override
                    public void onError(Exception e) {
                        Gdx.app.postRunnable(() -> callback.onError(e));
                    }
                });
            })

            .addOnFailureListener(e ->
                Gdx.app.postRunnable(() -> callback.onError(e)));
    }


    @Override
    public void createUser(String email, String password, Callback<UserInfo> callback) {
        if (email == null || email.isEmpty()) {
            callback.onError(new IllegalArgumentException("Email cannot be empty."));
            return;
        }
        if (password == null || password.length() < 6) {
            callback.onError(new IllegalArgumentException("Password must be at least 6 characters."));
            return;
        }
        if (!email.contains("@") || !email.contains(".") || email.indexOf('@') > email.lastIndexOf('.') || email.indexOf('@') == 0 || email.lastIndexOf('.') == email.length() - 1) {
            callback.onError(new IllegalArgumentException("Not a valid email address."));
            return;
        }

        FirebaseAuth.getInstance()
            .createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener(authResult -> {
                FirebaseUser firebaseUser = authResult.getUser();
                if (firebaseUser == null) {
                    Gdx.app.postRunnable(() -> callback.onError(new Exception("User creation failed")));
                    return;
                }

                String uid = firebaseUser.getUid();
                Map<String, Object> userData = new HashMap<>();
                userData.put("uid", uid);
                userData.put("email", email);

                db.collection("users").document(uid)
                    .set(userData)
                    .addOnSuccessListener(auth -> {
                        checkIfUserExists(new Callback<UserInfo>() {
                            @Override
                            public void onSuccess(UserInfo userInfo) {
                                Gdx.app.postRunnable(() -> callback.onSuccess(userInfo));
                            }

                            @Override
                            public void onError(Exception e) {
                                Gdx.app.postRunnable(() -> callback.onError(e));
                            }
                        });
                    })
                    .addOnFailureListener(e ->
                        Gdx.app.postRunnable(() -> callback.onError(e)));
            })
            .addOnFailureListener(e ->
                Gdx.app.postRunnable(() -> callback.onError(e)));
    }

    @Override
    public void signOut() {
        FirebaseAuth.getInstance().signOut();
    }

    @Override
    public void checkIfUserExists(Callback<UserInfo> callback) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Gdx.app.postRunnable(() -> callback.onError(new Exception("No user is signed in")));
            return;
        }

        String uid = user.getUid();
        String email = user.getEmail();

        db.collection("users").document(uid)
                .get()
                .addOnSuccessListener(doc -> {
                    String username = doc.contains("username") ? doc.getString("username") : null;
                    UserInfo userInfo = new UserInfo(uid, email, username);
                    Gdx.app.postRunnable(() -> callback.onSuccess(userInfo));
                })
                .addOnFailureListener(e -> Gdx.app.postRunnable(() -> callback.onError(e)));
    }

    @Override
    public void createUsername(String username, Callback<UserInfo> callback) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Gdx.app.postRunnable(() -> callback.onError(new Exception("No user is signed in")));
            return;
        }

        if (username == null || username.isEmpty()) {
            Gdx.app.postRunnable(() -> callback.onError(new Exception("Username cannot be empty")));
            return;
        }

        isUsernameAvailable(username, new FirebaseManager.Callback<Boolean>() {
            @Override
            public void onSuccess(Boolean available) {
                if (!available) {
                    Gdx.app.postRunnable(() -> callback.onError(new Exception("Username is already taken")));
                    return;
                }

                String uid = user.getUid();
                String email = user.getEmail();

                Map<String, Object> data = new HashMap<>();
                data.put("email", email);
                data.put("username", username);

                db.collection("users").document(uid)
                    .set(data)
                    .addOnSuccessListener(unused -> {
                        checkIfUserExists(new Callback<UserInfo>() {
                            @Override
                            public void onSuccess(UserInfo userInfo) {
                                db.collection("scores")
                                    .get()
                                    .addOnSuccessListener(snapshot -> {

                                        // Update the username in all level scores
                                        for (DocumentSnapshot levelDoc : snapshot) {
                                            String levelId = levelDoc.getId();
                                            Map<String, Object> data = levelDoc.getData();
                                            if (data != null && data.containsKey(uid)) {
                                                Map<String, Object> entry = (Map<String, Object>) data.get(uid);
                                                entry.put("username", username);
                                                data.put(uid, entry);
                                                db.collection("scores").document(levelId)
                                                    .update(uid, entry);
                                            }
                                        }
                                    });

                                Gdx.app.postRunnable(() -> callback.onSuccess(userInfo));
                            }

                            @Override
                            public void onError(Exception e) {
                                Gdx.app.postRunnable(() -> callback.onError(e));
                            }
                        });
                    })
                    .addOnFailureListener(e -> {
                        Gdx.app.postRunnable(() -> callback.onError(e));
                    });
            }

            @Override
            public void onError(Exception e) {
                Gdx.app.postRunnable(() -> callback.onError(e));
            }
        });
    }



    @Override
    public void isUsernameAvailable(String username, Callback<Boolean> callback) {
        db.collection("users")
                .whereEqualTo("username", username)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    boolean available = querySnapshot.isEmpty();
                    Gdx.app.postRunnable(() -> callback.onSuccess(available));
                })
                .addOnFailureListener(e -> Gdx.app.postRunnable(() -> callback.onError(e)));
    }
}
