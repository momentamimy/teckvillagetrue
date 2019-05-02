package teckvillage.developer.khaled_pc.teckvillagetrue;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import teckvillage.developer.khaled_pc.teckvillagetrue.Model.Get_Calls_Log;
import teckvillage.developer.khaled_pc.teckvillagetrue.Model.database.Database_Helper;
import teckvillage.developer.khaled_pc.teckvillagetrue.Model.retrofit.ApiAccessToken;
import teckvillage.developer.khaled_pc.teckvillagetrue.Model.retrofit.JSON_Mapping.BodyNumberModel;
import teckvillage.developer.khaled_pc.teckvillagetrue.Model.retrofit.JSON_Mapping.FetchedUserData;
import teckvillage.developer.khaled_pc.teckvillagetrue.Model.retrofit.WhoCallerApi;
import teckvillage.developer.khaled_pc.teckvillagetrue.Model.retrofit.retrofitHead;
import teckvillage.developer.khaled_pc.teckvillagetrue.View.CheckNetworkConnection;
import teckvillage.developer.khaled_pc.teckvillagetrue.View.ConnectionDetector;
import teckvillage.developer.khaled_pc.teckvillagetrue.View.User_Contact_Profile_From_log_list;

public class PopupDialogActivity extends Activity {


    TextView CallerName;
    TextView CallerCountry;
    RelativeLayout ProfileBlurLayOut;
    ImageView CallerImage;
    ImageView CloseDialog;

    TextView CallerNumber;
    TextView CallerNumberType;

    RelativeLayout IconCall, IconMessage, IconSave, IconBlock;
    Button viewProfile;

    ImageView CallerImageProgress;
    ProgressBar CallerProgress;
    Get_Calls_Log get_calls_log;
    List<String> BlockNumbers;
    Database_Helper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup_dialog);
        String number = getIntent().getStringExtra("Number");
        MyCustomAssignNumDialog(number);
        WhoCallerDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                finish();
            }
        });

    }

    Dialog WhoCallerDialog;

    public void MyCustomAssignNumDialog(final String number) {
        WhoCallerDialog = new Dialog(this, R.style.DialogTheme);
        WhoCallerDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        WhoCallerDialog.setContentView(R.layout.who_caller_dialog);
        WhoCallerDialog.show();
        Window window = WhoCallerDialog.getWindow();
        window.setLayout(Toolbar.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.WRAP_CONTENT);
        WhoCallerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        CallerName = WhoCallerDialog.findViewById(R.id.Caller_Name);
        CallerCountry = WhoCallerDialog.findViewById(R.id.Caller_Country);
        ProfileBlurLayOut = WhoCallerDialog.findViewById(R.id.ProfileBlurLayOut);
        CallerImage = WhoCallerDialog.findViewById(R.id.Caller_Image);
        CloseDialog = WhoCallerDialog.findViewById(R.id.Close_Dialog);

        CallerNumber = WhoCallerDialog.findViewById(R.id.Caller_Number);
        CallerNumberType = WhoCallerDialog.findViewById(R.id.Caller_Number_Type);

        CallerImageProgress = WhoCallerDialog.findViewById(R.id.progress_contact_img);
        CallerProgress = WhoCallerDialog.findViewById(R.id.progress);

        IconCall = WhoCallerDialog.findViewById(R.id.Call_Icon_Layout);
        IconMessage = WhoCallerDialog.findViewById(R.id.Message_Icon_Layout);
        IconSave = WhoCallerDialog.findViewById(R.id.Save_Icon_Layout);
        IconBlock = WhoCallerDialog.findViewById(R.id.Block_Icon_Layout);
        viewProfile=WhoCallerDialog.findViewById(R.id.ViewProfile);

        get_calls_log=new Get_Calls_Log(getApplicationContext());
        if (get_calls_log.contactExists(number))
        {
            IconSave.setVisibility(View.GONE);
        }

        viewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), User_Contact_Profile_From_log_list.class);
                    intent.putExtra("ContactNUm",number);
                    startActivity(intent);
                }
        });

        IconCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:" + number));
                    startActivity(intent);
                }
            }
        });

        IconMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                get_calls_log=new Get_Calls_Log(getApplicationContext());
                String contactName= (String) get_calls_log.getContactName(number);
                Intent intent=new Intent(getApplicationContext(), SMS_MessagesChat.class);
                if (TextUtils.isEmpty(contactName)) {
                    intent.putExtra("LogSMSName",number);
                }else {
                    intent.putExtra("LogSMSName",contactName);
                }
                intent.putExtra("LogSMSAddress",number);
                startActivity(intent);
            }
        });

        IconSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ContactsContract.Intents.Insert.ACTION);
                intent.setType(ContactsContract.RawContacts.CONTENT_TYPE);
                intent.putExtra(ContactsContract.Intents.Insert.PHONE, number);
                if (!CallerName.getText().toString().equals(number))
                {
                    intent.putExtra(ContactsContract.Intents.Insert.NAME, CallerName.getText().toString());
                }
                startActivity(intent);
            }
        });

        IconBlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //khaled
                WhoCallerDialog.dismiss();
                String contactName=CallerName.getText().toString();
                Insert_Number_To_Blocklist( number,contactName );
            }
        });

        get_calls_log=new Get_Calls_Log(this);
        String contactName= (String) get_calls_log.getContactName(number);
        Bitmap contactPhoto=  get_calls_log.retrieveContactPhoto(number);
        if (!TextUtils.isEmpty(contactName))
        {
            CallerName.setText(contactName);
            CallerNumber.setText(number);
        }
        else
        {
            CallerName.setText(number);
            CallerNumber.setText(number);
        }


        if (contactPhoto!=null)
        {
            BlurBuilder blurBuilder=new BlurBuilder();
            CallerImage.setImageBitmap(contactPhoto);
            Bitmap resultBmp = blurBuilder.blur(this, contactPhoto);
            ProfileBlurLayOut.setBackgroundDrawable(new BitmapDrawable(resultBmp));
        }
        //khaled Block
        boolean isBlock=false;
        //Block List Phone numbers
        BlockNumbers = RetreiveAllNumberInBlockList(PopupDialogActivity.this);

        if (BlockNumbers.size() > 0) {
            for (int i = 0; i < BlockNumbers.size(); i++) {
                if ((BlockNumbers.get(i) != null) && BlockNumbers.get(i).equalsIgnoreCase(number
                )) {
                    isBlock=true;
                }
            }
        }
        if (number.length()<=4||isBlock)
        {
            ProfileBlurLayOut.setBackgroundColor(getResources().getColor(R.color.redColor));
            CallerImage.setImageResource(R.drawable.ic_nurse_red);
        }

        CloseDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WhoCallerDialog.dismiss();
            }
        });

        getUserDataApi(getApplicationContext(),number);
    }


    public int dpToPx(float dp, Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }


    public static class BlurBuilder {

        private static final float BITMAP_SCALE = 0.15f;
        private static final float BLUR_RADIUS = 15f;


        public Bitmap blur(Context context, Bitmap image) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                int width = Math.round(image.getWidth() * BITMAP_SCALE);
                int height = Math.round(image.getHeight() * BITMAP_SCALE);

                Bitmap inputBitmap = Bitmap.createScaledBitmap(image, width, height, false);
                Bitmap outputBitmap = Bitmap.createBitmap(inputBitmap);

                RenderScript rs = RenderScript.create(context);

                ScriptIntrinsicBlur intrinsicBlur = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
                Allocation tmpIn = Allocation.createFromBitmap(rs, inputBitmap);
                Allocation tmpOut = Allocation.createFromBitmap(rs, outputBitmap);

                intrinsicBlur.setRadius(BLUR_RADIUS);
                intrinsicBlur.setInput(tmpIn);
                intrinsicBlur.forEach(tmpOut);
                tmpOut.copyTo(outputBitmap);

                return outputBitmap;
            }
            return null;
        }
    }

    public void getUserDataApi(Context context, String number) {

        if (CheckNetworkConnection.hasInternetConnection(context)) {

            //Check internet Access
            if (ConnectionDetector.hasInternetConnection(context)) {

                if(!ApiAccessToken.getAPIaccessToken(context).equals("NoAPIACCESS")){

                    BodyNumberModel bodyNumberModel = new BodyNumberModel(number);
                    Retrofit retrofit = retrofitHead.headOfGetorPostReturnRes();
                    WhoCallerApi whoCallerApi = retrofit.create(WhoCallerApi.class);
                    Call<FetchedUserData> userDataCall = whoCallerApi.fetchUserData(ApiAccessToken.getAPIaccessToken(context), bodyNumberModel);

                    CallerImageProgress.setVisibility(View.VISIBLE);
                    CallerProgress.setVisibility(View.VISIBLE);

                    userDataCall.enqueue(new Callback<FetchedUserData>() {
                        @Override
                        public void onResponse(Call<FetchedUserData> call, Response<FetchedUserData> response) {
                            if (response.isSuccessful())
                            {

                                FetchedUserData resp =response.body();
                                updateCard(resp);

                            }
                            else
                            {
                                Log.d("onFailure", "other error");
                                CallerImageProgress.setVisibility(View.GONE);
                                CallerProgress.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onFailure(Call<FetchedUserData> call, Throwable t) {
                            Log.d("onFailure", t.getMessage());
                            CallerImageProgress.setVisibility(View.GONE);
                            CallerProgress.setVisibility(View.GONE);
                        }
                    });

                }

            }
        }

    }

    public void updateCard(final FetchedUserData userData)
    {
        if (userData.isIs_spam())
        {
            ProfileBlurLayOut.setBackgroundColor(getResources().getColor(R.color.redColor));
        }

        if(userData.getName()!=null){
            CallerName.setText(userData.getName());
        }
        if(userData.getPhone()!=null){
            CallerNumber.setText(userData.getPhone());
        }
        if(userData.getCountry()!=null){
            CallerCountry.setText(userData.getCountry());
            CallerCountry.setVisibility(View.VISIBLE);
        }else {
            CallerCountry.setVisibility(View.GONE);
        }



        Picasso.with(getApplicationContext()).load("http://whocaller.net/uploads/"+userData.getUser_img())
                .into(CallerImage, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        CallerImageProgress.setVisibility(View.GONE);
                        CallerProgress.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {
                        if (userData.isIs_spam())
                        {
                            CallerImage.setImageResource(R.drawable.ic_nurse_red);
                        }
                        else
                        {
                            CallerImage.setImageResource(R.drawable.ic_nurse);
                        }
                        CallerImageProgress.setVisibility(View.GONE);
                        CallerProgress.setVisibility(View.GONE);
                    }
                });
        Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                // Bitmap is loaded, use image here
                BlurBuilder blurBuilder=new BlurBuilder();
                Bitmap resultBmp = blurBuilder.blur(getApplicationContext(), bitmap);
                ProfileBlurLayOut.setBackgroundDrawable(new BitmapDrawable(resultBmp));
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }
            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };
        Picasso.with(getApplicationContext()).load("http://whocaller.net/uploads/"+userData.getUser_img()).into(target);
    }

    public List<String> RetreiveAllNumberInBlockList(Context context) {

        List<String> BlockNumbersH;
        db = new Database_Helper(context);
        BlockNumbersH = db.getAllBlocklistNumbers();

        return BlockNumbersH;

    }

    void Insert_Number_To_Blocklist(String num,String name){

        if(num != null&& !num.isEmpty()){
            db.insertBlock(name,num,"personal");
        }

    }
}
