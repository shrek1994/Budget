package com.maciejwozny.budget.view;

import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Created by Maciej Wozny on 22.10.2017.
 * 2017 All rights reserved.
 */
public class ExpenditureWindowShower implements View.OnClickListener {
    PopupWindow mPopupWindow;

    @Override
    public void onClick(View v) {
        LayoutInflater inflater = (LayoutInflater) v.getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View customView = inflater.inflate(0, null);//TODO, null);
        mPopupWindow = new PopupWindow(
                customView,
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT
        );
        if(Build.VERSION.SDK_INT>=21){
            mPopupWindow.setElevation(5.0f);
        }

        new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPopupWindow.dismiss();
            }
        };
        mPopupWindow.showAtLocation(v.getRootView(), Gravity.CENTER, 0, 0);
    }
}
