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

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import java.util.Arrays;
import java.util.List;

public class Fragment_category extends Fragment {
    private AlertDialog alertDialog; // Declare alertDialog at a higher scope
    private ImageView iconView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_category, container, false);

        // Find the CardView in the layout
        CardView categoryCardView = view.findViewById(R.id.categoryinput);

        // Set OnClickListener for the CardView
        categoryCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Inflate the dialog layout
                View dialogView = getLayoutInflater().inflate(R.layout.popup_layout, null);

                // Create an AlertDialog
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setView(dialogView);

                // Find views inside the dialog layout
                EditText editText = dialogView.findViewById(R.id.editText);
                ImageButton closeButton = dialogView.findViewById(R.id.closeButton);
                TextView selectIconTextView = dialogView.findViewById(R.id.selecticon);

                // Set click listener for the select icon TextView
                selectIconTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Show a list of icons when the TextView is clicked
                        showIconListDialog();
                    }
                });

                // Set click listeners for other views inside the dialog
                closeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Close the dialog when the close button is clicked
                        alertDialog.dismiss();
                    }
                });

                // Create and show the AlertDialog
                alertDialog = builder.create();
                alertDialog.show();
            }
        });

        return view;
    }


    private void showIconListDialog() {

        Integer[] iconIds = {R.drawable.ic1, R.drawable.ic1, R.drawable.ic1};

        List<Integer> iconList = Arrays.asList(iconIds);

        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(
                getActivity(),
                R.layout.custom_icon_list_item,
                R.id.text_view,
                iconList) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textView = view.findViewById(R.id.text_view);
                ImageView imageView = view.findViewById(R.id.icon);
                textView.setText("Icon " + (position + 1));
                // Set the icon
                imageView.setImageResource(iconList.get(position));
                return view;
            }
        };

        // Create a dialog with the list of icons
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Select an Icon")
                .setAdapter(adapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        int selectedIconId = iconList.get(which);

                    }
                });

        // Show the dialog
        builder.create().show();
    }
}