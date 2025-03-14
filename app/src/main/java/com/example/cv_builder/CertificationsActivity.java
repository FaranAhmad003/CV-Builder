package com.example.cv_builder;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Calendar;

public class CertificationsActivity extends AppCompatActivity {

    private EditText etCertificationTitle;
    private Spinner spinnerCertYear;
    private Button btnCaptureImage, btnAddCertification, btnSaveCertifications, btnCancelCertifications;
    private ImageView imgCertificate;
    private Bitmap capturedImage;

    private static final String PREFS_NAME = "CVBuilderPrefs";
    private static final String CERTIFICATION_KEY = "certification_details";
    private ArrayList<String> certificationsList = new ArrayList<>();

    private ActivityResultLauncher<Intent> cameraLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_certifications);

        // Request Camera Permission at Runtime (Android 6+)
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, 100);
        }

        // Initialize UI Elements
        etCertificationTitle = findViewById(R.id.etCertificationTitle);
        spinnerCertYear = findViewById(R.id.spinnerCertYear);
        btnCaptureImage = findViewById(R.id.btnCaptureImage);
        imgCertificate = findViewById(R.id.imgCertificate);
        btnAddCertification = findViewById(R.id.btnAddCertification);
        btnSaveCertifications = findViewById(R.id.btnSaveCertifications);
        btnCancelCertifications = findViewById(R.id.btnCancelCertifications);

        // Initialize ActivityResultLauncher for Camera
        cameraLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Bundle extras = result.getData().getExtras();
                        capturedImage = (Bitmap) extras.get("data");
                        imgCertificate.setImageBitmap(capturedImage);
                        imgCertificate.setVisibility(ImageView.VISIBLE);
                    }
                });

        // Populate Year Spinner
        ArrayList<String> years = new ArrayList<>();
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int year = 1980; year <= currentYear; year++) {
            years.add(String.valueOf(year));
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, years);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCertYear.setAdapter(adapter);

        // Capture Image
        btnCaptureImage.setOnClickListener(v -> openCamera());

        // Save Certification
        btnSaveCertifications.setOnClickListener(v -> {
            addCertification();
            SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit();
            editor.putString(CERTIFICATION_KEY, certificationsList.toString());
            editor.apply();
            Toast.makeText(CertificationsActivity.this, "Certifications Saved!", Toast.LENGTH_SHORT).show();
            finish();
        });

        btnCancelCertifications.setOnClickListener(v -> finish());
        btnAddCertification.setOnClickListener(v -> addCertification());
    }

    // Open Camera for Capturing Certificate Image
    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Ensure a Camera App Exists
        if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
            cameraLauncher.launch(intent);
        } else {
            Toast.makeText(this, "No camera available on this device!", Toast.LENGTH_SHORT).show();
        }
    }


    // Handle Camera Permission Response
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Camera Permission Granted!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Camera Permission Denied! Cannot capture images.", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void addCertification() {
        String title = etCertificationTitle.getText().toString();
        String year = spinnerCertYear.getSelectedItem().toString();

        if (title.isEmpty()) {
            Toast.makeText(this, "Enter certification title!", Toast.LENGTH_SHORT).show();
            return;
        }

        certificationsList.add(title + " - " + year);
        Toast.makeText(this, "Certification Added!", Toast.LENGTH_SHORT).show();
        etCertificationTitle.setText("");
    }
}
