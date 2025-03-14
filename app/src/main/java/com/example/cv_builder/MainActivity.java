package com.example.cv_builder;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "CVBuilderPrefs";
    private static final String PROFILE_IMAGE_URI = "profile_image_uri";

    private Button btnProfile, btnPersonalDetails, btnSummary, btnEducation, btnExperience, btnCertifications, btnReferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize buttons
        btnProfile = findViewById(R.id.btnProfile);
        btnPersonalDetails = findViewById(R.id.btnPersonalDetails);
        btnSummary = findViewById(R.id.btnSummary);
        btnEducation = findViewById(R.id.btnEducation);
        btnExperience = findViewById(R.id.btnExperience);
        btnCertifications = findViewById(R.id.btnCertifications);
        btnReferences = findViewById(R.id.btnReferences);

        // Open gallery when "Select Profile Picture" button is clicked
        btnProfile.setOnClickListener(v -> openGallery());

        // Navigate to Personal Details screen
        btnPersonalDetails.setOnClickListener(v -> openActivity(PersonalDetailsActivity.class));

        // Navigate to Summary screen (To be implemented)
       /* btnSummary.setOnClickListener(v -> openActivity(SummaryActivity.class));

        // Navigate to Education screen (To be implemented)
        btnEducation.setOnClickListener(v -> openActivity(EducationActivity.class));

        // Navigate to Experience screen (To be implemented)
        btnExperience.setOnClickListener(v -> openActivity(ExperienceActivity.class));

        // Navigate to Certifications screen (To be implemented)
        btnCertifications.setOnClickListener(v -> openActivity(CertificationsActivity.class));

        // Navigate to References screen (To be implemented)
        btnReferences.setOnClickListener(v -> openActivity(ReferencesActivity.class));*/
    }

    // Generic method to start a new activity
    private void openActivity(Class<?> activityClass) {
        Intent intent = new Intent(MainActivity.this, activityClass);
        startActivity(intent);
    }

    // Launcher for picking an image (without displaying it)
    private final ActivityResultLauncher<Intent> galleryLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri imageUri = result.getData().getData();
                        saveProfileImage(imageUri);
                    }
                }
            });

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        galleryLauncher.launch(intent);
    }

    // Save the selected image URI for future use
    private void saveProfileImage(Uri imageUri) {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PROFILE_IMAGE_URI, imageUri.toString());
        editor.apply();
    }
}
