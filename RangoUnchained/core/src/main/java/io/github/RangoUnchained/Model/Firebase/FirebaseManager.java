package io.github.RangoUnchained.Model.Firebase;

import java.util.List;

public interface FirebaseManager {
    interface Callback {
        void onSuccess(List<Integer> scores);
        void onError(Exception e);
    }
    void loadScores(Callback callback);
    void updateScore(Integer score);
}
