package com.example.cv_builder;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Calendar;

public class EducationActivity extends AppCompatActivity {

    private RadioGroup radioGroupEducationLevel;
    private Spinner spinnerYear;
    private EditText etInstitution;
    private Button btnAddMore, btnSaveEducation, btnCancelEducation;

    private static final String PREFS_NAME = "CVBuilderPrefs";
    private static final String EDUCATION_KEY = "education_details";
    private ArrayList<String> educationList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_education);

        // Initialize UI Elements
        radioGroupEducationLevel = findViewById(R.id.radioGroupEducationLevel);
        spinnerYear = findViewById(R.id.spinnerYear);
        etInstitution = findViewById(R.id.etInstitution);
        btnAddMore = findViewById(R.id.btnAddMore);
        btnSaveEducation = findViewById(R.id.btnSaveEducation);
        btnCancelEducation = findViewById(R.id.btnCancelEducation);

        // Populate Year Spinner with years from 1980 to the current year
        ArrayList<String> years = new ArrayList<>();
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int year = 1980; year <= currentYear; year++) {
            years.add(String.valueOf(year));
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, years);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerYear.setAdapter(adapter);

        // Load saved education details
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        educationList.add(sharedPreferences.getString(EDUCATION_KEY, ""));

        // Add More Education
        btnAddMore.setOnClickListener(v -> addEducation());

        // Save Education
        btnSaveEducation.setOnClickListener(v -> {
            addEducation();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(EDUCATION_KEY, educationList.toString());
            editor.apply();
            Toast.makeText(EducationActivity.this, "Education Details Saved!", Toast.LENGTH_SHORT).show();
            finish();
        });

        // Cancel Button
        btnCancelEducation.setOnClickListener(v -> finish());
    }

    private void addEducation() {
        int selectedId = radioGroupEducationLevel.getCheckedRadioButtonId();
        RadioButton selectedRadioButton = findViewById(selectedId);
        String educationLevel = selectedRadioButton != null ? selectedRadioButton.getText().toString() : "Not Selected";
        String year = spinnerYear.getSelectedItem().toString();
        String institution = etInstitution.getText().toString().trim();

        if (institution.isEmpty()) {
            Toast.makeText(this, "Please enter institution name!", Toast.LENGTH_SHORT).show();
            return;
        }

        String educationEntry = educationLevel + " - " + year + " - " + institution;
        educationList.add(educationEntry);
        Toast.makeText(this, "Education Added!", Toast.LENGTH_SHORT).show();
        etInstitution.setText("");
    }
}
