package com.maciejwozny.budget.view;

import android.util.Log;
import android.widget.TextView;

import com.maciejwozny.budget.BudgetActivity;
import com.maciejwozny.budget.model.DailyBudget;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by Maciej Wozny on 21.10.2017.
 * 2017 All rights reserved.
 */
public class DailyBudgetView implements Observer {
    private static final String TAG = DailyBudgetView.class.getSimpleName();
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
        Log.d(TAG, "refreshing");
        //TODO move to strings.xml !
        todayBudget.setText("Today's budget: "
                + dailyBudget.getDailyBudget());
        remainingDailyBudget.setText("Today's remaining budget: "
                + dailyBudget.getDailyRemainingBudget());
    }
}
