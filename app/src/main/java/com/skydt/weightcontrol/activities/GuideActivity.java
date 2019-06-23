package com.skydt.weightcontrol.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.skydt.weightcontrol.R;
import com.skydt.weightcontrol.services.ImagePopupService;

public class GuideActivity extends AppCompatActivity implements View.OnClickListener
{
    private TextView tvHeadline;
    private TextView tvInformation;
    private TextView tvBarProgressNum;
    private ProgressBar progressBar;
    private Button btnPrevious;
    private Button btnNext;
    private ImageButton btnImage;

    private int pageNumber;
    private String pageProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guide_activity);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        pageNumber = 1;
        pageProgress = "Side: %d / %d";
        loadInterface();
        populateInterface();
    }

    private void loadInterface()
    {
        tvHeadline = findViewById(R.id.tvHeadline);
        tvInformation = findViewById(R.id.tvInformation);
        tvInformation.setMovementMethod(new ScrollingMovementMethod());
        tvBarProgressNum = findViewById(R.id.tvBarProgressNum);
        progressBar = findViewById(R.id.progressBar);
        //progressBar.getProgressDrawable().setColorFilter(Color.parseColor("#53C557"), android.graphics.PorterDuff.Mode.SRC_IN);
        btnImage = findViewById(R.id.btnImage);
        btnPrevious = findViewById(R.id.btnPrevious);
        btnNext = findViewById(R.id.btnNext);
        btnPrevious.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        btnImage.setOnClickListener(this);
    }

    private void populateInterface()
    {
        switch (pageNumber)
        {
            case 1:
                tvHeadline.setText(this.getResources().getText(R.string.guide_intro_head));
                tvInformation.setText(this.getResources().getText(R.string.guide_intro_text));
                tvInformation.scrollTo(0, 0);
                tvBarProgressNum.setText(String.format(pageProgress, pageNumber, progressBar.getMax()));
                progressBar.setProgress(pageNumber);
                btnImage.setClickable(false);
                btnImage.setAlpha(0.2f);
                btnPrevious.setClickable(false);
                btnPrevious.setAlpha(0.2f);
                break;
            case 2:
                tvHeadline.setText(this.getResources().getText(R.string.guide_diet_create_head));
                tvInformation.setText(this.getResources().getText(R.string.guide_diet_create_text));
                tvInformation.scrollTo(0, 0);
                tvBarProgressNum.setText(String.format(pageProgress, pageNumber, progressBar.getMax()));
                progressBar.setProgress(pageNumber);
                btnPrevious.setClickable(true);
                btnPrevious.setAlpha(1);
                btnImage.setClickable(true);
                btnImage.setAlpha(1f);
                break;
            case 3:
                tvHeadline.setText(this.getResources().getText(R.string.guide_main_menu_head));
                tvInformation.setText(this.getResources().getText(R.string.guide_main_menu_text));
                tvInformation.scrollTo(0, 0);
                tvBarProgressNum.setText(String.format(pageProgress, pageNumber, progressBar.getMax()));
                progressBar.setProgress(pageNumber);
                break;
            case 4:
                tvHeadline.setText(this.getResources().getText(R.string.guide_diet_activity_head));
                tvInformation.setText(this.getResources().getText(R.string.guide_diet_activity_text));
                tvInformation.scrollTo(0, 0);
                tvBarProgressNum.setText(String.format(pageProgress, pageNumber, progressBar.getMax()));
                progressBar.setProgress(pageNumber);
                break;
            case 5:
                tvHeadline.setText(this.getResources().getText(R.string.guide_day_activity_head));
                tvInformation.setText(this.getResources().getText(R.string.guide_day_activity_text));
                tvInformation.scrollTo(0, 0);
                tvBarProgressNum.setText(String.format(pageProgress, pageNumber, progressBar.getMax()));
                progressBar.setProgress(pageNumber);
                break;
            case 6:
                tvHeadline.setText(this.getResources().getText(R.string.guide_weigh_body_head));
                tvInformation.setText(this.getResources().getText(R.string.guide_weigh_body_text));
                tvInformation.scrollTo(0, 0);
                tvBarProgressNum.setText(String.format(pageProgress, pageNumber, progressBar.getMax()));
                progressBar.setProgress(pageNumber);
                break;
            case 7:
                tvHeadline.setText(this.getResources().getText(R.string.guide_weigh_food_head));
                tvInformation.setText(this.getResources().getText(R.string.guide_weigh_food_text));
                tvInformation.scrollTo(0, 0);
                tvBarProgressNum.setText(String.format(pageProgress, pageNumber, progressBar.getMax()));
                progressBar.setProgress(pageNumber);
                btnNext.setClickable(true);
                btnNext.setAlpha(1);
                break;
            case 8:
                tvHeadline.setText(this.getResources().getText(R.string.guide_diet_select_head));
                tvInformation.setText(this.getResources().getText(R.string.guide_diet_select_text));
                tvInformation.scrollTo(0, 0);
                tvBarProgressNum.setText(String.format(pageProgress, pageNumber, progressBar.getMax()));
                progressBar.setProgress(pageNumber);
                btnNext.setClickable(false);
                btnNext.setAlpha(0.2f);
                break;
            default:
                break;
        }
    }

    private void showImage()
    {
        ImagePopupService popupService = new ImagePopupService(findViewById(android.R.id.content), this, pageNumber);
        DisplayMetrics displayMetrics = new DisplayMetrics();

        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        double[] sizes = {displayMetrics.widthPixels * 0.85, displayMetrics.heightPixels * 0.85};
        popupService.showPopupWindow(sizes);
    }

    @Override
    public void onClick(View v)
    {
        if (v instanceof ImageButton)
        {
            showImage();
        }
        else
        {
            Button btn = (Button) v;
            int btnID = btn.getId();

            switch (btnID)
            {
                case R.id.btnNext:
                    pageNumber += 1;
                    populateInterface();
                    break;
                case R.id.btnPrevious:
                    pageNumber -= 1;
                    populateInterface();
                    break;
                default:
                    break;
            }
        }
    }
}
