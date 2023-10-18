package com.eterces.expiryalert;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Fragment_category extends Fragment {
    private AlertDialog alertDialog;
    private CategoryDatabaseHelper categoryDbHelper;
    private RecyclerView recyclerView;
    private CategoryAdapter categoryAdapter;
    private List<String> categoryList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_category, container, false);

        categoryDbHelper = new CategoryDatabaseHelper(getActivity());

        CardView categoryCardView = view.findViewById(R.id.categoryinput);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        categoryList = new ArrayList<>();
        categoryAdapter = new CategoryAdapter(categoryList);
        recyclerView.setAdapter(categoryAdapter);

        categoryCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View dialogView = getLayoutInflater().inflate(R.layout.popup_layout, null);

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setView(dialogView);

                EditText editText = dialogView.findViewById(R.id.editText);

                builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String categoryName = editText.getText().toString().trim();
                        if (!categoryName.isEmpty()) {
                            // Save the category to the database
                            saveCategoryToDatabase(categoryName);
                            // Refresh the RecyclerView with the updated data
                            refreshRecyclerView();
                        }
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                alertDialog = builder.create();
                alertDialog.show();
            }
        });

        // Initialize the RecyclerView with existing categories
        refreshRecyclerView();

        return view;
    }

    private void saveCategoryToDatabase(String categoryName) {
        // You can use the CategoryDatabaseHelper to save the category
        long result = categoryDbHelper.insertCategory(categoryName);
        if (result != -1) {
            showToast("Category added successfully");
        } else {
            showToast("Failed to add category");
        }
    }

    private void refreshRecyclerView() {
        // Load categories from the database and update the RecyclerView
        categoryList.clear();
        categoryList.addAll(categoryDbHelper.getAllCategories());
        categoryAdapter.notifyDataSetChanged();
    }

    private void showToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }
}