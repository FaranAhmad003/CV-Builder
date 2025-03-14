package com.example.cv_builder;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class ReferencesActivity extends AppCompatActivity {

    private EditText etReferenceLink;
    private Button btnSubmitLink, btnUploadDocument;
    private ListView listViewLinks, listViewDocs;

    private static final String PREFS_NAME = "CVBuilderPrefs";
    private static final String REFERENCE_LINKS_KEY = "reference_links";
    private static final String REFERENCE_DOCS_KEY = "reference_documents";

    private ArrayList<String> referenceLinks = new ArrayList<>();
    private ArrayList<String> referenceDocs = new ArrayList<>();

    private ArrayAdapter<String> linksAdapter;
    private ArrayAdapter<String> docsAdapter;

    private ActivityResultLauncher<Intent> filePickerLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_references);

        // Initialize UI Elements
        etReferenceLink = findViewById(R.id.etReferenceLink);
        btnSubmitLink = findViewById(R.id.btnSubmitLink);
        btnUploadDocument = findViewById(R.id.btnUploadDocument);
        listViewLinks = findViewById(R.id.listViewLinks);
        listViewDocs = findViewById(R.id.listViewDocs);

        // Load saved data
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        referenceLinks.addAll(sharedPreferences.getStringSet(REFERENCE_LINKS_KEY, new HashSet<>()));
        referenceDocs.addAll(sharedPreferences.getStringSet(REFERENCE_DOCS_KEY, new HashSet<>()));

        // Setup ListView Adapters
        linksAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, referenceLinks);
        docsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, referenceDocs);
        listViewLinks.setAdapter(linksAdapter);
        listViewDocs.setAdapter(docsAdapter);

        // Handle link submission
        btnSubmitLink.setOnClickListener(v -> addReferenceLink());

        // Handle file upload
        btnUploadDocument.setOnClickListener(v -> openFilePicker());

        // Register File Picker Activity
        filePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Uri selectedFileUri = result.getData().getData();
                        if (selectedFileUri != null) {
                            addReferenceDocument(selectedFileUri.toString());
                        }
                    }
                }
        );

        // Request Storage Permission if not granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
        }

        // Remove link on long click
        listViewLinks.setOnItemLongClickListener((parent, view, position, id) -> {
            referenceLinks.remove(position);
            saveReferenceLinks();
            linksAdapter.notifyDataSetChanged();
            Toast.makeText(this, "Reference Link Removed!", Toast.LENGTH_SHORT).show();
            return true;
        });

        // Remove document on long click
        listViewDocs.setOnItemLongClickListener((parent, view, position, id) -> {
            referenceDocs.remove(position);
            saveReferenceDocuments();
            docsAdapter.notifyDataSetChanged();
            Toast.makeText(this, "Document Removed!", Toast.LENGTH_SHORT).show();
            return true;
        });
    }

    private void addReferenceLink() {
        String referenceLink = etReferenceLink.getText().toString();
        if (referenceLink.isEmpty()) {
            Toast.makeText(this, "Please enter a reference link!", Toast.LENGTH_SHORT).show();
        } else {
            referenceLinks.add(referenceLink);
            saveReferenceLinks();
            linksAdapter.notifyDataSetChanged();
            etReferenceLink.setText("");
            Toast.makeText(this, "Reference Link Added!", Toast.LENGTH_SHORT).show();
        }
    }

    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        filePickerLauncher.launch(intent);
    }

    private void addReferenceDocument(String fileUri) {
        referenceDocs.add(fileUri);
        saveReferenceDocuments();
        docsAdapter.notifyDataSetChanged();
        Toast.makeText(this, "Document Added!", Toast.LENGTH_SHORT).show();
    }

    private void saveReferenceLinks() {
        SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit();
        editor.putStringSet(REFERENCE_LINKS_KEY, new HashSet<>(referenceLinks));
        editor.apply();
    }

    private void saveReferenceDocuments() {
        SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit();
        editor.putStringSet(REFERENCE_DOCS_KEY, new HashSet<>(referenceDocs));
        editor.apply();
    }
}
