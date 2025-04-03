package io.github.RangoUnchained.Model.Firebase;

import java.util.List;

public interface FirebaseManager {
    interface Callback<T> {
        void onSuccess(T result);
        void onError(Exception e);
    }

    void loadScores(Callback<List<Integer>> scores);
    void updateScore(Callback<List<Integer>> score);
    void logIn(String email, String password, Callback<UserInfo> callback);
    void createUser(String email, String password, Callback<UserInfo> callback);
    void signOut();
    void checkIfUserExists(Callback<UserInfo> callback);
    void isUsernameAvailable(String username, Callback<Boolean> callback);
    void createUsername(String username, Callback<UserInfo> callback);
}
