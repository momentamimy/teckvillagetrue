package com.developer.whocaller.net;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.sinch.verification.CodeInterceptionException;
import com.sinch.verification.Config;
import com.sinch.verification.InitiationResult;
import com.sinch.verification.InvalidInputException;
import com.sinch.verification.ServiceErrorException;
import com.sinch.verification.SinchVerification;
import com.sinch.verification.Verification;
import com.sinch.verification.VerificationListener;


public class verification extends AppCompatActivity {

    private final String APPLICATION_KEY = "d85acaee-fdcf-4c9e-8f2f-a23d4e68960d";

    EditText numberPhone;
    Button verify;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);


        numberPhone = (EditText) findViewById(R.id.phoneNumber);
        verify = (Button) findViewById(R.id.verify);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        verify.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String number = numberPhone.getText().toString();
                if(number.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Phone number cannot be empty!", Toast.LENGTH_LONG).show();
                }
                else {
                    showProgressDialog();
                    startVerification(number);
                }
            }
        });
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
            hideProgressDialog();
            if (e instanceof InvalidInputException) {
                Toast.makeText(verification.this,"Incorrect number provided", Toast.LENGTH_LONG).show();
            } else if (e instanceof ServiceErrorException) {
                Toast.makeText(verification.this,"Sinch service error", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(verification.this,"Other system error, check your network state", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onVerified() {
            hideProgressDialog();
            new AlertDialog.Builder(verification.this)
                    .setMessage("Verification Successful!")
                    .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            dialog.cancel();
                        }
                    })
                    .show();
        }

        @Override
        public void onVerificationFailed(Exception e) {
            hideProgressDialog();
            if (e instanceof CodeInterceptionException) {
                Toast.makeText(verification.this,"Intercepting the verification call automatically failed", Toast.LENGTH_LONG).show();
            } else if (e instanceof ServiceErrorException) {
                Toast.makeText(verification.this, "Sinch service error", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(verification.this,"Other system error, check your network state", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onVerificationFallback() {

        }
    }
    private void showProgressDialog() {
        progressBar.setVisibility(ProgressBar.VISIBLE);
    }

    private void hideProgressDialog() {
        progressBar.setVisibility(ProgressBar.INVISIBLE);
    }
}

