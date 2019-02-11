package teckvillage.developer.khaled_pc.teckvillagetrue.View;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mukesh.countrypicker.Country;
import com.mukesh.countrypicker.CountryPicker;
import com.mukesh.countrypicker.OnCountryPickerListener;

import teckvillage.developer.khaled_pc.teckvillagetrue.R;
import teckvillage.developer.khaled_pc.teckvillagetrue.verification;

public class LoginActivity extends AppCompatActivity implements TextWatcher {

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
        phoneNumber.addTextChangedListener(this);
        codeCountry=findViewById(R.id.CodeCountry);
        continu=findViewById(R.id.continu);


        codeCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countryPicker.showDialog(getSupportFragmentManager());
            }
        });

        countryPicker = new CountryPicker.Builder().with(this)
                        .listener(new OnCountryPickerListener() {
                            @Override
                            public void onSelectCountry(Country country) {
                                myCountry=country;
                                String code_country=country.getName()+" ("+country.getDialCode()+")";
                                codeCountry.setText(code_country);
                                codeCountry.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_tick, 0);
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
        }

        if(isValidPhoneNumber(phoneNumber.getText().toString().trim())){
            phoneNumber.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_tick, 0);

        }
    }

    private boolean isValidPhoneNumber(CharSequence phoneNumber) {
        if (!TextUtils.isEmpty(phoneNumber)&&phoneNumber.length() > 4) {
            return true;
        }
        return false;
    }


    private boolean setcross(CharSequence phoneNumber) {
        if (!TextUtils.isEmpty(phoneNumber)&&phoneNumber.length() > 1&&phoneNumber.length() <= 4) {
            return true;
        }
        return false;
    }



}
