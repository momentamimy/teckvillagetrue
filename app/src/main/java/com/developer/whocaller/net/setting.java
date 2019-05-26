package com.developer.whocaller.net;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;


import com.developer.whocaller.net.Controller.LocaleHelper;

import java.util.Locale;

public class setting extends AppCompatActivity {

    TextView changeLang;

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


        changeLang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyCustomLanguageDialog();
            }
        });

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

        changeLang=findViewById(R.id.changLang);

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



    Dialog MyDialogLanguage;

    public void MyCustomLanguageDialog() {
        MyDialogLanguage = new Dialog(setting.this);
        MyDialogLanguage.requestWindowFeature(Window.FEATURE_NO_TITLE);
        MyDialogLanguage.setContentView(R.layout.language_dialog);
        Window window = MyDialogLanguage.getWindow();
        window.setLayout(Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT);
        MyDialogLanguage.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView english,arabic;
        english=MyDialogLanguage.findViewById(R.id.English);
        arabic=MyDialogLanguage.findViewById(R.id.Arabic);

        english.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences =getSharedPreferences("LANGUAGE",MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putString("lang","en");
                editor.commit();
                updateViews("en");
                changeLang.setText("\u25CF  change Language:   en");

                MyDialogLanguage.dismiss();
            }
        });

        arabic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences =getSharedPreferences("LANGUAGE",MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putString("lang","ar");
                editor.commit();
                updateViews("ar");
                changeLang.setText(" \u25CF  تغير اللغة:  عربية");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    //setLocale("ar");
                }
                MyDialogLanguage.dismiss();
            }
        });

        MyDialogLanguage.show();
    }

    /*
    private void restartActivity() {
        Intent intent=new Intent(getApplicationContext(),MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

        Intent intent1 = getIntent();
        startActivity(intent1);
    }*/
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base));
    }
    private void updateViews(String languageCode) {
        Context context = LocaleHelper.setLocale(this, languageCode);
        Resources resources = context.getResources();

        changeLang.setText(resources.getString(R.string.StringLangSetting));

    }
}
