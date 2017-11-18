package com.maciejwozny.budget;

import android.os.Bundle;
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

public class BudgetActivity extends AppCompatActivity {
    public final static Budget DEFAULT_BUDGET = new Budget("default budget", 10, 1500);

    private BudgetDatabase database = new BudgetDatabase(this);
    private DailyBudget dailyBudget = new DailyBudget(database, Calendar.getInstance());
    private MonthlyBudget monthlyBudget = new MonthlyBudget(database, Calendar.getInstance());
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
                database.onUpgrade(database.getWritableDatabase(), 0, 0);
                expenseAdditional.notifyObservers();
            }
        });

//*********************************************************************************************//
        //TODO move to another class or method

        TextView todaysBudget = (TextView) findViewById(R.id.todaysBudgetTextView);
        TextView todaysRemainingBudget = (TextView) findViewById(R.id.todaysRemainingBudgetTextView);
        dailyBudgetView = new DailyBudgetView(todaysBudget, todaysRemainingBudget, dailyBudget);

        TextView monthlyBudget = (TextView) findViewById(R.id.monthlyBudgetTextView);
        TextView monthlySpends = (TextView) findViewById(R.id.monthlySpendsTextView);
        TextView remainedMonthly = (TextView) findViewById(R.id.remainedMonthlyTextView);
        monthlyBudgetView = new MonthlyBudgetView(monthlyBudget, monthlySpends, remainedMonthly, this.monthlyBudget);

        EditText nameExpense = (EditText) findViewById(R.id.nameExpenseEditText);
        EditText amontExpense = (EditText) findViewById(R.id.amontExpenseEditText);
        EditText expenseDate = (EditText) findViewById(R.id.expenceDateEditText);
        Button addExpanse = (Button) findViewById(R.id.addExpenceButton);
        addExpenseView = new AddExpenseView(nameExpense, amontExpense, expenseDate, addExpanse, expenseAdditional);

        expenseAdditional.addObserver(dailyBudgetView);
        expenseAdditional.addObserver(monthlyBudgetView);

//*********************************************************************************************//
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_budget, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
