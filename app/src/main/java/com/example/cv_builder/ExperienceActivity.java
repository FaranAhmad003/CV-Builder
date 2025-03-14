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

public class ExperienceActivity extends AppCompatActivity {

    private EditText etJobTitle, etCompanyName;
    private RadioGroup radioGroupJobType;
    private Spinner spinnerStartYear, spinnerEndYear;
    private Button btnAddExperience, btnSaveExperience, btnCancelExperience;

    private static final String PREFS_NAME = "CVBuilderPrefs";
    private static final String EXPERIENCE_KEY = "experience_details";
    private ArrayList<String> experienceList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_experience);

        // Initialize UI Elements
        etJobTitle = findViewById(R.id.etJobTitle);
        etCompanyName = findViewById(R.id.etCompanyName);
        radioGroupJobType = findViewById(R.id.radioGroupJobType);
        spinnerStartYear = findViewById(R.id.spinnerStartYear);
        spinnerEndYear = findViewById(R.id.spinnerEndYear);
        btnAddExperience = findViewById(R.id.btnAddExperience);
        btnSaveExperience = findViewById(R.id.btnSaveExperience);
        btnCancelExperience = findViewById(R.id.btnCancelExperience);

        // Populate Year Spinners
        ArrayList<String> years = new ArrayList<>();
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int year = 1980; year <= currentYear; year++) {
            years.add(String.valueOf(year));
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, years);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStartYear.setAdapter(adapter);
        spinnerEndYear.setAdapter(adapter);

        // Save Experience
        btnSaveExperience.setOnClickListener(v -> {
            addExperience();
            SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit();
            editor.putString(EXPERIENCE_KEY, experienceList.toString());
            editor.apply();
            Toast.makeText(ExperienceActivity.this, "Experience Saved!", Toast.LENGTH_SHORT).show();
            finish();
        });

        btnCancelExperience.setOnClickListener(v -> finish());
        btnAddExperience.setOnClickListener(v -> addExperience());
    }

    private void addExperience() {
        int selectedId = radioGroupJobType.getCheckedRadioButtonId();
        RadioButton selectedRadioButton = findViewById(selectedId);
        String jobType = selectedRadioButton != null ? selectedRadioButton.getText().toString() : "Not Selected";
        String jobTitle = etJobTitle.getText().toString();
        String companyName = etCompanyName.getText().toString();
        String startYear = spinnerStartYear.getSelectedItem().toString();
        String endYear = spinnerEndYear.getSelectedItem().toString();

        if (jobTitle.isEmpty() || companyName.isEmpty()) {
            Toast.makeText(this, "Enter all details!", Toast.LENGTH_SHORT).show();
            return;
        }

        String experienceEntry = jobTitle + " at " + companyName + " (" + startYear + " - " + endYear + ") [" + jobType + "]";
        experienceList.add(experienceEntry);
        Toast.makeText(this, "Experience Added!", Toast.LENGTH_SHORT).show();
        etJobTitle.setText("");
        etCompanyName.setText("");
    }
}
