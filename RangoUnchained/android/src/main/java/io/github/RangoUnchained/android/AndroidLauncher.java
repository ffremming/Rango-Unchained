package io.github.RangoUnchained.android;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

import io.github.RangoUnchained.Controllers.GameController;

/** Launches the Android application. */
public class AndroidLauncher extends AndroidApplication {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize Firebase
        com.google.firebase.FirebaseApp.initializeApp(this);

        // Platform specific FirebaseManager implementation
        FirebaseManagerAndroid firebaseManager = new FirebaseManagerAndroid();
        GameController.getInstance().setFirebaseManager(firebaseManager);

        AndroidApplicationConfiguration configuration = new AndroidApplicationConfiguration();
        configuration.useImmersiveMode = true; // Recommended, but not required.
        initialize(GameController.getInstance(), configuration);
    }
}
