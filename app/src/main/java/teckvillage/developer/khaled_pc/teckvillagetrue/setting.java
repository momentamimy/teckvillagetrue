package teckvillage.developer.khaled_pc.teckvillagetrue;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;

public class setting extends AppCompatActivity {


    Switch switchOutgoing;
    Switch switchIncoming;

    SharedPreferences preferences;

    RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        preferences=getSharedPreferences("PopUp_Dialog",MODE_PRIVATE);

        getSupportActionBar().setTitle("Setting");
        getSupportActionBar().setDisplayShowHomeEnabled(true);   //back button on App Bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);   //back button on App Bar

        intiateSwitchRadioGroup();
        switchOutgoing.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor=preferences.edit();
                editor.putBoolean("SwitchOutgoing",isChecked);
                editor.apply();
            }
        });

        switchIncoming.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor=preferences.edit();
                editor.putBoolean("switchIncoming",isChecked);
                editor.apply();

            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                SharedPreferences.Editor editor=preferences.edit();
                switch (checkedId)
                {
                    case R.id.radioButton:
                        editor.putString("DialogPosition","Top");
                        editor.apply();
                        Toast.makeText(getApplicationContext(),"Top",Toast.LENGTH_LONG).show();
                        break;
                    case R.id.radioButton2:
                        editor.putString("DialogPosition","Center");
                        editor.commit();
                        Toast.makeText(getApplicationContext(),"Center",Toast.LENGTH_LONG).show();
                        break;
                    case R.id.radioButton3:
                        editor.putString("DialogPosition","Bottom");
                        editor.commit();
                        Toast.makeText(getApplicationContext(),"Bottom",Toast.LENGTH_LONG).show();
                        break;
                }
            }
        });
    }


    public void intiateSwitchRadioGroup()
    {
        boolean swithcOutgoingValue=preferences.getBoolean("SwitchOutgoing",true);
        boolean swithcincomgoingValue=preferences.getBoolean("switchIncoming",true);
        String dialogPositionValue=preferences.getString("DialogPosition","Center");

        radioGroup=findViewById(R.id.radioGroup1);

        switchOutgoing=findViewById(R.id.switch_outgoing);
        switchIncoming=findViewById(R.id.switch_income);


        if (swithcincomgoingValue)
        {
            switchIncoming.setChecked(true);
        }
        else
        {
            switchIncoming.setChecked(false);
        }
        if (swithcOutgoingValue)
        {
            switchOutgoing.setChecked(true);
        }
        else
        {
            switchOutgoing.setChecked(false);
        }

        if (dialogPositionValue.equals("Top"))
        {
            radioGroup.check(R.id.radioButton);
        }
        else if (dialogPositionValue.equals("Center"))
        {
            radioGroup.check(R.id.radioButton2);
        }
        else if (dialogPositionValue.equals("Bottom"))
        {
            radioGroup.check(R.id.radioButton3);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }


}
