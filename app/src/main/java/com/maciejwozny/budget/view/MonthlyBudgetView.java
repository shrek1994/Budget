package com.maciejwozny.budget.view;

import android.util.Log;
import android.widget.TextView;

import com.maciejwozny.budget.BudgetActivity;
import com.maciejwozny.budget.model.MonthlyBudget;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by maciek on 18.11.17.
 */

public class MonthlyBudgetView implements Observer {
    private static final String TAG = MonthlyBudgetView.class.getSimpleName();
    private TextView monthlyBudgetText;
    private TextView monthlySpends;
    private TextView remainingMonthlyBudget;
    private MonthlyBudget monthlyBudget;

    public MonthlyBudgetView(TextView monthlyBudgetText, TextView monthlySpends, TextView remainingMonthlyBudget, MonthlyBudget monthlyBudget) {
        this.monthlyBudgetText = monthlyBudgetText;
        this.monthlySpends = monthlySpends;
        this.remainingMonthlyBudget = remainingMonthlyBudget;
        this.monthlyBudget = monthlyBudget;
        update(null, null);
    }

    @Override
    public void update(Observable o, Object arg) {
        Log.d(TAG, "refreshing");
        monthlyBudgetText.setText("Monthly budget: "
                + monthlyBudget.getMonthlyBudget(BudgetActivity.DEFAULT_BUDGET.getName()));
        monthlySpends.setText("Monthly spends: "
                + monthlyBudget.getMonthlySpends(BudgetActivity.DEFAULT_BUDGET.getName()));
        remainingMonthlyBudget.setText("Remained: "
                + monthlyBudget.getMonthlyRemaining(BudgetActivity.DEFAULT_BUDGET.getName()));
    }
}
