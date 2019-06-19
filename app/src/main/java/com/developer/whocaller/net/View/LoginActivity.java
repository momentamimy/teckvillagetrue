package com.developer.whocaller.net.View;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.developer.whocaller.net.Controller.LocaleHelper;
import com.developer.whocaller.net.R;
import com.developer.whocaller.net.SMSVerificationActivity;
import com.mukesh.countrypicker.Country;
import com.mukesh.countrypicker.CountryPicker;
import com.mukesh.countrypicker.OnCountryPickerListener;
import com.sdsmdg.tastytoast.TastyToast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;



public class LoginActivity extends AppCompatActivity implements TextWatcher {

    CountryPicker countryPicker;
    TextView codeCountry;
    EditText phoneNumber;
    TextView continu;
    Country myCountry=null;
    boolean PhoneNumberValid;
    SharedPreferences sharedPreferences;
    boolean IsPhoneNumberMissed_Call_VerificationHappen;
    String  countrycodee,phnum,countryname;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        phoneNumber=findViewById(R.id.phone_num);
        phoneNumber.addTextChangedListener(this);
        codeCountry=findViewById(R.id.CodeCountry);
        continu=findViewById(R.id.continu);

        sharedPreferences = getSharedPreferences("WhoCaller?", Context.MODE_PRIVATE);
        IsPhoneNumberMissed_Call_VerificationHappen= sharedPreferences.getBoolean("IsPhoneNumberMissedCallVerification", false);
        countrycodee = sharedPreferences.getString("Countrycode","false");
        phnum = sharedPreferences.getString("PhoneNumVerified","false");
        countryname = sharedPreferences.getString("UserCountry","false");

        //Close This Activity If Phone number Verified and Open Signup ACtivity
        if(IsPhoneNumberMissed_Call_VerificationHappen){
            Intent intent = new Intent(getApplicationContext(), Signup.class);
            intent.putExtra("countrycode",countrycodee);
            intent.putExtra("phonenumber",phnum);
            intent.putExtra("contryname",countryname);
            startActivity(intent);
            finish();

        }

        codeCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Sort Countries
                List<Country> countryArrayList = new ArrayList<>();
                countryArrayList = countryPicker.getAllCountries();
                Collections.sort(countryArrayList, new Comparator<Country>() {
                    @Override
                    public int compare(Country country1, Country country2) {
                        return country1.getName().trim().compareToIgnoreCase(country2.getName().trim());
                    }
                });

                countryPicker.sortCountries(countryArrayList);//Set Sorted list Of Countries
                countryPicker.showDialog(getSupportFragmentManager());//Show Dialog

            }
        });

        countryPicker = new CountryPicker.Builder().with(this).listener(new OnCountryPickerListener() {
                            @Override
                            public void onSelectCountry(Country country) {
                                myCountry=country;
                                String code_country=country.getName()+" ("+country.getDialCode()+")";
                                codeCountry.setText(code_country);
                                codeCountry.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_tick, 0);

                            }
                        }).build();

        //On Click continue btn
        continu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Check wifi or data available
              if (CheckNetworkConnection.hasInternetConnection(LoginActivity.this)) {

                    //Check internet Access
                   if (ConnectionDetector.hasInternetConnection(LoginActivity.this)) {

                        if (myCountry == null) {
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.You_Should_Select_Your_Country), Toast.LENGTH_LONG).show();
                            return;
                        } else if (!PhoneNumberValid) {
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.You_Should_Enter_Correct_Phone_Number), Toast.LENGTH_LONG).show();
                            return;
                        } else if (myCountry.getName().equals("Egypt") && phoneNumber.getText().toString().trim().length() < 11 || phoneNumber.getText().toString().trim().length() > 11) {

                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.You_Should_Enter_Correct_Phone_Number), Toast.LENGTH_LONG).show();
                            return;
                        }

                        //Toast.makeText(getApplicationContext(),myCountry.getDialCode().toString(),Toast.LENGTH_LONG).show();
                        String number = phoneNumber.getText().toString().trim();
                        String numberwithcode = myCountry.getDialCode() + phoneNumber.getText().toString().trim();
                        String councode = myCountry.getDialCode();
                        String contryname = myCountry.getName();
                        Intent intent = new Intent(getApplicationContext(), SMSVerificationActivity.class);
                        intent.putExtra("num", number);
                        intent.putExtra("numwtihcode", numberwithcode);
                        intent.putExtra("codenum", councode);
                        intent.putExtra("contryname", contryname);
                        startActivity(intent);
                        finish();

          } else {
                TastyToast.makeText(LoginActivity.this, getResources().getString(R.string.Internet_not_access_Please_connect_to_the_internet), TastyToast.LENGTH_LONG, TastyToast.ERROR);
            }
        } else {
            TastyToast.makeText(LoginActivity.this, getResources().getString(R.string.You_are_offline_Please_connect_to_the_internet), TastyToast.LENGTH_LONG, TastyToast.ERROR);
        }
            }
        });
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }


    @Override
    public void afterTextChanged(Editable s) {

        if(phoneNumber.getText().toString().trim().length()==0){
            phoneNumber.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        }

        if(setcross(phoneNumber.getText().toString().trim())){
            phoneNumber.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_close, 0);
            PhoneNumberValid=false;
        }

        if(isValidPhoneNumber(phoneNumber.getText().toString().trim())){
            phoneNumber.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_tick, 0);
            PhoneNumberValid=true;
        }
    }

    private boolean isValidPhoneNumber(CharSequence phoneNumber) {
        if (!TextUtils.isEmpty(phoneNumber)&&phoneNumber.length() >= 5) {
            return true;
        }
        return false;
    }


    private boolean setcross(CharSequence phoneNumber) {
        if (!TextUtils.isEmpty(phoneNumber)&&phoneNumber.length() >= 1&&phoneNumber.length() < 5) {
            return true;
        }
        return false;
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base));
    }
}
