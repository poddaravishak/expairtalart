package com.eterces.expiryalert;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class fragment_home extends Fragment {

    private ProductAdapter productAdapter;
    private ArrayList<Product> productList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize RecyclerView and ProductAdapter
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        productList = new ArrayList<>();
        productAdapter = new ProductAdapter(requireContext(), productList);

        // Set layout manager and adapter to RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(productAdapter);

        // Load data from the database
        loadDataFromDatabase();

        return view;
    }

    private void loadDataFromDatabase() {
        // Initialize your DatabaseHelper
        DatabaseHelper dbHelper = new DatabaseHelper(requireContext());

        // Perform database query to fetch the list of products
        Cursor cursor = dbHelper.getData();

        // Iterate through the cursor and populate the productList
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME));
            String date = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_DATE));
            String imageUri = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_IMAGE_URI));
            String category = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_CATEGORY));

            // Create a Product object and add it to the productList
            Product product = new Product(name, date, calculateDaysRemaining(date), category, imageUri);
            productList.add(product);
        }

        // Close the cursor and database
        cursor.close();
        dbHelper.close();

        // Notify the adapter that the data has changed
        productAdapter.notifyDataSetChanged();
    }

    // You need to implement your logic to calculate days remaining
    private String calculateDaysRemaining(String expirationDate) {
        // Date format to parse the expiration date
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        try {
            // Parse the expiration date string to Date object
            Date expiration = dateFormat.parse(expirationDate);

            // Get the current date
            Date currentDate = new Date();

            // Calculate the difference in milliseconds
            long differenceInMillis = expiration.getTime() - currentDate.getTime();

            // Calculate the difference in days
            long daysDifference = TimeUnit.MILLISECONDS.toDays(differenceInMillis);

            // Return the days remaining as a string
            return String.valueOf(daysDifference);
        } catch (ParseException e) {
            e.printStackTrace();
            return " ";
        }
    }

}