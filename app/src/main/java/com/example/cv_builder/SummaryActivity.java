package com.example.cv_builder;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SummaryActivity extends AppCompatActivity {

    private EditText etSummary;
    private Button btnSaveSummary, btnCancelSummary;
    private static final String PREFS_NAME = "CVBuilderPrefs";
    private static final String SUMMARY_KEY = "summary_text";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        // Initialize UI elements
        etSummary = findViewById(R.id.etSummary);
        btnSaveSummary = findViewById(R.id.btnSaveSummary);
        btnCancelSummary = findViewById(R.id.btnCancelSummary);

        // Load saved summary if available
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String savedSummary = sharedPreferences.getString(SUMMARY_KEY, "");
        etSummary.setText(savedSummary);

        // Save Button Click Event
        btnSaveSummary.setOnClickListener(v -> {
            String summaryText = etSummary.getText().toString();
            if (summaryText.isEmpty()) {
                Toast.makeText(SummaryActivity.this, "Summary cannot be empty!", Toast.LENGTH_SHORT).show();
            } else {
                // Save summary to SharedPreferences
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(SUMMARY_KEY, summaryText);
                editor.apply();
                Toast.makeText(SummaryActivity.this, "Summary Saved!", Toast.LENGTH_SHORT).show();
                finish(); // Close activity and go back
            }
        });

        // Cancel Button Click Event
        btnCancelSummary.setOnClickListener(v -> finish()); // Close activity
    }
}
