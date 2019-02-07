package teckvillage.developer.khaled_pc.teckvillagetrue.View;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mukesh.countrypicker.Country;
import com.mukesh.countrypicker.CountryPicker;
import com.mukesh.countrypicker.OnCountryPickerListener;

import teckvillage.developer.khaled_pc.teckvillagetrue.R;
import teckvillage.developer.khaled_pc.teckvillagetrue.verification;

public class LoginActivity extends AppCompatActivity {

    CountryPicker countryPicker;
    TextView codeCountry;
    EditText phoneNumber;
    TextView continu;
    Country myCountry=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        phoneNumber=findViewById(R.id.phone_num);
        codeCountry=findViewById(R.id.CodeCountry);
        continu=findViewById(R.id.continu);
        codeCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countryPicker.showDialog(getSupportFragmentManager());
            }
        });
        countryPicker =
                new CountryPicker.Builder().with(this)
                        .listener(new OnCountryPickerListener() {
                            @Override
                            public void onSelectCountry(Country country) {
                                myCountry=country;
                                String code_country=country.getName()+" ("+country.getDialCode()+")";
                                codeCountry.setText(code_country);
                            }
                        }).build();
        continu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myCountry==null)
                {
                    Toast.makeText(getApplicationContext(),"you shold select your country",Toast.LENGTH_LONG).show();
                    return;
                }
                String number=myCountry.getDialCode()+phoneNumber.getText().toString();
                Intent intent=new Intent(getApplicationContext(), Missed_Call_Verification.class);
                intent.putExtra("num",number);
                startActivity(intent);
                finish();
            }
        });
    }
}
