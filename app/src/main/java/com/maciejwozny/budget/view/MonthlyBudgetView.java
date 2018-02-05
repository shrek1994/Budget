package com.maciejwozny.budget.view;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import com.maciejwozny.budget.BudgetActivity;
import com.maciejwozny.budget.R;
import com.maciejwozny.budget.model.MonthlyBudget;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by Maciej Wozny on 18.11.2017.
 * 2017 All rights reserved.
 */
public class MonthlyBudgetView implements Observer {
    private static final String TAG = MonthlyBudgetView.class.getSimpleName();
    private TextView monthlyBudgetText;
    private TextView monthlySpends;
    private TextView remainingMonthlyBudget;
    private MonthlyBudget monthlyBudget;
    private Context context;

    public MonthlyBudgetView(TextView monthlyBudgetText, TextView monthlySpends, TextView remainingMonthlyBudget, MonthlyBudget monthlyBudget) {
        this.monthlyBudgetText = monthlyBudgetText;
        this.monthlySpends = monthlySpends;
        this.remainingMonthlyBudget = remainingMonthlyBudget;
        this.monthlyBudget = monthlyBudget;
        this.context = monthlyBudgetText.getContext();
        update(null, null);
    }

    @Override
    public void update(Observable o, Object arg) {
        Log.d(TAG, "refreshing");
        monthlyBudgetText.setText(context.getString(R.string.monthly_budget)
                + monthlyBudget.getMonthlyBudget(BudgetActivity.DEFAULT_BUDGET.getName()));
        monthlySpends.setText("Monthly spends: "
                + monthlyBudget.getMonthlySpends(BudgetActivity.DEFAULT_BUDGET.getName()));
        remainingMonthlyBudget.setText("Remained: "
                + monthlyBudget.getMonthlyRemaining(BudgetActivity.DEFAULT_BUDGET.getName()));
    }
}
