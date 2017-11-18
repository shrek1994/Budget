package com.maciejwozny.budget.view;

import android.widget.TextView;

import com.maciejwozny.budget.BudgetActivity;
import com.maciejwozny.budget.model.DailyBudget;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by maciej on 21.10.17.
 */

public class DailyBudgetView implements Observer {
    private TextView todayBudget;
    private TextView remainingDailyBudget;
    private DailyBudget dailyBudget;

    public DailyBudgetView(TextView todayBudget, TextView remainingDailyBudget, DailyBudget dailyBudget) {
        this.todayBudget = todayBudget;
        this.remainingDailyBudget = remainingDailyBudget;
        this.dailyBudget = dailyBudget;
        update(null, null);
    }

    @Override
    public void update(Observable o, Object obj) {
        todayBudget.setText("Today's budget: " + dailyBudget.getDailyBudget(BudgetActivity.BUDGET_NAME));
        remainingDailyBudget.setText("Today's remaining budget: " + dailyBudget.getDailyRemainingBudget(BudgetActivity.BUDGET_NAME));
    }
}
