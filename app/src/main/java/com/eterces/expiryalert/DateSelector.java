package com.eterces.expiryalert;
import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class DateSelector {

    public static void setupDateSelector(Context context, Button dateButton) {
        final Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        final DatePickerDialog datePickerDialog = new DatePickerDialog(
                context,
                (view, selectedYear, selectedMonth, selectedDayOfMonth) -> {
                    // Set the selected date to the button
                    String selectedDate = selectedYear + "-" + (selectedMonth + 1) + "-" + selectedDayOfMonth;
                    dateButton.setText(selectedDate);
                },
                year, month, dayOfMonth
        );

        // Set the minimum date to the current date
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

        // Set a click listener on the button to show the date picker dialog
        dateButton.setOnClickListener(v -> datePickerDialog.show());
    }
}