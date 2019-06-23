package com.skydt.weightcontrol.activities;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import com.skydt.weightcontrol.R;

public class InformationActivity extends AppCompatActivity
{
    private String versionName;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.information_activity);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        loadAndPopulateInterface();
    }

    private void loadAndPopulateInterface()
    {
        TextView tvDevelopers = findViewById(R.id.tvDevelopers);
        tvDevelopers.setText(this.getResources().getText(R.string.developers));

        TextView tvMethodBy = findViewById(R.id.tvMethodBy);
        tvMethodBy.setMovementMethod(LinkMovementMethod.getInstance());
        tvMethodBy.setText(this.getResources().getText(R.string.method_by));

        TextView tvLicense = findViewById(R.id.tvLicense);
        tvLicense.setText(this.getResources().getText(R.string.license));
        tvLicense.setMovementMethod(LinkMovementMethod.getInstance());

        getVersionInfo();
        TextView tvBuildInfo = findViewById(R.id.tvBuildInfo);
        tvBuildInfo.append(versionName);
    }

    private void getVersionInfo()
    {
        try
        {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            versionName = packageInfo.versionName;
        }
        catch (PackageManager.NameNotFoundException nnfe)
        {
            nnfe.printStackTrace();
        }
    }
}
