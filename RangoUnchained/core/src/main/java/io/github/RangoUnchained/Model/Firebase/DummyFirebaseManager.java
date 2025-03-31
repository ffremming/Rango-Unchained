package io.github.RangoUnchained.Model.Firebase;

import java.util.ArrayList;
import java.util.List;

import io.github.RangoUnchained.Model.Firebase.FirebaseManager;

public class DummyFirebaseManager implements FirebaseManager {
    @Override
    public void loadScores(Callback callback) {
        // Simulate some fake data for testing
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
    public void updateScore(Integer score) {

    }
}
