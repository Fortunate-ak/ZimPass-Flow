package com.zimpassflow.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import com.zimpassflow.R;
import com.zimpassflow.utils.PreferenceManager;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        PreferenceManager preferenceManager = new PreferenceManager(this);

        new Handler().postDelayed(() -> {
            if (preferenceManager.isLoggedIn()) {
                startActivity(new Intent(SplashActivity.this, DashboardActivity.class));
            } else if (!preferenceManager.isOnboardingFinished()) {
                startActivity(new Intent(SplashActivity.this, OnboardingActivity.class));
            } else {
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            }
            finish();
        }, 2000);
    }
}