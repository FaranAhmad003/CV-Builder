package com.example.cv_builder;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class CVScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cvscreen);

        // Initialize UI elements
        ImageView imgProfile = findViewById(R.id.imgProfile);
        TextView tvPersonalDetails = findViewById(R.id.tvPersonalDetails);
        TextView tvSummary = findViewById(R.id.tvSummary);
        LinearLayout layoutEducation = findViewById(R.id.layoutEducation);
        LinearLayout layoutExperience = findViewById(R.id.layoutExperience);
        LinearLayout layoutCertifications = findViewById(R.id.layoutCertifications);
        LinearLayout layoutReferences = findViewById(R.id.layoutReferences);

        // Fetch data from Intent safely
        Intent intent = getIntent();

        String profileImageUri = intent.hasExtra("profile_image") ? intent.getStringExtra("profile_image") : "";
        String personalDetails = intent.hasExtra("personal_details") ? intent.getStringExtra("personal_details") : "No personal details provided";
        String summary = intent.hasExtra("summary") ? intent.getStringExtra("summary") : "No summary provided";

        ArrayList<String> educationList = intent.hasExtra("education_list") ? intent.getStringArrayListExtra("education_list") : new ArrayList<>();
        ArrayList<String> experienceList = intent.hasExtra("experience_list") ? intent.getStringArrayListExtra("experience_list") : new ArrayList<>();
        ArrayList<String> certificationList = intent.hasExtra("certification_list") ? intent.getStringArrayListExtra("certification_list") : new ArrayList<>();
        ArrayList<String> referenceLinks = intent.hasExtra("reference_links") ? intent.getStringArrayListExtra("reference_links") : new ArrayList<>();
        ArrayList<String> referenceDocs = intent.hasExtra("reference_docs") ? intent.getStringArrayListExtra("reference_docs") : new ArrayList<>();

        // Set Profile Image
        if (!profileImageUri.isEmpty()) {
            imgProfile.setImageURI(Uri.parse(profileImageUri));
        }

        // Display Personal Details
        tvPersonalDetails.setText(personalDetails);

        // Display Summary
        tvSummary.setText(summary);

        // Display Education
        for (String edu : educationList) {
            TextView tv = new TextView(this);
            tv.setText(edu);
            layoutEducation.addView(tv);
        }

        // Display Experience
        for (String exp : experienceList) {
            TextView tv = new TextView(this);
            tv.setText(exp);
            layoutExperience.addView(tv);
        }

        // Display Certifications
        for (String cert : certificationList) {
            TextView tv = new TextView(this);
            tv.setText(cert);
            layoutCertifications.addView(tv);
        }

        // Display References (Links & Documents)
        for (String refLink : referenceLinks) {
            TextView tv = new TextView(this);
            tv.setText(refLink);
            layoutReferences.addView(tv);
        }

        for (String refDoc : referenceDocs) {
            TextView tv = new TextView(this);
            tv.setText(refDoc);
            layoutReferences.addView(tv);
        }
    }
}
