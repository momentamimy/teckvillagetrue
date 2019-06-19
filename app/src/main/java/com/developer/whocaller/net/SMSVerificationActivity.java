package com.developer.whocaller.net;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.developer.whocaller.net.View.Signup;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class SMSVerificationActivity extends AppCompatActivity {


    private String phoneVerificationId="";
    private String verificationCode="";
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks verificationCallbacks;
    private PhoneAuthProvider.ForceResendingToken resendToken;
    private FirebaseAuth fbAuth;

    ProgressDialog dialog;
    TextView codeText;

    String coincode;
    String number;
    String numberwithcode;
    String contryname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_verification);

        fbAuth = FirebaseAuth.getInstance();

        number=getIntent().getStringExtra("num");
        coincode=getIntent().getStringExtra("codenum");
        contryname=getIntent().getStringExtra("contryname");
        numberwithcode=getIntent().getStringExtra("numwtihcode");

        codeText=findViewById(R.id.Code_Text);

        if(numberwithcode.isEmpty()) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.Phone_number_cannot_be_empty),Toast.LENGTH_LONG).show();
        }
        else {
            sendCode(numberwithcode);
        }
    }

    //--------------------------------------------------------------------------------------------------------
    public void sendCode(String phoneNumber) {
        dialog = new ProgressDialog(SMSVerificationActivity.this);//open  ProgressDialog
        dialog.setMessage("جار التحميل...");
        dialog.setCancelable(false);
        dialog.show();


        setUpVerificatonCallbacks();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                120,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                verificationCallbacks);
    }
    //--------------------------------------------------------------------------------------------------------

    //--------------------------------------------------------------------------------------------------------
    private void setUpVerificatonCallbacks() {
        verificationCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verificaiton without
                //     user action.

                verificationCode=credential.getSmsCode();

                codeText.setText(verificationCode);
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                signInWithPhoneAuthCredential(credential);
            }
            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    // Invalid request
                    Toast.makeText(getApplicationContext(), "wrong request" + e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // SMS quota exceeded
                    Toast.makeText(getApplicationContext(),"limitations exceeded",Toast.LENGTH_LONG).show();
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }
            }

            @Override
            public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.

                // Save verification ID and resending token so we can use them later
                phoneVerificationId = verificationId;
                resendToken = token;

            }
        };
    }
    //--------------------------------------------------------------------------------------------------------



    //--------------------------------------------------------------------------------------------------------
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {

        fbAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }

//Phone Number Verified Success Put SharePreference
                    SharedPreferences sharedPreferences = getSharedPreferences("WhoCaller?", MODE_PRIVATE);
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
                    FirebaseAuth.getInstance().signOut();
                } else {
                    if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                        Toast.makeText(getApplicationContext(),"الكود غير صحيح",Toast.LENGTH_LONG).show();
                        if (dialog.isShowing()) {
                            dialog.dismiss();
                        }
                    }
                }
            }
        }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();

            }
        });
    }
    //--------------------------------------------------------------------------------------------------------
}
