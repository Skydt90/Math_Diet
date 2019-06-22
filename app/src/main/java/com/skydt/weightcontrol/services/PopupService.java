package com.skydt.weightcontrol.services;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.text.method.LinkMovementMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.skydt.weightcontrol.R;
import com.skydt.weightcontrol.activities.InformationActivity;

public class PopupService implements View.OnClickListener
{
    private android.widget.PopupWindow popupWindow;
    private View view;
    private Context context;
    private SharedPreferences sharedPreferences;

    public PopupService(View view, Context context, SharedPreferences sharedPreferences)
    {
        this.view = view;
        this.context = context;
        this.sharedPreferences = sharedPreferences;
    }

    public void showPopupWindow(double[] sizes)
    {
        // inflate the layout of the popup window
        LayoutInflater inflater = LayoutInflater.from(context);
        View popupView = inflater.inflate(R.layout.popup_window, null);

        // enables clickable links in text view
        ((TextView)popupView.findViewById(R.id.tvWelcome)).setMovementMethod(LinkMovementMethod.getInstance());

        Button btn1 = popupView.findViewById(R.id.button1);
        Button btn2 = popupView.findViewById(R.id.button2);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);

        // set the size of the popup and enables outside clicks (focusable)
        popupWindow = new android.widget.PopupWindow(popupView, (int)sizes[0], (int)sizes[1], true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));

        // check SDK version, since only available in 21 and above
        if (Build.VERSION.SDK_INT >= 21)
        {
            popupWindow.setElevation(20);
        }
        // show popup at screen center
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        // dim AFTER popup is shown to avoid null point
        dimBehind(popupWindow, context);
    }

    private void dimBehind(android.widget.PopupWindow popupWindow, Context context)
    {
        // get the containing view, which the popup is on top of
        View container = popupWindow.getContentView().getRootView();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams params = (WindowManager.LayoutParams) container.getLayoutParams();

        // dims the containing layout
        params.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        params.dimAmount = 0.7f;
        windowManager.updateViewLayout(container, params);
    }

    private void saveBoolInSharedPreferences()
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("welcome", false);
        editor.apply();
    }

    @Override
    public void onClick(View v)
    {
        Button btn = (Button) v;
        int id = btn.getId();

        switch(id)
        {
            case R.id.button1:
                popupWindow.dismiss();
                saveBoolInSharedPreferences();
                break;
            case R.id.button2:
                Intent intent = new Intent(context, InformationActivity.class);
                popupWindow.dismiss();
                saveBoolInSharedPreferences();
                context.startActivity(intent);
                break;
            default:
                break;
        }
    }
}
