package io.github.RangoUnchained.android;

import com.badlogic.gdx.Gdx;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import io.github.RangoUnchained.Model.Firebase.FirebaseManager;

public class FirebaseManagerAndroid implements FirebaseManager {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    public void loadScores(FirebaseManager.Callback callback) {
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
    public void updateScore(Integer score) {

    }
}
