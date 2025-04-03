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
import io.github.RangoUnchained.Model.Firebase.UserInfo;

public class FirebaseManagerAndroid implements FirebaseManager {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    public void loadScores(FirebaseManager.Callback<List<Integer>> callback) {
        // Fetch scores from Firestore
        db.collection("scores")
            .get()
            .addOnSuccessListener(querySnapshot -> {
                // Convert Firestore documents to a list of scores
                List<Integer> scores = new ArrayList<>();
                for (DocumentSnapshot doc : querySnapshot.getDocuments()) {
                    Long score = doc.getLong("score");
                    if (score != null) {
                        scores.add(score.intValue());
                    }
                }
                // Use the rendering thread to update the score-UI
                Gdx.app.postRunnable(() -> callback.onSuccess(scores));
            })
            .addOnFailureListener(e -> {
                Gdx.app.postRunnable(() -> callback.onError(e));
            });
    }

    @Override
    public void updateScore(Callback<List<Integer>> score) {
        // TODO: Implement updateScore
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
