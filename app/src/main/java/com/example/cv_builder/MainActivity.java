package com.example.cv_builder;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "CVBuilderPrefs";
    private static final String PROFILE_IMAGE_URI = "profile_image_uri";
    private static final String PERSONAL_DETAILS_KEY = "personal_details";
    private static final String SUMMARY_KEY = "summary_text";
    private static final String EDUCATION_DETAILS_KEY = "education_details";
    private static final String EXPERIENCE_DETAILS_KEY = "experience_details";
    private static final String CERTIFICATION_DETAILS_KEY = "certification_details";
    private static final String REFERENCE_LINKS_KEY = "reference_links";
    private static final String REFERENCE_DOCS_KEY = "reference_documents";

    private Button btnProfile, btnPersonalDetails, btnSummary, btnEducation, btnExperience, btnCertifications, btnReferences, btnSubmit;

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
        btnSubmit = findViewById(R.id.btnSubmit);

        // Open gallery when "Select Profile Picture" button is clicked
        btnProfile.setOnClickListener(v -> openGallery());

        // Navigate to different sections
        btnPersonalDetails.setOnClickListener(v -> openActivity(PersonalDetailsActivity.class));
        btnSummary.setOnClickListener(v -> openActivity(SummaryActivity.class));
        btnEducation.setOnClickListener(v -> openActivity(EducationActivity.class));
        btnExperience.setOnClickListener(v -> openActivity(ExperienceActivity.class));
        btnCertifications.setOnClickListener(v -> openActivity(CertificationsActivity.class));
        btnReferences.setOnClickListener(v -> openActivity(ReferencesActivity.class));

        // Submit CV Data
        btnSubmit.setOnClickListener(v -> submitCV());
    }

    // Generic method to start a new activity
    private void openActivity(Class<?> activityClass) {
        Intent intent = new Intent(MainActivity.this, activityClass);
        startActivity(intent);
    }

    // ActivityResultLauncher for picking an image
    private final ActivityResultLauncher<Intent> galleryLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri imageUri = result.getData().getData();
                    saveProfileImage(imageUri);
                }
            });

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        galleryLauncher.launch(intent);
    }

    // Save the selected image URI for future use
    private void saveProfileImage(Uri imageUri) {
        if (imageUri != null) {
            SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(PROFILE_IMAGE_URI, imageUri.toString());
            editor.apply();
            Toast.makeText(this, "Profile picture saved!", Toast.LENGTH_SHORT).show();
        }
    }

    private void submitCV() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        // Fetch Profile Image
        String profileImageUri = sharedPreferences.getString(PROFILE_IMAGE_URI, "");

        // Fetch Personal Details
        String personalDetails = sharedPreferences.getString(PERSONAL_DETAILS_KEY, "No personal details provided");

        // Fetch Summary
        String summary = sharedPreferences.getString(SUMMARY_KEY, "No summary provided");

        // Fetch Education
        Set<String> educationSet = sharedPreferences.getStringSet(EDUCATION_DETAILS_KEY, new HashSet<>());
        ArrayList<String> educationList = educationSet != null ? new ArrayList<>(educationSet) : new ArrayList<>();

        // Fetch Experience
        Set<String> experienceSet = sharedPreferences.getStringSet(EXPERIENCE_DETAILS_KEY, new HashSet<>());
        ArrayList<String> experienceList = experienceSet != null ? new ArrayList<>(experienceSet) : new ArrayList<>();

        // Fetch Certifications
        Set<String> certificationSet = sharedPreferences.getStringSet(CERTIFICATION_DETAILS_KEY, new HashSet<>());
        ArrayList<String> certificationList = certificationSet != null ? new ArrayList<>(certificationSet) : new ArrayList<>();

        // Fetch References (Links & Documents)
        Set<String> referenceLinksSet = sharedPreferences.getStringSet(REFERENCE_LINKS_KEY, new HashSet<>());
        ArrayList<String> referenceLinksList = referenceLinksSet != null ? new ArrayList<>(referenceLinksSet) : new ArrayList<>();

        Set<String> referenceDocsSet = sharedPreferences.getStringSet(REFERENCE_DOCS_KEY, new HashSet<>());
        ArrayList<String> referenceDocsList = referenceDocsSet != null ? new ArrayList<>(referenceDocsSet) : new ArrayList<>();

        // Debugging Logs
        if (profileImageUri.isEmpty()) {
            Toast.makeText(this, "No profile image selected", Toast.LENGTH_SHORT).show();
        }

        // Start CVScreenActivity & Pass Data
        Intent intent = new Intent(MainActivity.this, CVScreenActivity.class);
        intent.putExtra("profile_image", profileImageUri);
        intent.putExtra("personal_details", personalDetails);
        intent.putExtra("summary", summary);
        intent.putStringArrayListExtra("education_list", educationList);
        intent.putStringArrayListExtra("experience_list", experienceList);
        intent.putStringArrayListExtra("certification_list", certificationList);
        intent.putStringArrayListExtra("reference_links", referenceLinksList);
        intent.putStringArrayListExtra("reference_docs", referenceDocsList);

        startActivity(intent);
    }
}
