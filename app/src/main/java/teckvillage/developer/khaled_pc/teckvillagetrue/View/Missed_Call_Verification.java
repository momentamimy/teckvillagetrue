package teckvillage.developer.khaled_pc.teckvillagetrue.View;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.sinch.verification.CodeInterceptionException;
import com.sinch.verification.Config;
import com.sinch.verification.InitiationResult;
import com.sinch.verification.InvalidInputException;
import com.sinch.verification.ServiceErrorException;
import com.sinch.verification.SinchVerification;
import com.sinch.verification.Verification;
import com.sinch.verification.VerificationListener;

import teckvillage.developer.khaled_pc.teckvillagetrue.R;

public class Missed_Call_Verification extends AppCompatActivity {
    private final String APPLICATION_KEY = "e0a9bd29-08ef-425c-9a03-84d04aa7ef7e";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_missed__call__verification);

        String number=getIntent().getStringExtra("num");

        if(number.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Phone number cannot be empty!",Toast.LENGTH_LONG).show();
        }
        else {
            startVerification(number);
        }

        /*ImageView dd=findViewById(R.id.centerparent);
        dd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Signup.class);
                startActivity(intent);
                finish();
            }
        });*/
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
                Toast.makeText(getApplicationContext(),"Incorrect number provided",Toast.LENGTH_LONG).show();
            } else if (e instanceof ServiceErrorException) {
                Toast.makeText(getApplicationContext(),"Sinch service error",Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(),"Other system error, check your network state", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onVerified() {
            Intent intent = new Intent(getApplicationContext(), Signup.class);
            startActivity(intent);
            finish();
        }

        @Override
        public void onVerificationFailed(Exception e) {
            if (e instanceof CodeInterceptionException) {
                Toast.makeText(getApplicationContext(),"Intercepting the verification call automatically failed",Toast.LENGTH_LONG).show();
            } else if (e instanceof ServiceErrorException) {
                Toast.makeText(getApplicationContext(), "Sinch service error",Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(),"Other system error, check your network state", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onVerificationFallback() {

        }
    }
}
