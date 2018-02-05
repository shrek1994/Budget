package com.maciejwozny.budget;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.maciejwozny.budget.model.ExpenseList;
import com.maciejwozny.budget.sql.BudgetDatabase;
import com.maciejwozny.budget.sql.IBudgetDatabase;
import com.maciejwozny.budget.sql.tables.Expenditure;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Maciej Wozny on 21.10.2017.
 * 2017 All rights reserved.
 */
public class ExpenseListActivity extends AppCompatActivity {
    ExpenseList expenseList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_list);
        expenseList = new ExpenseList(new BudgetDatabase(this));

        final ListView listview = (ListView) findViewById(R.id.expenseListView);
        final ExpenseArrayAdapter adapter = new ExpenseArrayAdapter(this,
                R.layout.expense_row,
                expenseList.getExpenses(BudgetActivity.DEFAULT_BUDGET.getName()));
        listview.setAdapter(adapter);
    }


    private class ExpenseArrayAdapter extends ArrayAdapter<Expenditure> {
        public ExpenseArrayAdapter(Context context, int resourceId,
                                   List<Expenditure> objects) {
            super(context, resourceId, objects);
        }


        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) ExpenseListActivity.this
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.expense_row, parent, false);
            TextView expanseName = (TextView) rowView.findViewById(R.id.expenseNameTextView);
            TextView amountAndDateExpense = (TextView) rowView.findViewById(R.id.amountAndDateExpenseTextView);
            expanseName.setText(getItem(position).getName());
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

            amountAndDateExpense.setText(getItem(position).getAmount() + " PLN ("
                    + format.format(getItem(position).getDate()) + ")");

            Button remove = (Button) rowView.findViewById(R.id.removeExpenseButton);
            remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Expenditure expenditure = ExpenseArrayAdapter.this.getItem(position);
                    expenseList.removeExpense(expenditure);
                    ExpenseArrayAdapter.this.remove(expenditure);
                }
            });

            return rowView;
        }
    }
}
