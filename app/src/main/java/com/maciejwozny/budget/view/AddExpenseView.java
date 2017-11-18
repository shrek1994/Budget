package com.maciejwozny.budget.view;

import android.app.DatePickerDialog;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.maciejwozny.budget.model.ExpenseAdditional;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import static java.lang.String.valueOf;

/**
 * Created by maciek on 18.11.17.
 */

public class AddExpenseView implements View.OnClickListener {
    Calendar myCalendar = Calendar.getInstance();
    private EditText expenseName;
    private EditText expenseAmount;
    private EditText expenseDate;
    private Button addExpense;
    private ExpenseAdditional expenseAdditional;

    public AddExpenseView(final EditText expenseName, EditText expenseAmount, final EditText expenseDate, Button addExpense, ExpenseAdditional expenseAdditional) {
        this.expenseName = expenseName;
        this.expenseAmount = expenseAmount;
        this.expenseDate = expenseDate;
        this.addExpense = addExpense;
        this.expenseAdditional = expenseAdditional;

        this.addExpense.setOnClickListener(this);

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        this.expenseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(expenseDate.getContext(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        expenseAdditional.addExpense(expenseName.getText().toString(),
                Integer.parseInt(expenseAmount.getText().toString()),
                valueOf(expenseDate.getText().toString()));
    }


    private void updateLabel() {
        String myFormat = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        expenseDate.setText(sdf.format(myCalendar.getTime()));
    }
}
