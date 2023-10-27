package com.eterces.expiryalert;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.MediaStore;
import android.util.Log;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 3;

    private @NonNull ActivityMainBinding binding;

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

        // Your existing code...

        Intent serviceIntent = new Intent(this, NotificationService.class);
        startService(serviceIntent);
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

        // Get the category names from the database
        CategoryDatabaseHelper dbHelper = new CategoryDatabaseHelper(this);
        List<String> categoryNamesFromDatabase = dbHelper.getAllCategories();

        // Get the default category names from the resource XML
        String[] defaultCategoriesArray = getResources().getStringArray(R.array.categories_array);
        List<String> defaultCategories = Arrays.asList(defaultCategoriesArray);

        // Create a list that combines the default categories and the categories from the database
        List<String> combinedCategories = new ArrayList<>(defaultCategories);
        combinedCategories.addAll(categoryNamesFromDatabase);

        // Add a hint as the first item
        combinedCategories.add(0, "Select Category");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, combinedCategories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);

        // Set the hint as unselectable
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
                String expirationDate = dateButton.getText().toString();

                // Check if the required fields are not empty
                if (!name.isEmpty() && selectedImageUri != null && !expirationDate.isEmpty()) {
                    // Save the data to the database
                    saveDataToDatabase(name, expirationDate, selectedImageUri.toString(), category);

                    // Add the event to the Google Calendar
                    addToGoogleCalendar(name, expirationDate);

                    // Refresh the fragment
                    refreshFragment();

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

    private void addToGoogleCalendar(String eventName, String eventDate) {
        try {
            // Create an Intent to open the calendar app
            Intent intent = new Intent(Intent.ACTION_INSERT);
            intent.setData(CalendarContract.Events.CONTENT_URI);
            intent.putExtra(CalendarContract.Events.TITLE, eventName);
            intent.putExtra(CalendarContract.Events.EVENT_LOCATION, "Location (if needed)");
            intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, getMillisecondsFromDateString(eventDate));
            intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, getMillisecondsFromDateString(eventDate));

            // Start the calendar app
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to add event to Google Calendar", Toast.LENGTH_SHORT).show();
        }
    }

    private long getMillisecondsFromDateString(String dateStr) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        try {
            Date date = dateFormat.parse(dateStr);
            if (date != null) {
                return date.getTime();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private void refreshFragment() {
        // Replace the current fragment with the same fragment to refresh it
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.frame_layout);
        if (currentFragment != null) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.detach(currentFragment);
            fragmentTransaction.attach(currentFragment);
            fragmentTransaction.commit();
        }
    }
}