package com.developer.whocaller.net;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;


import com.developer.whocaller.net.Controller.LocaleHelper;
import com.developer.whocaller.net.View.SplashScreen;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class setting extends AppCompatActivity {

    TextView changeLang,privacy;

    Switch switchOutgoing;
    Switch switchIncoming;

    SharedPreferences preferences;

    TextView callWindowLocation;
    RadioGroup radioGroup;

    RadioButton radioButton,radioButton2,radioButton3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        preferences=getSharedPreferences("PopUp_Dialog",MODE_PRIVATE);

        getSupportActionBar().setTitle(getResources().getString(R.string.settings));
        getSupportActionBar().setDisplayShowHomeEnabled(true);   //back button on App Bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);   //back button on App Bar

        intiateSwitchRadioGroup();

        privacy=findViewById(R.id.legel);
        privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogPrivacyandTerms();
            }
        });

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

    private void openDialogPrivacyandTerms() {
        final String[] Option = {getString(R.string.Terms_of_Service),getString(R.string.Privacy_Policy)};

        final AlertDialog.Builder builder = new AlertDialog.Builder(setting.this);
        builder.setTitle(getString(R.string.legal_amp_privacy));
        builder.setItems(Option, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // the user clicked on colors[which]
                if(Option[which].equals(getString(R.string.Terms_of_Service))){
                    //open link
                    openTermsOfService(setting.this);

                }else if(Option[which].equals(getString(R.string.Privacy_Policy))) {
                    openPrivacyPolice(setting.this);
                }

            }
        });
        builder.show();
    }


    public void intiateSwitchRadioGroup()
    {
        boolean swithcOutgoingValue=preferences.getBoolean("SwitchOutgoing",true);
        boolean swithcincomgoingValue=preferences.getBoolean("switchIncoming",true);
        String dialogPositionValue=preferences.getString("DialogPosition","Center");

        changeLang=findViewById(R.id.changLang);


        callWindowLocation=findViewById(R.id.call_window_location);

        radioGroup=findViewById(R.id.radioGroup1);
        radioButton=findViewById(R.id.radioButton);
        radioButton2=findViewById(R.id.radioButton2);
        radioButton3=findViewById(R.id.radioButton3);

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
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    //setLocale("ar");
                }
                MyDialogLanguage.dismiss();
            }
        });

        MyDialogLanguage.show();
    }


    private void restartActivity() {
        finish();
        Intent intent1 =new Intent(this, SplashScreen.class);
        intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent1);
    }
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base));
    }
    private void updateViews(String languageCode) {
        Context context = LocaleHelper.setLocale(this, languageCode);
        Resources resources = context.getResources();

        restartActivity();

    }

    static  void openPrivacyPolice(Context context){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("https://whocaller.net/privacy-policy/"));
        context.startActivity(intent);
    }

    static  void openTermsOfService(Context context){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("https://whocaller.net/terms-of-service/"));
        context.startActivity(intent);
    }
}
