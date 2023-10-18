package com.eterces.expiryalert;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.eterces.expiryalert.databinding.ActivityMainBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 3;

    private @NonNull
    ActivityMainBinding binding;

    private ImageView imageViewSelectedImage;
    private Button dateButton;
    private Uri selectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        replaceFragment(new fragment_home());
        binding.bottomNavigationView.setBackground(null);
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.home) {
                replaceFragment(new fragment_home());
            } else if (item.getItemId() == R.id.category) {
                replaceFragment(new Fragment_category());
            } else if (item.getItemId() == R.id.notify) {
                replaceFragment(new Fragment_Notification());
            } else if (item.getItemId() == R.id.settings) {
                replaceFragment(new Fragment_Settings());
            }
            return true;
        });

        FloatingActionButton floatingActionButton = findViewById(R.id.floating);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInputDialog();
            }
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    private void showInputDialog() {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_input, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();

        dateButton = dialogView.findViewById(R.id.buttonPickDate);
        Button buttonPickImage = dialogView.findViewById(R.id.buttonPickImage);
        EditText editTextInput = dialogView.findViewById(R.id.editTextInput);
        imageViewSelectedImage = dialogView.findViewById(R.id.imageViewSelectedImage);
        Button buttonSubmit = dialogView.findViewById(R.id.buttonSubmit);

        // Initialize the Spinner
        Spinner spinnerCategory = dialogView.findViewById(R.id.spinnerCategory);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.categories_array, // Replace with your array resource
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);
        spinnerCategory.setSelection(0, false);

        DateSelector.setupDateSelector(this, dateButton);

        buttonPickImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagePicker();
            }
        });

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get other input values
                String name = editTextInput.getText().toString().trim();
                String category = spinnerCategory.getSelectedItem().toString();

                // Check if the required fields are not empty
                if (!name.isEmpty() && selectedImageUri != null) {
                    // Save the data to the database or perform other actions
                    saveDataToDatabase(name, dateButton.getText().toString(), selectedImageUri.toString(), category);

                    // Dismiss the dialog
                    dialog.dismiss();
                } else {
                    Toast.makeText(MainActivity.this, "Please fill in all the required fields", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialog.show();
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            // Handle the selected image
            Uri selectedImageUri = data.getData();
            if (selectedImageUri != null && imageViewSelectedImage != null) {
                // Display the selected image in the ImageView using Glide
                imageViewSelectedImage.setVisibility(View.VISIBLE);
                Glide.with(this).load(selectedImageUri).into(imageViewSelectedImage);

                // Assign the selectedImageUri to the global variable
                this.selectedImageUri = selectedImageUri;
            }
        }
    }

    private void saveDataToDatabase(String name, String expirationDate, String imageUri, String category) {
        // Assuming you have an instance of DatabaseHelper
        DatabaseHelper dbHelper = new DatabaseHelper(this);

        try {
            // Insert the product into the database
            long newRowId = dbHelper.insertProduct(name, expirationDate, imageUri, category);

            // Check if the insertion was successful
            if (newRowId != -1) {
                Toast.makeText(this, "Product saved successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Failed to save product", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}