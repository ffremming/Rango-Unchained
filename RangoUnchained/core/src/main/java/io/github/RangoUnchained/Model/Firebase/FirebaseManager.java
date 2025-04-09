package io.github.RangoUnchained.Model.Firebase;

import java.util.List;

import io.github.RangoUnchained.Model.Firebase.Utils.ScoreInfo;
import io.github.RangoUnchained.Model.Firebase.Utils.UserInfo;

public interface FirebaseManager {
    interface Callback<T> {
        void onSuccess(T result);
        void onError(Exception e);
    }

    void loadScores(int level, Callback<List<ScoreInfo>> callback);
    void updateScoreForLevel(UserInfo userInfo, int level, int score, Callback<Boolean> callback);
    void uploadScoreToLeaderboard(UserInfo userInfo, int level, int score, Callback<Void> callback);
    void logIn(String email, String password, Callback<UserInfo> callback);
    void createUser(String email, String password, Callback<UserInfo> callback);
    void signOut();
    void checkIfUserExists(Callback<UserInfo> callback);
    void isUsernameAvailable(String username, Callback<Boolean> callback);
    void createUsername(String username, Callback<UserInfo> callback);
}
