package com.example.cv_builder;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class PersonalDetailsActivity extends AppCompatActivity {

    private EditText etFullName, etPhoneNumber, etEmail, etLinkedIn, etGitHub;
    private Button btnSave, btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_details);

        // Initialize input fields
        etFullName = findViewById(R.id.etFullName);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        etEmail = findViewById(R.id.etEmail);
        etLinkedIn = findViewById(R.id.etLinkedIn);
        etGitHub = findViewById(R.id.etGitHub);

        // Initialize buttons
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);

        // Save Button Click Event
        btnSave.setOnClickListener(v -> {
            String fullName = etFullName.getText().toString();
            String phoneNumber = etPhoneNumber.getText().toString();
            String email = etEmail.getText().toString();
            String linkedIn = etLinkedIn.getText().toString();
            String github = etGitHub.getText().toString();

            if (fullName.isEmpty() || phoneNumber.isEmpty() || email.isEmpty()) {
                Toast.makeText(PersonalDetailsActivity.this, "Please fill in all required fields!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(PersonalDetailsActivity.this, "Details Saved!", Toast.LENGTH_SHORT).show();
                finish(); // Close this activity and go back
            }
        });

        // Cancel Button Click Event
        btnCancel.setOnClickListener(v -> finish()); // Close Activity
    }
}
