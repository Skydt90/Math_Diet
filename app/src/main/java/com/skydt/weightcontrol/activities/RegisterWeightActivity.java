package com.skydt.weightcontrol.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.skydt.weightcontrol.R;
import com.skydt.weightcontrol.models.BodyWeighIn;
import com.skydt.weightcontrol.models.Day;
import com.skydt.weightcontrol.models.FoodWeighIn;
import com.skydt.weightcontrol.services.BodyWeighInService;
import com.skydt.weightcontrol.services.DayService;
import com.skydt.weightcontrol.services.FoodWeighInService;

public class RegisterWeightActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher
{
    private EditText etNewWeight;
    private RadioGroup rgMealType;
    private Button btnRegister;
    private String btnText;
    private int dietID;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_weight_activity);
        extractIntent();
        loadInterface();
    }

    private void extractIntent()
    {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
        {
            btnText = bundle.getString("btnText");
            dietID = bundle.getInt("dietID");
        }
        else
        {
            btnText = "";
            dietID = 0;
        }
    }

    private void loadInterface()
    {
        etNewWeight = findViewById(R.id.etNewWeight);
        rgMealType = findViewById(R.id.rbGroup);
        btnRegister = findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(this);
        if (!btnText.equals("Mad"))
        {
            rgMealType.setAlpha(0f);
            etNewWeight.setHint("Nuværende vægt");
        }
        etNewWeight.addTextChangedListener(this);
        btnRegister.setClickable(false);
        btnRegister.setAlpha(0.2f);
    }

    @Override
    public void onClick(View v)
    {
        DayService dayService = new DayService();
        Day day = dayService.loadDayByPrimaryKey(dayService.getCurrentDateAsString(), dietID, this);
        Intent intent = new Intent(this, MainMenuActivity.class);
        BodyWeighInService bodyWeighInService = new BodyWeighInService();
        BodyWeighIn bodyWeighIn;
        double enteredWeight;

        switch (btnText)
        {
            case "Morgenvægt":
                day.setMorningWeight(Double.parseDouble(etNewWeight.getText().toString()));
                dayService.updateMorningWeight(day, day.getMorningWeight(), this);
                enteredWeight = Double.parseDouble(etNewWeight.getText().toString());
                bodyWeighIn = new BodyWeighIn(day.getDayID(), day.getDietID(), enteredWeight);
                bodyWeighInService.createWeighIn(bodyWeighIn, this);
                break;

            case "Krop":
                enteredWeight = Double.parseDouble(etNewWeight.getText().toString());
                bodyWeighIn = new BodyWeighIn(day.getDayID(), day.getDietID(), enteredWeight);
                bodyWeighInService.createWeighIn(bodyWeighIn, this);
                dayService.updateAllowedFoodIntakeBasedOnBodyWeighIn(day, enteredWeight, this);
                break;

            case "Mad":
                FoodWeighInService foodWeighInService = new FoodWeighInService();
                enteredWeight = Double.parseDouble(etNewWeight.getText().toString());
                int selectedId = rgMealType.getCheckedRadioButtonId();
                RadioButton rbSelectedMeal = findViewById(selectedId);
                FoodWeighIn foodWeighIn = new FoodWeighIn(day.getDayID(), day.getDietID(), enteredWeight, rbSelectedMeal.getText().toString());
                foodWeighInService.createWeighIn(foodWeighIn, this);
                dayService.updateAllowedFoodIntakeBasedOnFoodWeighIn(day, foodWeighIn.getFoodWeighIn(), this);
                break;

            default:
                break;
        }
        startActivity(intent);
        Toast.makeText(RegisterWeightActivity.this, R.string.registreret, Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after)
    {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count)
    {
        btnRegister.setClickable(true);
        btnRegister.setAlpha(1);
    }

    @Override
    public void afterTextChanged(Editable s)
    {
        if(s.toString().trim().length()==0)
        {
            btnRegister.setClickable(false);
            btnRegister.setAlpha(0.2f);
        }
    }

    public void hideKeyBoard(View view)
    {
        InputMethodManager inputMethodManager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
