package teckvillage.developer.khaled_pc.teckvillagetrue;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
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
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.v4.app.ActivityCompat;
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

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.Task;
import com.intrusoft.squint.DiagonalView;
import com.sdsmdg.tastytoast.TastyToast;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;
import me.kaede.tagview.OnTagClickListener;
import me.kaede.tagview.OnTagDeleteListener;
import me.kaede.tagview.Tag;
import me.kaede.tagview.TagView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import teckvillage.developer.khaled_pc.teckvillagetrue.Model.database.Database_Helper;
import teckvillage.developer.khaled_pc.teckvillagetrue.Model.database.tables.Tags;
import teckvillage.developer.khaled_pc.teckvillagetrue.View.CheckNetworkConnection;
import teckvillage.developer.khaled_pc.teckvillagetrue.View.ConnectionDetector;
import teckvillage.developer.khaled_pc.teckvillagetrue.View.Signup;
import teckvillage.developer.khaled_pc.teckvillagetrue.View.SplashScreen;
import teckvillage.developer.khaled_pc.teckvillagetrue.Model.SharedPreference.getSharedPreferenceValue;
import teckvillage.developer.khaled_pc.teckvillagetrue.Model.retrofit.ApiAccessToken;
import teckvillage.developer.khaled_pc.teckvillagetrue.Model.SharedPreference.getSharedPreferenceValue;
import teckvillage.developer.khaled_pc.teckvillagetrue.Model.retrofit.WhoCallerApi;
import teckvillage.developer.khaled_pc.teckvillagetrue.Model.retrofit.retrofitHead;

import teckvillage.developer.khaled_pc.teckvillagetrue.Model.retrofit.JSON_Mapping.ResultModel_Update_User_data;
import teckvillage.developer.khaled_pc.teckvillagetrue.Model.retrofit.JSON_Mapping.Result_Update_User_Data;
import teckvillage.developer.khaled_pc.teckvillagetrue.Model.retrofit.WhoCallerApi;
import teckvillage.developer.khaled_pc.teckvillagetrue.Model.retrofit.retrofitHead;

import static android.media.MediaRecorder.VideoSource.CAMERA;
import static teckvillage.developer.khaled_pc.teckvillagetrue.View.Signup.encodeTobase64;

public class UserProfileActivity extends AppCompatActivity {
    //CONTACT
    RelativeLayout Phone_Edit_Layout,home_Edit_Layout,Email_Edit_Layout,Websie_Edit_Layout,TagEdit_Layout;

    //ABOUT
    RelativeLayout info_Edit_Layout,edit_usernameandimagerelative;
    LinearLayout info_Gender_Layout;
    Bitmap bitmap;
    TextView setphonenumber,address,email,website,shortnote,gender,usernameprofile,companytextview,titletextview,atcom;

    DiagonalView diagonalView;
    CircleImageView userPhoto,progressCircleImageView;
    String USer_Image,UserName,UserEmail,title,company;
    ImageView editbtnimgandusername;
    Button save;
    TagView tag;
    String UserProfImgInString;
    String genderinreq;
    ProgressBar progressBar;
    private static final int MY_CAMERA_REQUEST_CODE = 142;
    private static final int GALLERY = 0;
    boolean  ImageChangedaa=false;
    ProgressDialog mProgressDialog;
    boolean removeImage=false;
    private Random random;
    Database_Helper db;


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
        edit_usernameandimagerelative=findViewById(R.id.edit_usernameandimagerelative);
        companytextview=findViewById(R.id.companny_Profile);
        titletextview=findViewById(R.id.title_Profile);
        atcom=findViewById(R.id.atcom_Profile);

        //CHANGE UserName Or Image
        editbtnimgandusername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        //Edit Photo and UserName
        edit_usernameandimagerelative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
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

                ShowTagsDialog();
            }
        });

        website.setText(getSharedPreferenceValue.getUserWebsite(this));
        //set gender
        String puttedtextgender=getSharedPreferenceValue.getUserGender(this);
        if(puttedtextgender.equals("male")){
            gender.setText("Male");
        }else if(puttedtextgender.equals("female")){
            gender.setText("Female");
        }if(puttedtextgender.equals("other")){
            gender.setText("Prefer not to say");
        }else {
            gender.setText(puttedtextgender);
        }

        address.setText(getSharedPreferenceValue.getUseraddress(this));


        UserName=getSharedPreferenceValue.getUserName(this);
        usernameprofile.setText(UserName);

        //retrieve image
         USer_Image= getSharedPreferenceValue.getUserImage(this);

        //Check if User Not have Image
        if(USer_Image.equals("NoImageHere")){
            userPhoto.setImageDrawable(getResources().getDrawable(R.drawable.ic_nurse));
        }else {
            Log.w("lehh","lehh");
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


        //set company
        company=getSharedPreferenceValue.getCompany(this);
        title=getSharedPreferenceValue.getTitle(this);

        if(title.equals("Notext")&&company.equals("Notext")){
            companytextview.setVisibility(View.GONE);
            titletextview.setVisibility(View.GONE);
            atcom.setVisibility(View.GONE);
        }else if(title.equals("Notext")&&!company.equals("Notext")){
            titletextview.setVisibility(View.GONE);
            atcom.setVisibility(View.GONE);
            companytextview.setText(company);
        }else if(!title.equals("Notext")&&company.equals("Notext")){
            companytextview.setVisibility(View.GONE);
            atcom.setVisibility(View.GONE);
            titletextview.setText(title);
        }else {
            companytextview.setVisibility(View.VISIBLE);
            titletextview.setVisibility(View.VISIBLE);
            atcom.setVisibility(View.VISIBLE);
            companytextview.setText(company);
            titletextview.setText(title);
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



            /*if(!USer_Image.equals("NoImageHere")){
                BlurBuilder blurBuilder=new BlurBuilder();
                Bitmap bitmap =((BitmapDrawable)userPhoto.getDrawable()).getBitmap();
                Bitmap resultBmp = blurBuilder.blur(this,bitmap );//BitmapFactory.decodeResource(getResources(), R.drawable.tamimy)
                diagonalView.setImageBitmap(resultBmp);
            }*/



    }


    private void ShowTagsDialog() {

        final Dialog  DialogTagEdit = new Dialog(this);
        DialogTagEdit.requestWindowFeature(Window.FEATURE_NO_TITLE);
        DialogTagEdit.setContentView(R.layout.tags);
        Window window = DialogTagEdit.getWindow();
        window.setLayout(Toolbar.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.WRAP_CONTENT);
        DialogTagEdit.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TagView tagdialog;


        tagdialog=DialogTagEdit.findViewById(R.id.tagview);

        //SET LISTENER
        tagdialog.setOnTagClickListener(new OnTagClickListener() {

            @Override
            public void onTagClick(int position, Tag tag) {
                Toast.makeText(UserProfileActivity.this, "click tag id = " + tag.id + " position = " + position, Toast.LENGTH_SHORT).show();
            }
        });
        tagdialog.setOnTagDeleteListener(new OnTagDeleteListener() {

            @Override
            public void onTagDeleted(int position, Tag tag) {
                Toast.makeText(UserProfileActivity.this, "delete tag id = " + tag.id + " position =" + position, Toast.LENGTH_SHORT).show();
            }
        });

        db=new Database_Helper(this);
        String[] tagss= getResources().getStringArray(R.array.continents);
        tagdialog.addTags(tagss);


        //ADD TAG
        List<Tags> tags = db.getAllTagslist();

        //String[] colors = this.getResources().getStringArray(R.array.colors);
        for (int i = 0; i < tags.size(); i++) {
            Tag tag = new Tag(tags.get(i).getTagname());
            tag.id=tags.get(i).getId() ;
            Log.w("tagsid", String.valueOf(tags.get(i).getId()));
            tag.tagTextColor = Color.parseColor("#FFFFFF");
            tag.layoutColor = Color.parseColor("#0089c0");
            tag.radius = 20f;
            tag.tagTextSize = 14f;
            tag.layoutBorderSize = 1f;
            tag.isDeletable = true;
            tagdialog.addTag(tag);
        }



        DialogTagEdit.show();



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
        String streetText=Street.getText().toString().trim();
        String zipcodeTExt=","+ZibCode.getText().toString().trim();
        String CityText=" "+City.getText().toString().trim();
        String CountryText=Country.getText().toString().trim();


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

    Dialog MyDialoguserEdit=null;
    public void MyCustomHomeUserNameDialog() {



        MyDialoguserEdit = new Dialog(this);
        MyDialoguserEdit.requestWindowFeature(Window.FEATURE_NO_TITLE);
        MyDialoguserEdit.setContentView(R.layout.username_edit_dialog);
        Window window = MyDialoguserEdit.getWindow();
        window.setLayout(Toolbar.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.WRAP_CONTENT);
        MyDialoguserEdit.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        final EditText fisrtname,lastname,company,title;
        TextView Cancel,Ok;


        fisrtname=MyDialoguserEdit.findViewById(R.id.edit_firstname);
        lastname=MyDialoguserEdit.findViewById(R.id.edit_lastname);
        company=MyDialoguserEdit.findViewById(R.id.edit_company);
        title=MyDialoguserEdit.findViewById(R.id.edit_title);


        try {


        final String username=getSharedPreferenceValue.getUserName(UserProfileActivity.this);
        if(!username.equals("User_Name")){
            String[] tokens = username.split(" ");
            if(tokens.length!=2){throw new IllegalArgumentException();}
            String firstnm = tokens[0];
            String lastnm = tokens[1];
            fisrtname.setText(firstnm);
            lastname.setText(lastnm);
        }

        String Companyshared=getSharedPreferenceValue.getCompany(this);
        String TitleShared=getSharedPreferenceValue.getTitle(this);

        if(!Companyshared.equals("Notext")){
            company.setText(Companyshared);
        }

        if(!TitleShared.equals("Notext")){
            title.setText(TitleShared);
        }


        Cancel=MyDialoguserEdit.findViewById(R.id.btn_close);
        Ok=MyDialoguserEdit.findViewById(R.id.btn_submit);

        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialoguserEdit.dismiss();
            }
        });


        Ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String companytext=company.getText().toString().trim();
                final String titleText=title.getText().toString().trim();


                if(!companytext.isEmpty()){
                    companytextview.setText(companytext);
                    companytextview.setVisibility(View.VISIBLE);
                }else {
                    companytextview.setVisibility(View.GONE);
                }

                if(!titleText.isEmpty()){
                    titletextview.setText(titleText);
                    titletextview.setVisibility(View.VISIBLE);
                }else {
                    titletextview.setVisibility(View.GONE);
                }

                if(!companytext.isEmpty()&&!titleText.isEmpty()){
                    atcom.setVisibility(View.VISIBLE);
                }else {
                    atcom.setVisibility(View.GONE);
                }

                String fistname=fisrtname.getText().toString().trim();
                String lastnameTExt=lastname.getText().toString().trim();
                if(!fistname.isEmpty()&&!lastnameTExt.isEmpty()){

                    String fullnamediaaaaaa=fistname+" "+lastnameTExt;

                    if(fullnamediaaaaaa !=null){
                        usernameprofile.setText(fullnamediaaaaaa);
                    }
                }




                MyDialoguserEdit.dismiss();
            }
        });
        MyDialoguserEdit.show();


        }catch (Exception e){
            e.printStackTrace();
        }
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
                gender.setText("Prefer not to say");
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


        if(validation(usernameprofile.getText().toString(),email.getText().toString().trim())) {

            Log.w("asd", String.valueOf(ImageChangedaa));
            if(ImageChangedaa){

                requestUpdate();

            }else {

                requestUpdateWithoutProfilePic();
            }

        }
    }

    void requestUpdate(){
        File file = null;
        MultipartBody.Part fileToUpload = null;
        RequestBody mFile = null;
        RequestBody removeImg = null;
        try {


            Log.w("rf3","rf3");
            Drawable drawable = userPhoto.getDrawable();
            boolean hasImage = (drawable != null);

            if (hasImage && (drawable instanceof BitmapDrawable)) {

                bitmap = ((BitmapDrawable) userPhoto.getDrawable()).getBitmap();// bitmapDrawable.getBitmap();

                if (bitmap != null) {


                    file = loadImageFromStorage(saveToInternalStorage(bitmap));

                    Log.w("fileimg", file.getPath() + " || " + file.getName() + " || " + String.valueOf(file) +" || "+Integer.parseInt(String.valueOf(file.length()/1024)));

                    file=new File(Signup.resizeAndCompressImageBeforeSend(UserProfileActivity.this,file.getPath(),file.getName()));
                    mFile = RequestBody.create(MediaType.parse("image/*"), file);
                    fileToUpload = MultipartBody.Part.createFormData("img", file.getName(), mFile);

                    Log.w("fileimg2", file.getPath() + " || " + file.getName() + " || " + String.valueOf(file) +" || "+Integer.parseInt(String.valueOf(file.length()/1024)));
                }


            } else {
                fileToUpload = null;
                removeImage=true;
                removeImg=RequestBody.create(MediaType.parse("text/plain"), String.valueOf(removeImage));
            }




        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestBody companyRequest=RequestBody.create(MediaType.parse("text/plain"), "");
        RequestBody titleRequest=RequestBody.create(MediaType.parse("text/plain"), "");
        if(companytextview.getVisibility() == View.VISIBLE){
            companyRequest = RequestBody.create(MediaType.parse("text/plain"), companytextview.getText().toString());
        }

        if(titletextview.getVisibility() == View.VISIBLE){
            titleRequest = RequestBody.create(MediaType.parse("text/plain"), titletextview.getText().toString());
        }


        //Convert text to RequestBody
        RequestBody emailRequest = RequestBody.create(MediaType.parse("text/plain"), email.getText().toString());
        RequestBody fnameRequest = RequestBody.create(MediaType.parse("text/plain"), usernameprofile.getText().toString());
        RequestBody addressreq = RequestBody.create(MediaType.parse("text/plain"), address.getText().toString());

        if(gender.getText().toString().equals("Male")){
            genderinreq="male";
        }else if(gender.getText().toString().equals("Female")){
            genderinreq="female";
        }if(gender.getText().toString().equals("Prefer not to say")){
            genderinreq="other";
        }
        RequestBody websiterequest = RequestBody.create(MediaType.parse("text/plain"), website.getText().toString());
        RequestBody genderrebody = RequestBody.create(MediaType.parse("text/plain"), genderinreq);
        RequestBody aboutreq = RequestBody.create(MediaType.parse("text/plain"), shortnote.getText().toString());


        //Check wifi or data available
        if (CheckNetworkConnection.hasInternetConnection(UserProfileActivity.this)) {

            //Check internet Access
            if (ConnectionDetector.hasInternetConnection(UserProfileActivity.this)) {

                try {

                    mProgressDialog = new ProgressDialog(UserProfileActivity.this);
                    mProgressDialog.setIndeterminate(true);
                    mProgressDialog.setCancelable(false);
                    mProgressDialog.setMessage("Loading...");
                    mProgressDialog.show();

                    Retrofit retrofit = retrofitHead.headOfGetorPostReturnRes();
                    WhoCallerApi whoCallerApi = retrofit.create(WhoCallerApi.class);
                    Call<ResultModel_Update_User_data> updateUserdata = whoCallerApi.UptadeUserProfile(ApiAccessToken.getAPIaccessToken(UserProfileActivity.this), fnameRequest, emailRequest, fileToUpload,null, companyRequest, titleRequest, addressreq, websiterequest,genderrebody,aboutreq,removeImg);//,devicename,AndroidVersion

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

                                        Log.w("success", String.valueOf(user));

                                        /*
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
                                        }*/


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
                                        if (user.getGender() != null) {
                                            editor.putString("User_gender", user.getGender());
                                        }
                                        if (user.getTag_id() != null) {
                                            editor.putString("User_TagID", user.getTag_id());
                                        }
                                        if (user.getWebsite() != null) {
                                            editor.putString("User_Website", user.getWebsite());
                                        }
                                        if (user.getCompany() != null) {
                                            editor.putString("CompanyProfile", user.getCompany());
                                        }else {
                                            editor.putString("CompanyProfile", "Notext");
                                        }
                                        if (user.getTitle() != null) {
                                            editor.putString("TitleProfile", user.getTitle());
                                        }else {
                                            editor.putString("TitleProfile", "Notext");
                                        }
                                        if(user.getImg()!=null){
                                            editor.putString("User_img_profile","http://whocaller.net/whocallerAdmin/uploads/"+ user.getImg());
                                        }else {
                                            //user not upload img
                                            editor.putString("User_img_profile","NoImageHere");
                                        }
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


                }catch (Exception e){
                    e.printStackTrace();
                }

            } else {
                TastyToast.makeText(UserProfileActivity.this, "Internet not access Please connect to the internet", TastyToast.LENGTH_LONG, TastyToast.ERROR);
            }
        } else {
            TastyToast.makeText(UserProfileActivity.this, "You're offline. Please connect to the internet", TastyToast.LENGTH_LONG, TastyToast.ERROR);
        }
    }


    void requestUpdateWithoutProfilePic(){

        RequestBody companyRequest=RequestBody.create(MediaType.parse("text/plain"), "");
        RequestBody titleRequest=RequestBody.create(MediaType.parse("text/plain"), "");;
        if(companytextview.getVisibility() == View.VISIBLE){
            companyRequest = RequestBody.create(MediaType.parse("text/plain"), companytextview.getText().toString());
        }

        if(titletextview.getVisibility() == View.VISIBLE){
            titleRequest = RequestBody.create(MediaType.parse("text/plain"), titletextview.getText().toString());
        }


        //Convert text to RequestBody
        RequestBody emailRequest = RequestBody.create(MediaType.parse("text/plain"), email.getText().toString());
        RequestBody fnameRequest = RequestBody.create(MediaType.parse("text/plain"), usernameprofile.getText().toString());
        RequestBody addressreq = RequestBody.create(MediaType.parse("text/plain"), address.getText().toString());

        if(gender.getText().toString().equals("Male")){
            genderinreq="male";
        }else if(gender.getText().toString().equals("Female")){
            genderinreq="female";
        }if(gender.getText().toString().equals("Prefer not to say")){
            genderinreq="other";
        }
        RequestBody websiterequest = RequestBody.create(MediaType.parse("text/plain"), website.getText().toString());
        RequestBody genderrebody = RequestBody.create(MediaType.parse("text/plain"), genderinreq);
        RequestBody aboutreq = RequestBody.create(MediaType.parse("text/plain"), shortnote.getText().toString());


        //Check wifi or data available
        if (CheckNetworkConnection.hasInternetConnection(UserProfileActivity.this)) {

            //Check internet Access
            if (ConnectionDetector.hasInternetConnection(UserProfileActivity.this)) {

                try {

                    mProgressDialog = new ProgressDialog(UserProfileActivity.this);
                    mProgressDialog.setIndeterminate(true);
                    mProgressDialog.setCancelable(false);
                    mProgressDialog.setMessage("Loading...");
                    mProgressDialog.show();

                    Retrofit retrofit = retrofitHead.headOfGetorPostReturnRes();
                    WhoCallerApi whoCallerApi = retrofit.create(WhoCallerApi.class);
                    Call<ResultModel_Update_User_data> updateUserdata = whoCallerApi.UptadeUserProfileWithoutImage(ApiAccessToken.getAPIaccessToken(UserProfileActivity.this), fnameRequest, emailRequest,null, companyRequest, titleRequest, addressreq, websiterequest,genderrebody,aboutreq);//,devicename,AndroidVersion

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

                                        Log.w("success", String.valueOf(user));

                                        /*
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
                                        }*/


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
                                        if (user.getGender() != null) {
                                            editor.putString("User_gender", user.getGender());
                                        }
                                        if (user.getTag_id() != null) {
                                            editor.putString("User_TagID", user.getTag_id());
                                        }
                                        if (user.getWebsite() != null) {
                                            editor.putString("User_Website", user.getWebsite());
                                        }
                                        if (user.getCompany() != null) {
                                            editor.putString("CompanyProfile", user.getCompany());
                                        }else {
                                            editor.putString("CompanyProfile", "Notext");
                                        }
                                        if (user.getTitle() != null) {
                                            editor.putString("TitleProfile", user.getTitle());
                                        }else {
                                            editor.putString("TitleProfile", "Notext");
                                        }
                                        if(user.getImg()!=null){
                                            editor.putString("User_img_profile","http://whocaller.net/uploads/"+ user.getImg());
                                        }else {
                                            //user not upload img
                                            editor.putString("User_img_profile","NoImageHere");
                                        }
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


                }catch (Exception e){
                    e.printStackTrace();
                }

            } else {
                TastyToast.makeText(UserProfileActivity.this, "Internet not access Please connect to the internet", TastyToast.LENGTH_LONG, TastyToast.ERROR);
            }
        } else {
            TastyToast.makeText(UserProfileActivity.this, "You're offline. Please connect to the internet", TastyToast.LENGTH_LONG, TastyToast.ERROR);
        }
    }

    void EDIT_PhoneNumber_ALertDialog(){


        final AlertDialog dialog=   new AlertDialog.Builder(UserProfileActivity.this)
                .setTitle("Change Number")
                .setMessage("You will logout ,Badges and Data associated with this number will be lost. Tap Continue to change this phone number")

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
                .setIcon(getResources().getDrawable(R.drawable.ic_icon)).create();

        //2. now setup to change color of the button
        dialog.setOnShowListener( new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#ba160c"));
            }
        });

        dialog.show();




    }

    public File loadImageFromStorage(String path) {

        try {
            File f=new File(path, "profile.png");
            //Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            //ImageView img=(ImageView)findViewById(R.id.imgPicker);
            //img.setImageBitmap(b);
            return f;
        }
        catch (Exception e)
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
        File mypath=new File(directory,"profile.png");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 4, fos);
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


    private void showDialog(){
        try {
            AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
            pictureDialog.setTitle("Select Action");
            String[] pictureDialogItems = {
                    "Change Profile Picture",
                    "Edit User Name",
                    "Remove Profile Picture"};
            PackageManager pm = getPackageManager();

            pictureDialog.setItems(pictureDialogItems,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case 0:
                                    showPictureDialog();
                                    break;
                                case 1:
                                    MyCustomHomeUserNameDialog();
                                    break;
                                case 2:
                                    removeProfilePic();
                                    break;
                            }
                        }
                    });
            pictureDialog.show();

        }catch (Exception e) {
            Toast.makeText(this, "Camera Permission Error", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void removeProfilePic() {
        ImageChangedaa=true;
        userPhoto.setImageDrawable(getResources().getDrawable(R.drawable.ic_nurse));
        diagonalView.setImageDrawable(getResources().getDrawable(R.drawable.paper_bgd_1));
    }


    private void showPictureDialog(){
        try {
            AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
            pictureDialog.setTitle("Select Action");
            String[] pictureDialogItems = {
                    "Select Photo From Gallery",
                    "Capture Photo From Camera" };
            PackageManager pm = getPackageManager();
            int hasPerm = pm.checkPermission(Manifest.permission.CAMERA, getPackageName());
            if (hasPerm == PackageManager.PERMISSION_GRANTED) {
                pictureDialog.setItems(pictureDialogItems,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        choosePhotoFromGallary();
                                        break;
                                    case 1:
                                        takePhotoFromCamera();
                                        break;
                                }
                            }
                        });
                pictureDialog.show();
            } else
                ActivityCompat.requestPermissions(UserProfileActivity.this, new String[]{android.Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);
        } catch (Exception e) {
            Toast.makeText(this, "Camera Permission Error", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }


    public void choosePhotoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, GALLERY);
    }

    private void takePhotoFromCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == this.RESULT_CANCELED) {
            return;
        }

        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                    userPhoto.setImageBitmap(bitmap);
                    //show
                    BlurBuilder blurBuilder=new BlurBuilder();
                    Bitmap bitmap2 =((BitmapDrawable)userPhoto.getDrawable()).getBitmap();
                    Bitmap resultBmp = blurBuilder.blur(UserProfileActivity.this,bitmap2 );//BitmapFactory.decodeResource(getResources(), R.drawable.tamimy)
                    diagonalView.setImageBitmap(resultBmp);
                    ImageChangedaa=true;
                    Log.w("mofg2am4kda", String.valueOf(ImageChangedaa));

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(UserProfileActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == CAMERA) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            userPhoto.setImageBitmap(thumbnail);
            //show
            BlurBuilder blurBuilder=new BlurBuilder();
            Bitmap bitmap2 =((BitmapDrawable)userPhoto.getDrawable()).getBitmap();
            Bitmap resultBmp = blurBuilder.blur(UserProfileActivity.this,bitmap2 );//BitmapFactory.decodeResource(getResources(), R.drawable.tamimy)
            diagonalView.setImageBitmap(resultBmp);
            ImageChangedaa=true;
            Log.w("mofg2am4kdatanya", String.valueOf(ImageChangedaa));
            //saveImage(thumbnail);
            //Toast.makeText(Signup.this, "Image Saved!", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_CAMERA_REQUEST_CODE : {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                   showDialog();

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(getApplicationContext(), "Permission denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mProgressDialog != null){
            if (mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
        }

    }

}
