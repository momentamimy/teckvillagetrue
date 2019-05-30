package com.developer.whocaller.net.View;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.developer.whocaller.net.Controller.LocaleHelper;
import com.developer.whocaller.net.R;
import com.sinch.verification.CodeInterceptionException;
import com.sinch.verification.Config;
import com.sinch.verification.InitiationResult;
import com.sinch.verification.InvalidInputException;
import com.sinch.verification.ServiceErrorException;
import com.sinch.verification.SinchVerification;
import com.sinch.verification.Verification;
import com.sinch.verification.VerificationListener;



public class Missed_Call_Verification extends AppCompatActivity {
    private final String APPLICATION_KEY = "033abb17-bec3-4157-9bf4-407bedcebf90";
    SharedPreferences sharedPreferences;
    String coincode;
    String number;
    String numberwithcode;
    String contryname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_missed__call__verification);
        Log.w("plp","lol");
        try {

            //Check wifi or data available
            if (CheckNetworkConnection.hasInternetConnection(Missed_Call_Verification.this)) {

                //Check internet Access
                if (ConnectionDetector.hasInternetConnection(Missed_Call_Verification.this)) {

                        number=getIntent().getStringExtra("num");
                        coincode=getIntent().getStringExtra("codenum");
                        contryname=getIntent().getStringExtra("contryname");
                        numberwithcode=getIntent().getStringExtra("numwtihcode");
                        //Log.w("code",coincode);

                        if(numberwithcode.isEmpty()) {
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.Phone_number_cannot_be_empty),Toast.LENGTH_LONG).show();
                        }
                        else {
                            startVerification(numberwithcode);
                        }

                } else {
                    /*View parentLayout = findViewById(android.R.id.content);
                    Snackbar.make(parentLayout, "Internet not access Please connect to the internet", Snackbar.LENGTH_LONG)
                            .setAction("CLOSE", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            })
                            .setActionTextColor(getResources().getColor(android.R.color.white ))
                            .show();*/
                    View parentLayout = findViewById(android.R.id.content);
                    Snackbar.make(parentLayout, getResources().getString(R.string.You_are_offline_Please_connect_to_the_internet), Snackbar.LENGTH_LONG)
                            .setActionTextColor(getResources().getColor(android.R.color.white ))
                            .show();
                    //TastyToast.makeText(Missed_Call_Verification.this, "Internet not access Please connect to the internet", TastyToast.LENGTH_LONG, TastyToast.ERROR);
                    thread.start();

                }
            } else {
                View parentLayout = findViewById(android.R.id.content);
                Snackbar.make(parentLayout, getResources().getString(R.string.You_are_offline_Please_connect_to_the_internet), Snackbar.LENGTH_LONG)
                        .setActionTextColor(getResources().getColor(android.R.color.white ))
                        .show();
                //TastyToast.makeText(Missed_Call_Verification.this, "You're offline. Please connect to the internet", TastyToast.LENGTH_LONG, TastyToast.ERROR);
                thread.start();
            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }


    private void startVerification(String phoneNumber) {
        Config config = SinchVerification.config().applicationKey(APPLICATION_KEY).context(getApplicationContext()).build();
        VerificationListener listener = new MyVerificationListener();
        Verification verification = SinchVerification.createFlashCallVerification(config, phoneNumber, listener);
        verification.initiate();
    }

    private class MyVerificationListener implements VerificationListener {

        @Override
        public void onInitiated(InitiationResult initiationResult) {

        }

        @Override
        public void onInitiationFailed(Exception e) {
            if (e instanceof InvalidInputException) {
                Toast.makeText(getApplicationContext(),getResources().getString(R.string.Incorrect_number_provided),Toast.LENGTH_LONG).show();
            } else if (e instanceof ServiceErrorException) {
                Toast.makeText(getApplicationContext(),getResources().getString(R.string.service_error),Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(),getResources().getString(R.string.Check_your_network_state), Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onVerified() {
            //Phone Number Verified Success Put SharePreference
            sharedPreferences = getSharedPreferences("WhoCaller?", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("IsPhoneNumberMissedCallVerification", true);
            editor.putString("Countrycode", coincode);
            editor.putString("UserCountry", contryname);
            editor.putString("PhoneNumVerified", number);
            editor.apply();

            Intent intent = new Intent(getApplicationContext(), Signup.class);
            intent.putExtra("countrycode",coincode);
            intent.putExtra("phonenumber",number);
            intent.putExtra("contryname",contryname);
            startActivity(intent);
            finish();

        }

        @Override
        public void onVerificationFailed(Exception e) {
            if (e instanceof CodeInterceptionException) {
                Toast.makeText(getApplicationContext(),getResources().getString(R.string.Intercepting_the_verification_call_automatically_failed),Toast.LENGTH_LONG).show();
            } else if (e instanceof ServiceErrorException) {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.service_error),Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(),getResources().getString(R.string.Check_your_network_state), Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onVerificationFallback() {

        }
    }

    //timer
    Thread thread = new Thread(){
        @Override
        public void run() {
            try {
                Thread.sleep(3400); // As I am using LENGTH_LONG in Toast
                Intent intent=new Intent(Missed_Call_Verification.this,SplashScreen.class);
                startActivity(intent);
                Missed_Call_Verification.this.finish();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base));
    }
}
