package com.skydt.weightcontrol.services;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.skydt.weightcontrol.R;

public class ImagePopupService
{
    private View view;
    private Context context;
    private int pageNumber;

    public ImagePopupService(View view, Context context, int pageNumber)
    {
        this.view = view;
        this.context = context;
        this.pageNumber = pageNumber;
    }

    public void showPopupWindow(double[] sizes)
    {
        // inflate the layout of the popup window
        LayoutInflater inflater = LayoutInflater.from(context);
        View popupView = inflater.inflate(R.layout.image_popup_window, null);

        ImageView imgView = popupView.findViewById(R.id.iwScreenshot);

        switch (pageNumber)
        {
            case 2:
               imgView.setBackgroundResource(R.drawable.create_diet);
               break;
            case 3:
                imgView.setBackgroundResource(R.drawable.main_menu);
                break;
            case 4:
                imgView.setBackgroundResource(R.drawable.diet_activity);
                break;
            case 5:
                imgView.setBackgroundResource(R.drawable.day_activity);
                break;
            case 6:
                imgView.setBackgroundResource(R.drawable.weigh_body);
                break;
            case 7:
                imgView.setBackgroundResource(R.drawable.weigh_food);
                break;
            case 8:
                imgView.setBackgroundResource(R.drawable.diet_selection);
                break;
            default:
                break;
        }

        // set the size of the popup and disables outside clicks (focusable)
        android.widget.PopupWindow popupWindow = new android.widget.PopupWindow(popupView, (int)sizes[0], (int)sizes[1]);
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
}
