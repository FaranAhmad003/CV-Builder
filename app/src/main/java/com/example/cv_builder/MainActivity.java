package com.example.cv_builder;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initializing buttons
        Button btnProfile = findViewById(R.id.btnProfile);
        Button btnPersonalDetails = findViewById(R.id.btnPersonalDetails);
        Button btnSummary = findViewById(R.id.btnSummary);
        Button btnEducation = findViewById(R.id.btnEducation);
        Button btnExperience = findViewById(R.id.btnExperience);
        Button btnCertifications = findViewById(R.id.btnCertifications);
        Button btnReferences = findViewById(R.id.btnReferences);

        // Set click listeners for navigation
       /* btnProfile.setOnClickListener(v -> navigateTo(ProfileActivity.class));
        btnPersonalDetails.setOnClickListener(v -> navigateTo(PersonalDetailsActivity.class));
        btnSummary.setOnClickListener(v -> navigateTo(SummaryActivity.class));
        btnEducation.setOnClickListener(v -> navigateTo(EducationActivity.class));
        btnExperience.setOnClickListener(v -> navigateTo(ExperienceActivity.class));
        btnCertifications.setOnClickListener(v -> navigateTo(CertificationsActivity.class));
        btnReferences.setOnClickListener(v -> navigateTo(ReferencesActivity.class));*/
    }

    // Method to start an activity
    private void navigateTo(Class<?> activityClass) {
        Intent intent = new Intent(MainActivity.this, activityClass);
        startActivity(intent);
    }
}
