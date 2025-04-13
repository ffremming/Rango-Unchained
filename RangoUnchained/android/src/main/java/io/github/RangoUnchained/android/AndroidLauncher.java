package io.github.RangoUnchained.android;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

import io.github.RangoUnchained.Controllers.GameController;
import io.github.RangoUnchained.android.Firebase.FirebaseManagerAndroid;
import io.github.RangoUnchained.android.Firebase.RealtimeDBManager;

/** Launches the Android application. */
public class AndroidLauncher extends AndroidApplication {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize Firebase
        com.google.firebase.FirebaseApp.initializeApp(this);

        // Set the FirebaseManager in the GameController
        GameController.getInstance().setFirebaseManager(new FirebaseManagerAndroid());
        // Set the MultiplayerManager in the GameController
        GameController.getInstance().setMultiplayerManager(new RealtimeDBManager());

        AndroidApplicationConfiguration configuration = new AndroidApplicationConfiguration();
        configuration.useImmersiveMode = true; // Recommended, but not required.
        initialize(GameController.getInstance(), configuration);
    }
}
