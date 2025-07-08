package org.bihe.finalproject.Activities;

import static org.bihe.finalproject.preferences.PreferencesManager.PREF_KEY_LOGGED;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import org.bihe.finalproject.preferences.PreferencesManager;
import org.bihe.finalproject.databinding.ActivitySplashBinding;

public class SplashActivity extends AppCompatActivity {

    private ActivitySplashBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        PreferencesManager preferencesManager = PreferencesManager.getInstance(getApplicationContext());
        boolean isLoggedIn = preferencesManager.get(PREF_KEY_LOGGED, false);
        Intent intent;
        if (isLoggedIn) {
            intent = new Intent(this, TransactionActivity.class);
        } else {
            intent = new Intent(this, LoginActivity.class);
        }
        startActivity(intent);
        finish();
        setContentView(view);

    }
}