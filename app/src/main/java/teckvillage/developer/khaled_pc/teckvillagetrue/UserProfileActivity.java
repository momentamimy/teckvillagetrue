package teckvillage.developer.khaled_pc.teckvillagetrue;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.intrusoft.squint.DiagonalView;
import com.sdsmdg.tastytoast.TastyToast;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import me.kaede.tagview.TagView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import teckvillage.developer.khaled_pc.teckvillagetrue.View.CheckNetworkConnection;
import teckvillage.developer.khaled_pc.teckvillagetrue.View.ConnectionDetector;
import teckvillage.developer.khaled_pc.teckvillagetrue.View.SplashScreen;
import teckvillage.developer.khaled_pc.teckvillagetrue.Model.SharedPreference.getSharedPreferenceValue;
import teckvillage.developer.khaled_pc.teckvillagetrue.Model.retrofit.ApiAccessToken;

import teckvillage.developer.khaled_pc.teckvillagetrue.Model.retrofit.JSON_Mapping.ResultModel_Update_User_data;
import teckvillage.developer.khaled_pc.teckvillagetrue.Model.retrofit.JSON_Mapping.Result_Update_User_Data;
import teckvillage.developer.khaled_pc.teckvillagetrue.Model.retrofit.WhoCallerApi;
import teckvillage.developer.khaled_pc.teckvillagetrue.Model.retrofit.retrofitHead;

import static teckvillage.developer.khaled_pc.teckvillagetrue.View.Signup.encodeTobase64;

public class UserProfileActivity extends AppCompatActivity {
    //CONTACT
    RelativeLayout Phone_Edit_Layout,home_Edit_Layout,Email_Edit_Layout,Websie_Edit_Layout,TagEdit_Layout;

    //ABOUT
    RelativeLayout info_Edit_Layout;
    LinearLayout info_Gender_Layout;
    Bitmap bitmap;
    TextView setphonenumber,address,email,website,shortnote,gender,usernameprofile;

    DiagonalView diagonalView;
    CircleImageView userPhoto,progressCircleImageView;
    String USer_Image,UserName,UserEmail;
    ImageView editbtnimgandusername;
    Button save;
    TagView tag;
    String UserProfImgInString;
    String genderinreq;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);


        diagonalView=findViewById(R.id.diagonal);
        userPhoto=findViewById(R.id.UserPhoto);
        setphonenumber=findViewById(R.id.phone_number);
        email=findViewById(R.id.Email);
        editbtnimgandusername=findViewById(R.id.edit_usernameandimage);
        usernameprofile=findViewById(R.id.UserName_Profile);
        TagEdit_Layout=findViewById(R.id.tag_Edit_Layout);
        gender=findViewById(R.id.gender_label);
        save=findViewById(R.id.savechangebtn);
        tag=findViewById(R.id.tagviewprofile);
        shortnote=findViewById(R.id.info);
        website=findViewById(R.id.Websie);
        address=findViewById(R.id.home_address);
        progressBar=findViewById(R.id.progressupdate);
        progressCircleImageView=findViewById(R.id.UserPhotopicssovisableimage);

        //CHANGE UserName Or Image
        editbtnimgandusername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //Call API TO SAVE CHANGE
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CallAPI_SAVE_Change();

            }
        });


        //OPEN Dialog to change Tag
        TagEdit_Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        website.setText(getSharedPreferenceValue.getUserWebsite(this));
        gender.setText(getSharedPreferenceValue.getUserGender(this));
        address.setText(getSharedPreferenceValue.getUseraddress(this));


        UserName=getSharedPreferenceValue.getUserName(this);
        usernameprofile.setText(UserName);

        //retrieve image
         USer_Image= getSharedPreferenceValue.getUserImage(this);

        //Check if User Not have Image
        if(USer_Image.equals("NoImageHere")){
            userPhoto.setImageDrawable(getResources().getDrawable(R.drawable.ic_nurse));
        }else {
            progressBar.setVisibility(View.VISIBLE);
            Picasso.with(this)
                    .load(USer_Image)
                    .into(userPhoto, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                           progressBar.setVisibility(View.GONE);
                           progressCircleImageView.setVisibility(View.GONE);
                           //show
                            BlurBuilder blurBuilder=new BlurBuilder();
                            Bitmap bitmap =((BitmapDrawable)userPhoto.getDrawable()).getBitmap();
                            Bitmap resultBmp = blurBuilder.blur(UserProfileActivity.this,bitmap );//BitmapFactory.decodeResource(getResources(), R.drawable.tamimy)
                            diagonalView.setImageBitmap(resultBmp);
                        }

                        @Override
                        public void onError() {
                            userPhoto.setImageDrawable(getResources().getDrawable(R.drawable.ic_nurse));
                            progressBar.setVisibility(View.GONE);
                            progressCircleImageView.setVisibility(View.GONE);
                        }
                    });
           // userPhoto.setImageBitmap(decodeBase64(USer_Image));
        }


        //CONTACT
        Phone_Edit_Layout=findViewById(R.id.Phone_Edit_Layout);
        home_Edit_Layout=findViewById(R.id.home_Edit_Layout);
        Email_Edit_Layout=findViewById(R.id.Email_Edit_Layout);
        Websie_Edit_Layout=findViewById(R.id.Websie_Edit_Layout);

        //ABOUT
        info_Edit_Layout=findViewById(R.id.info_Edit_Layout);
        info_Gender_Layout=findViewById(R.id.info_Gender_Layout);

        String PhoneNumber=getSharedPreferenceValue.getUserPhoneNumber(this);
        //Check if Phone number not found
        if(PhoneNumber.equals("NoValueStored")){
            setphonenumber.setText("Phone Number");
        }else {
            setphonenumber.setText(PhoneNumber);
        }

        UserEmail=getSharedPreferenceValue.getUserEmail(this);
        //Check if Phone number not found
        if(UserEmail.equals("NoValueStored")){
            email.setText("Email@gmail.com");
        }else {
            email.setText(UserEmail);
        }

        //CONTACT
        Phone_Edit_Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EDIT_PhoneNumber_ALertDialog();
            }
        });


        home_Edit_Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyCustomHomeEditDialog();
            }
        });
        Email_Edit_Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyCustomEmailEditDialog();
            }
        });
        Websie_Edit_Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyCustomWebsiteEditDialog();
            }
        });


        //ABOUT
        info_Edit_Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyCustomShortTextEditDialog();
            }
        });

        info_Gender_Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyCustomGenderDialog();
            }
        });



            if(!USer_Image.equals("NoImageHere")){
                BlurBuilder blurBuilder=new BlurBuilder();
                Bitmap bitmap =((BitmapDrawable)userPhoto.getDrawable()).getBitmap();
                Bitmap resultBmp = blurBuilder.blur(this,bitmap );//BitmapFactory.decodeResource(getResources(), R.drawable.tamimy)
                diagonalView.setImageBitmap(resultBmp);
            }



    }

    public class BlurBuilder {

        private static final float BITMAP_SCALE = 0.19f;
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
    private Bitmap darkenBitMap(Bitmap bm) {

        Canvas canvas = new Canvas(bm);
        Paint p = new Paint(Color.RED);
        //ColorFilter filter = new LightingColorFilter(0xFFFFFFFF , 0x00222222); // lighten
        ColorFilter filter = new LightingColorFilter(0x327F7F7F, 0x00333333);    // darken
        p.setColorFilter(filter);
        canvas.drawBitmap(bm, new Matrix(), p);

        return bm;
    }

    Dialog MyDialogHomeEdit=null;
    public void MyCustomHomeEditDialog() {
        MyDialogHomeEdit = new Dialog(this);
        MyDialogHomeEdit.requestWindowFeature(Window.FEATURE_NO_TITLE);
        MyDialogHomeEdit.setContentView(R.layout.home_edit_dialog);
        Window window = MyDialogHomeEdit.getWindow();
        window.setLayout(Toolbar.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.WRAP_CONTENT);
        MyDialogHomeEdit.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        final EditText Street,ZibCode,City,Country;
        TextView Cancel,Ok;

        Street=MyDialogHomeEdit.findViewById(R.id.edit_Street);
        ZibCode=MyDialogHomeEdit.findViewById(R.id.edit_ZIB);
        City=MyDialogHomeEdit.findViewById(R.id.edit_City);
        Country=MyDialogHomeEdit.findViewById(R.id.edit_country);

        Country.setText(getSharedPreferenceValue.getUserCountry(UserProfileActivity.this));

        Cancel=MyDialogHomeEdit.findViewById(R.id.btn_close);
        Ok=MyDialogHomeEdit.findViewById(R.id.btn_submit);

        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialogHomeEdit.dismiss();
            }
        });

        Ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                address.setText(Street.getText().toString().trim()+","+ZibCode.getText().toString().trim()+" "+City.getText().toString().trim());
                MyDialogHomeEdit.dismiss();
            }
        });
        MyDialogHomeEdit.show();
    }


    Dialog MyDialogEmailEdit=null;
    public void MyCustomEmailEditDialog() {
        MyDialogEmailEdit = new Dialog(this);
        MyDialogEmailEdit.requestWindowFeature(Window.FEATURE_NO_TITLE);
        MyDialogEmailEdit.setContentView(R.layout.email_edit_dialog);
        Window window = MyDialogEmailEdit.getWindow();
        window.setLayout(Toolbar.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.WRAP_CONTENT);
        MyDialogEmailEdit.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        final EditText Email;
        TextView Cancel,Ok;

        Email=MyDialogEmailEdit.findViewById(R.id.edit_Email);

        Cancel=MyDialogEmailEdit.findViewById(R.id.btn_close);
        Ok=MyDialogEmailEdit.findViewById(R.id.btn_submit);

        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialogEmailEdit.dismiss();
            }
        });

        Ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email.setText(Email.getText().toString().trim());
                MyDialogEmailEdit.dismiss();
            }
        });
        MyDialogEmailEdit.show();
    }


    Dialog MyDialogWebsiteEdit=null;
    public void MyCustomWebsiteEditDialog() {
        MyDialogWebsiteEdit= new Dialog(this);
        MyDialogWebsiteEdit.requestWindowFeature(Window.FEATURE_NO_TITLE);
        MyDialogWebsiteEdit.setContentView(R.layout.website_edit_dialog);
        Window window = MyDialogWebsiteEdit.getWindow();
        window.setLayout(Toolbar.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.WRAP_CONTENT);
        MyDialogWebsiteEdit.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        final EditText Website;
        TextView Cancel,Ok;

        Website=MyDialogWebsiteEdit.findViewById(R.id.edit_Websie);

        Cancel=MyDialogWebsiteEdit.findViewById(R.id.btn_close);
        Ok=MyDialogWebsiteEdit.findViewById(R.id.btn_submit);

        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialogWebsiteEdit.dismiss();
            }
        });

        Ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                website.setText(Website.getText().toString().trim());
                MyDialogWebsiteEdit.dismiss();
            }
        });
        MyDialogWebsiteEdit.show();
    }


    Dialog MyDialogShortTextEdit=null;
    public void MyCustomShortTextEditDialog() {
        MyDialogShortTextEdit= new Dialog(this);
        MyDialogShortTextEdit.requestWindowFeature(Window.FEATURE_NO_TITLE);
        MyDialogShortTextEdit.setContentView(R.layout.short_text_edit_dialog);
        Window window = MyDialogShortTextEdit.getWindow();
        window.setLayout(Toolbar.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.WRAP_CONTENT);
        MyDialogShortTextEdit.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        final EditText ShortText;
        final TextView NumChar;
        TextView Cancel,Ok;

        ShortText=MyDialogShortTextEdit.findViewById(R.id.edit_short_text);
        NumChar=MyDialogShortTextEdit.findViewById(R.id.Num_Char);

        ShortText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int i=160-ShortText.getText().length();
                NumChar.setText(i+" characters remaining");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        Cancel=MyDialogShortTextEdit.findViewById(R.id.btn_close);
        Ok=MyDialogShortTextEdit.findViewById(R.id.btn_submit);

        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialogShortTextEdit.dismiss();
            }
        });

        Ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shortnote.setText(ShortText.getText().toString().trim());
                MyDialogShortTextEdit.dismiss();
            }
        });
        MyDialogShortTextEdit.show();
    }


    Dialog MyDialogGender=null;
    public void MyCustomGenderDialog() {
        MyDialogGender= new Dialog(this);
        MyDialogGender.requestWindowFeature(Window.FEATURE_NO_TITLE);
        MyDialogGender.setContentView(R.layout.gender_dialog);
        Window window = MyDialogGender.getWindow();
        window.setLayout(Toolbar.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.WRAP_CONTENT);
        MyDialogGender.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView Prefer_not_to_say,Male,Female;

        Prefer_not_to_say=MyDialogGender.findViewById(R.id.Prefer_not_to_say);
        Male=MyDialogGender.findViewById(R.id.Male);
        Female=MyDialogGender.findViewById(R.id.Female);

        Prefer_not_to_say.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gender.setText("Prefer_not_to_say");
                MyDialogGender.dismiss();
            }
        });
        Male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gender.setText("Male");
                MyDialogGender.dismiss();
            }
        });
        Female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gender.setText("Female");
                MyDialogGender.dismiss();
            }
        });

        MyDialogGender.show();
    }

    void CallAPI_SAVE_Change() {

        File file = null;
        MultipartBody.Part fileToUpload=null;
        RequestBody mFile=null;


        try {

            Drawable drawable = userPhoto.getDrawable();
            boolean hasImage = (drawable != null);

            if(hasImage && (drawable instanceof BitmapDrawable)){

                bitmap =((BitmapDrawable)userPhoto.getDrawable()).getBitmap();// bitmapDrawable.getBitmap();

                if(bitmap!=null) {


                    file= loadImageFromStorage( saveToInternalStorage(bitmap));
                    mFile = RequestBody.create(MediaType.parse("image/*"), file);
                    fileToUpload = MultipartBody.Part.createFormData("img", file.getName(), mFile);

                    Log.w("fileimg",file.getPath()+" || "+file.getName()+" || "+String.valueOf(file));
                }


            }else {
                fileToUpload=null;
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        if(validation(usernameprofile.getText().toString(),email.getText().toString().trim())) {


            //Convert text to RequestBody
            RequestBody emailRequest = RequestBody.create(MediaType.parse("text/plain"), email.getText().toString());
            RequestBody fnameRequest = RequestBody.create(MediaType.parse("text/plain"), usernameprofile.getText().toString());
            RequestBody addressreq = RequestBody.create(MediaType.parse("text/plain"), address.getText().toString());

            if(gender.getText().toString().equals("Male")){
                 genderinreq="male";
            }else if(gender.getText().toString().equals("Female")){
                genderinreq="female";
            }if(gender.getText().toString().equals("Prefer_not_to_say")){
                genderinreq="other";
            }
            RequestBody websiterequest = RequestBody.create(MediaType.parse("text/plain"), website.getText().toString());
            RequestBody genderrebody = RequestBody.create(MediaType.parse("text/plain"), genderinreq);
            RequestBody aboutreq = RequestBody.create(MediaType.parse("text/plain"), shortnote.getText().toString());


            //Check wifi or data available
            if (CheckNetworkConnection.hasInternetConnection(UserProfileActivity.this)) {

                //Check internet Access
                if (ConnectionDetector.hasInternetConnection(UserProfileActivity.this)) {


                    final ProgressDialog mProgressDialog = new ProgressDialog(UserProfileActivity.this);
                    mProgressDialog.setIndeterminate(true);
                    mProgressDialog.setMessage("Loading...");
                    mProgressDialog.show();

                    Retrofit retrofit = retrofitHead.headOfGetorPostReturnRes();
                    WhoCallerApi whoCallerApi = retrofit.create(WhoCallerApi.class);
                    Call<ResultModel_Update_User_data> updateUserdata = whoCallerApi.UptadeUserProfile(ApiAccessToken.getAPIaccessToken(UserProfileActivity.this), fnameRequest, emailRequest, fileToUpload,null, null, null, addressreq, websiterequest,genderrebody,aboutreq);//,devicename,AndroidVersion

                    updateUserdata.enqueue(new Callback<ResultModel_Update_User_data>() {
                        @Override
                        public void onResponse(Call<ResultModel_Update_User_data> call, Response<ResultModel_Update_User_data> response) {
                            try {
                                if (mProgressDialog.isShowing())
                                    mProgressDialog.dismiss();

                                if (response.isSuccessful()) {

                                    if (response.body() != null) {

                                        // Do your success stuff...
                                        //Toast.makeText(getApplicationContext(), response.body().toString(), Toast.LENGTH_SHORT).show();
                                        Result_Update_User_Data user = response.body().getUser();

                                        Log.w("success", user.getEmail());
                                        Log.w("success", user.getName());
                                        Log.w("success", user.getPhone());

                                        //if respone retreive img name it mean user upload img
                                        if (user.getImg() != null) {
                                            Log.w("success", user.getImg());
                                            if (bitmap != null) {
                                                //convert img to string to save it
                                                UserProfImgInString = encodeTobase64(bitmap);
                                            } else {
                                                //img found in server but not sent
                                                UserProfImgInString = "NoImageHere";
                                            }
                                        } else {
                                            //user not upload img
                                            UserProfImgInString = "NoImageHere";
                                            Log.w("Noimguser", "noimg");
                                        }


                                        //when Login Success
                                        SharedPreferences sharedPref = getSharedPreferences("WhoCaller?", MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPref.edit();


                                        if (user.getPhone() != null) {
                                            editor.putString("User_phone", user.getPhone());
                                        }
                                        if (user.getName() != null) {
                                            editor.putString("User_name", user.getName());
                                        }
                                        if (user.getEmail() != null) {
                                            editor.putString("User_email", user.getEmail());
                                        }
                                        if (user.getAbout() != null) {
                                            editor.putString("User_ShortNote", user.getAbout());
                                        }
                                        if (user.getAddress() != null) {
                                            editor.putString("User_address", user.getAddress());
                                        }
                                        if (user.getCompany() != null) {
                                            editor.putString("User_company", user.getCompany());
                                        }
                                        if (user.getGender() != null) {
                                            editor.putString("User_gender", user.getGender());
                                        }
                                        if (user.getTag_id() != null) {
                                            editor.putString("User_TagID", user.getTag_id());
                                        }
                                        if (user.getWebsite() != null) {
                                            editor.putString("User_Website", user.getWebsite());
                                        }
                                        if(user.getImg()!=null){
                                            editor.putString("User_img_profile","http://whocaller.net/uploads/"+ user.getImg());
                                        }else {
                                            //user not upload img
                                            editor.putString("User_img_profile","NoImageHere");
                                        }
                                        //editor.putString("User_img_profile", UserProfImgInString);
                                        editor.apply();


                                        //Close open MainActivity
                                        finish();


                                    } else {
                                        Toast.makeText(getApplicationContext(), "Failure,Please try again", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(getApplicationContext(), "Failure,Please try again", Toast.LENGTH_SHORT).show();
                                }


                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResultModel_Update_User_data> call, Throwable t) {
                            if (mProgressDialog.isShowing())
                                mProgressDialog.dismiss();

                            Log.w("onFailure", t.toString());
                            Toast.makeText(getApplicationContext(), "Failure,Please try again", Toast.LENGTH_SHORT).show();
                            //View parentLayout = findViewById(R.id.layoutsignuo);
                            //Snackbar.make(parentLayout, "Failure,Please try again", Snackbar.LENGTH_LONG);
                        }
                    });


                } else {
                    TastyToast.makeText(UserProfileActivity.this, "Internet not access Please connect to the internet", TastyToast.LENGTH_LONG, TastyToast.ERROR);
                }
            } else {
                TastyToast.makeText(UserProfileActivity.this, "You're offline. Please connect to the internet", TastyToast.LENGTH_LONG, TastyToast.ERROR);
            }
        }
    }


    void EDIT_PhoneNumber_ALertDialog(){

        new AlertDialog.Builder(UserProfileActivity.this)
                .setTitle("Change number")
                .setMessage("Badges and Data associated with this number will be lost. Tap Continue to change this phone number")

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton("CONTINUE", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Continue with delete operation
                        //remove share preferences
                        SharedPreferences preferences = getSharedPreferences("WhoCaller?", MODE_PRIVATE);
                        preferences.edit().clear().apply();
                        //start splash
                        Intent intent=new Intent(UserProfileActivity.this,SplashScreen.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();

                    }
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Cancel
                        dialog.dismiss();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    public File loadImageFromStorage(String path) {

        try {
            File f=new File(path, "profile.jpg");
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            //ImageView img=(ImageView)findViewById(R.id.imgPicker);
            //img.setImageBitmap(b);
            return f;
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public String saveToInternalStorage(Bitmap bitmapImage){
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath=new File(directory,"profile.jpg");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }

    boolean validation(String fname, String email){
        if (TextUtils.isEmpty(fname)) {
            Toast.makeText(UserProfileActivity.this,"Name Field Empty",Toast.LENGTH_LONG).show();
            return false;
        }
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(UserProfileActivity.this,"Email Field Empty",Toast.LENGTH_LONG).show();
            return false;
        }else if( !Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(UserProfileActivity.this,"Email not valid",Toast.LENGTH_LONG).show();
            return false;
        }


        return  true;
    }



}
