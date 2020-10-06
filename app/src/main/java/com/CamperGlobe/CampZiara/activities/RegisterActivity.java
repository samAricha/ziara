package com.CamperGlobe.CampZiara.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.CamperGlobe.CampZiara.R;
import com.CamperGlobe.CampZiara.utils.CountryData;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    private EditText vPhoneNumber;
    private Button vButton;
    private Spinner spinnerCountries;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        FirebaseApp.initializeApp(this);
        vPhoneNumber = findViewById(R.id.regMobile);
        vButton = findViewById(R.id.verificationButton);
        spinnerCountries = findViewById(R.id.spinnerCountries);

        spinnerCountries.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, CountryData.countryNames));

        vButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String areaCode = CountryData.countryAreaCodes[spinnerCountries.getSelectedItemPosition()];
                String number = vPhoneNumber.getText().toString().trim();

                if (number.isEmpty() || number.length() < 9) {
                    vPhoneNumber.setError("please enter a valid number");
                    vPhoneNumber.requestFocus();
                    return;
                }

                String phoneNumber = "+" + areaCode + number;
                Intent intent = new Intent(RegisterActivity.this, VerifyPhoneActivity.class);
                intent.putExtra("number", phoneNumber);
                startActivity(intent);

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            Intent intent = new Intent(this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            finish();
            startActivity(intent);
        }
    }
}