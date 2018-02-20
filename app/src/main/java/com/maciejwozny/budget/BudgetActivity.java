package com.maciejwozny.budget;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.maciejwozny.budget.model.DailyBudget;
import com.maciejwozny.budget.model.ExpenseAdditional;
import com.maciejwozny.budget.model.MonthlyBudget;
import com.maciejwozny.budget.sql.BudgetDatabase;
import com.maciejwozny.budget.sql.tables.Budget;
import com.maciejwozny.budget.view.AddExpenseView;
import com.maciejwozny.budget.view.DailyBudgetView;
import com.maciejwozny.budget.view.MonthlyBudgetView;

import java.util.Calendar;

/**
 * Created by Maciej Wozny on 21.10.2017.
 * 2017 All rights reserved.
 */
public class BudgetActivity extends AppCompatActivity {
    private static final String TAG = BudgetActivity.class.getSimpleName();
    public static Budget DEFAULT_BUDGET = new Budget("default budget", 10, 1500);

    private BudgetDatabase database = new BudgetDatabase(this);
    private DailyBudget dailyBudget = new DailyBudget(this, database, Calendar.getInstance());
    private MonthlyBudget monthlyBudget = new MonthlyBudget(this, database, Calendar.getInstance());
    private ExpenseAdditional expenseAdditional = new ExpenseAdditional(database);

    private DailyBudgetView dailyBudgetView;
    private MonthlyBudgetView monthlyBudgetView;
    private AddExpenseView addExpenseView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Your database was removed !", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
//                database.onUpgrade(database.getWritableDatabase(), 0, 0);
                finish();
                startActivity(getIntent());
                expenseAdditional.notifyObservers();
            }
        });

//*********************************************************************************************//
        //TODO move to another class or method

        TextView todayBudget = (TextView) findViewById(R.id.todaysBudgetTextView);
        TextView todayRemainingBudget = (TextView) findViewById(R.id.todaysRemainingBudgetTextView);
        dailyBudgetView = new DailyBudgetView(todayBudget, todayRemainingBudget, dailyBudget);

        TextView monthlyBudget = (TextView) findViewById(R.id.monthlyBudgetTextView);
        TextView monthlySpends = (TextView) findViewById(R.id.monthlySpendsTextView);
        TextView remainedMonthly = (TextView) findViewById(R.id.remainedMonthlyTextView);
        monthlyBudgetView = new MonthlyBudgetView(monthlyBudget, monthlySpends, remainedMonthly, this.monthlyBudget);

        EditText nameExpense = (EditText) findViewById(R.id.nameExpenseEditText);
        EditText amountExpense = (EditText) findViewById(R.id.amountExpenseEditText);
        EditText expenseDate = (EditText) findViewById(R.id.expenseDateEditText);
        Button addExpanse = (Button) findViewById(R.id.addExpenseButton);
        addExpenseView = new AddExpenseView(nameExpense, amountExpense, expenseDate, addExpanse, expenseAdditional);

        expenseAdditional.addObserver(dailyBudgetView);
        expenseAdditional.addObserver(monthlyBudgetView);

//*********************************************************************************************//
    }

    @Override
    public void onResume() {
        super.onResume();
        expenseAdditional.notifyObservers();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_budget, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_ocr:
                startActivity(new Intent(this, OcrActivity_TestingActivity.class));
                return true;
            case R.id.expense_list:
                Intent intent = new Intent(this, ExpenseListActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
