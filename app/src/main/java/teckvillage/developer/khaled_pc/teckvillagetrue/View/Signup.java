package teckvillage.developer.khaled_pc.teckvillagetrue.View;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.jaredrummler.android.device.DeviceName;
import com.sdsmdg.tastytoast.TastyToast;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Calendar;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import teckvillage.developer.khaled_pc.teckvillagetrue.MainActivity;
import teckvillage.developer.khaled_pc.teckvillagetrue.R;
import teckvillage.developer.khaled_pc.teckvillagetrue.model.retrofit.JSON_Mapping.DataReceived;
import teckvillage.developer.khaled_pc.teckvillagetrue.model.retrofit.JSON_Mapping.ResultModel;
import teckvillage.developer.khaled_pc.teckvillagetrue.model.retrofit.WhoCallerApi;
import teckvillage.developer.khaled_pc.teckvillagetrue.model.retrofit.retrofitHead;

import static android.media.MediaRecorder.VideoSource.CAMERA;

public class Signup extends AppCompatActivity {

    private static final int GALLERY = 0;
    private static final String IMAGE_DIRECTORY = "/WhoCaller";
    private static final int RC_SIGN_IN = 255;
    private static final String TAG = "signIn";
    ImageView add_personal_photo;
    TextView FirstName, LastName, Email;
    TextInputLayout FirstNameError, LastNameError, EmailError;
    TextView continue_btn;
    CallbackManager callbackManager;
    ImageView loginButton;
    GoogleSignInClient mGoogleSignInClient;
    private static final String EMAIL = "email";
    private static final String PROFILE_PIC = "public_profile";
    private static final String USER_FRIEND = "user_friends";
    private static final String USER_LINK = "user_link";
    static String LoginMethod = "normal";
    String facebookProfileLink="";
    String phNumber, Codecountry;
    // Device model
    String PhoneModel = android.os.Build.MODEL;

    // Android version
    String AndroidVersion = android.os.Build.VERSION.RELEASE;
    String phoneID;
    TelephonyManager tm;//Manger for getting phoneid
    SharedPreferences sharedPreferences;
    String devicename;
    boolean IsUserHasPhoto=false;
    String UserProfImgInString;
    Bitmap bitmap;
    String countryname;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_signup);


        getIntentDetails();
        tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE); // for getting phone id

        if ((int) Build.VERSION.SDK_INT < 23) {
            //this is a check for build version below 23
            phoneID = tm.getDeviceId();
        } else {
            //this is a check for build version above 23
            if (!checkIfAlreadyhavePermission()) {
                requestForSpecificPermission();
            }else {
                phoneID = tm.getDeviceId();
            }
        }

        devicename= DeviceName.getDeviceName();

        Log.w("showunent",phNumber+"  ||  "+Codecountry);
        Log.w("phone",PhoneModel+"  ||  "+AndroidVersion+"  ||  "+phoneID+"  ||  "+devicename);
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        // Build a GoogleSignInClient with the options specified by gso.
         mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        //init View
        add_personal_photo=findViewById(R.id.add_personal_image);
        FirstName=findViewById(R.id.first_name);
        LastName=findViewById(R.id.last_name);
        Email=findViewById(R.id.Email);
        FirstNameError=findViewById(R.id.input_layout_first_name);
        LastNameError=findViewById(R.id.input_layout_last_name);
        EmailError=findViewById(R.id.input_layout_Email);
        continue_btn=findViewById(R.id.btn_continue);
        // Set the dimensions of the sign-in button.
        ImageView signInButton = findViewById(R.id.btn_google);
        //signInButton.setSize(SignInButton.SIZE_STANDARD);

        //On click GOOGLE Login
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signupGoogle();
            }
        });


        //FACEBOOK
        loginButton =  findViewById(R.id.login_button);
        //*********************************************when click on facebook login ImageButtom(ClickListener)*****************************************************
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logInWithReadPermissions(Signup.this, Arrays.asList(EMAIL,PROFILE_PIC,USER_LINK));//USER_FRIEND removed
            }
        });

        //loginButton.setReadPermissions(Arrays.asList(EMAIL,PROFILE_PIC,USER_FRIEND));
        //LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));
        // If you are using in a fragment, call loginButton.setFragment(this);

        // Callback registration
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                setFacebookData(loginResult);
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Toast.makeText(getApplicationContext(), "Error to Login Facebook"+exception.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        //On click Continue btn
        continue_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.w("methodlogin",LoginMethod);
                String fistname=FirstName.getText().toString().trim();
                String lastname=LastName.getText().toString().trim();
                String email=Email.getText().toString().trim();
                //Bitmap image=((BitmapDrawable)add_personal_photo.getDrawable()).getBitmap();

                File file = null;
                MultipartBody.Part fileToUpload=null;
                RequestBody mFile=null;


                try {

                Drawable drawable = add_personal_photo.getDrawable();
                boolean hasImage = (drawable != null);

                if(hasImage && (drawable instanceof BitmapDrawable)){

                     bitmap =((BitmapDrawable)add_personal_photo.getDrawable()).getBitmap();// bitmapDrawable.getBitmap();

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

                //File file = null;
                if(validation(fistname,lastname,email)) {

                    //Convert text to RequestBody
                    RequestBody emailRequest = RequestBody.create(MediaType.parse("text/plain"), email);
                    RequestBody fnameRequest = RequestBody.create(MediaType.parse("text/plain"), fistname + " " + lastname);
                    RequestBody codecounRequest = RequestBody.create(MediaType.parse("text/plain"), Codecountry);
                    RequestBody phNumberRequest = RequestBody.create(MediaType.parse("text/plain"), phNumber);
                    RequestBody LoginMethodRequest = RequestBody.create(MediaType.parse("text/plain"), LoginMethod);
                    RequestBody facebookProfileLinkRequest = RequestBody.create(MediaType.parse("text/plain"), facebookProfileLink);
                    RequestBody devicenameRequest = RequestBody.create(MediaType.parse("text/plain"), devicename);
                    RequestBody AndroidVersioncounRequest = RequestBody.create(MediaType.parse("text/plain"), AndroidVersion);

                    //Check wifi or data available
                    if (CheckNetworkConnection.hasInternetConnection(Signup.this)) {

                        //Check internet Access
                        if (ConnectionDetector.hasInternetConnection(Signup.this)) {

                            final ProgressDialog mProgressDialog = new ProgressDialog(Signup.this);
                            mProgressDialog.setIndeterminate(true);
                            mProgressDialog.setMessage("Loading...");
                            mProgressDialog.show();

                            Retrofit retrofit = retrofitHead.headOfGetorPostReturnRes();
                            WhoCallerApi whoCallerApi = retrofit.create(WhoCallerApi.class);
                            Call<ResultModel> register_user = whoCallerApi.registeruser(codecounRequest, phNumberRequest, fnameRequest, emailRequest, fileToUpload, LoginMethodRequest, facebookProfileLinkRequest, devicenameRequest, AndroidVersioncounRequest);//,devicename,AndroidVersion

                            register_user.enqueue(new Callback<ResultModel>() {
                                @Override
                                public void onResponse(Call<ResultModel> call, Response<ResultModel> response) {
                                    try {
                                        if (mProgressDialog.isShowing())
                                            mProgressDialog.dismiss();

                                        if (response.isSuccessful()) {

                                            if (response.body()!=null) {

                                                // Do your success stuff...
                                                //Toast.makeText(getApplicationContext(), response.body().toString(), Toast.LENGTH_SHORT).show();
                                                DataReceived user = response.body().getUser();

                                                Log.w("success", user.getEmail());
                                                Log.w("success", user.getName());
                                                Log.w("success", user.getPhone());
                                                Log.w("success", user.getApi_token());

                                                //if respone retreive img name it mean user upload img
                                                if(user.getImg()!=null){
                                                    Log.w("success", user.getImg());
                                                    if(bitmap!=null) {
                                                        //convert img to string to save it
                                                        UserProfImgInString = encodeTobase64(bitmap);
                                                    }else {
                                                        //img found in server but not sent
                                                        UserProfImgInString="NoImageHere";
                                                    }
                                                }else {
                                                    //user not upload img
                                                    UserProfImgInString="NoImageHere";
                                                    Log.w("Noimguser", "noimg");
                                                }



                                                //retreive img and convert from string to bitmap to display it
                                                //add_personal_photo.setImageBitmap(decodeBase64(UserProfImgInString));

                                                //when Login Success
                                                SharedPreferences sharedPref = getSharedPreferences("WhoCaller?", MODE_PRIVATE);
                                                SharedPreferences.Editor editor = sharedPref.edit();
                                                editor.putBoolean("UserLogin", true);

                                                if(user.getApi_token()!=null) {
                                                    editor.putString("User_API_token", user.getApi_token());
                                                }
                                                if(user.getPhone()!=null) {
                                                    editor.putString("User_phone", user.getPhone());
                                                }
                                                if(user.getName()!=null){
                                                    editor.putString("User_name", user.getName());
                                                }
                                                if(user.getEmail()!=null){
                                                    editor.putString("User_email", user.getEmail());
                                                }
                                                if(user.getImg()!=null){
                                                    editor.putString("User_img_profile","http://whocaller.net/uploads/"+ user.getImg());
                                                }
                                                //editor.putString("User_img_profile", UserProfImgInString);
                                                editor.putString("UserCountry", countryname);
                                                editor.apply();


                                                //Close open MainActivity
                                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                                startActivity(intent);
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
                                public void onFailure(Call<ResultModel> call, Throwable t) {
                                    if (mProgressDialog.isShowing())
                                        mProgressDialog.dismiss();

                                    Log.w("onFailure", t.toString());
                                    Toast.makeText(getApplicationContext(), "Failure,Please try again", Toast.LENGTH_SHORT).show();
                                    //View parentLayout = findViewById(R.id.layoutsignuo);
                                    //Snackbar.make(parentLayout, "Failure,Please try again", Snackbar.LENGTH_LONG);
                                }
                            });

                        }else {
                            TastyToast.makeText(Signup.this, "Internet not access Please connect to the internet", TastyToast.LENGTH_LONG, TastyToast.ERROR);
                        }
                   }else {
                        TastyToast.makeText(Signup.this, "You're offline. Please connect to the internet", TastyToast.LENGTH_LONG, TastyToast.ERROR);
                    }

                }


            }
        });



        //On click Camera
        add_personal_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPictureDialog();
            }
        });

    }

    private void getIntentDetails() {
        phNumber=getIntent().getStringExtra("phonenumber");
        Codecountry=getIntent().getStringExtra("countrycode");
        countryname=getIntent().getStringExtra("contryname");
    }


    boolean validation(String fname,String Lanme,String email){
        if (TextUtils.isEmpty(fname)) {
            FirstNameError.setError("Empty Field");
            return false;
        }else {
            FirstNameError.setErrorEnabled(false);
        }
        if (TextUtils.isEmpty(Lanme)) {
            LastNameError.setError("Empty Field");
            return false;
        }else {
            LastNameError.setErrorEnabled(false);
        }
        if (TextUtils.isEmpty(email)) {
            EmailError.setError("Empty Field");
            return false;
        }else {
            EmailError.setErrorEnabled(false);
        }


      return  true;
    }


    private void showPictureDialog(){
        try {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select photo from gallery",
                "Capture photo from camera" };
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
            Toast.makeText(this, "Camera Permission error", Toast.LENGTH_SHORT).show();
    } catch (Exception e) {
        Toast.makeText(this, "Camera Permission error", Toast.LENGTH_SHORT).show();
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
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }

        if (resultCode == this.RESULT_CANCELED) {
            return;
        }

        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                    String path = saveImage(bitmap);
                    //Toast.makeText(Signup.this, "Image Saved!", Toast.LENGTH_SHORT).show();
                    add_personal_photo.setImageBitmap(bitmap);
                    add_personal_photo.setPadding(20,20,20,20);
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(Signup.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == CAMERA) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            add_personal_photo.setImageBitmap(thumbnail);
            add_personal_photo.setPadding(20,20,20,20);
            saveImage(thumbnail);
            //Toast.makeText(Signup.this, "Image Saved!", Toast.LENGTH_SHORT).show();
        }
    }


    public String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File wallpaperDirectory = new File(Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
        // have the object build the directory structure, if needed.
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
        }

        try {
            File f = new File(wallpaperDirectory, Calendar.getInstance().getTimeInMillis() + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(this,
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            
            Log.d("TAG", "File Saved::--->" + f.getAbsolutePath());

            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }

    //**********************************************************user data from login facebook***********************************************************************
    private void setFacebookData(final LoginResult loginResult)
    {
        GraphRequest request = GraphRequest.newMeRequest(
                loginResult.getAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        // Application code
                        try {
                            Log.i("Response",response.toString());

                            String firstName = response.getJSONObject().getString("first_name");
                            String lastName = response.getJSONObject().getString("last_name");
                            String email="";
                            if (response.getJSONObject().has("email")) {
                                email =response.getJSONObject().getString("email");
                            }

                            Profile profile = Profile.getCurrentProfile();
                            String id   = profile.getId();
                            String link = profile.getLinkUri().toString();

                            if (Profile.getCurrentProfile()!=null)
                            {
                                // imageView.setImageURI(p);
                                Log.i("Login", "ProfilePic" + Profile.getCurrentProfile().getProfilePictureUri(200, 200));
                                Uri l = Profile.getCurrentProfile().getProfilePictureUri(200, 200);
                                add_personal_photo.setImageURI(l);
                            }

                            URL imageURL = new URL("https://graph.facebook.com/" + id + "/picture?type=large");
                            Picasso.with(Signup.this).load(imageURL.toString()).into(add_personal_photo);
                            add_personal_photo.setPadding(20,20,20,20);

                            //Put Data in EditText
                            FirstName.setText(firstName);
                            LastName.setText(lastName);
                            Email.setText(email);

                            LoginMethod="facebook";

                            if(link!=null){
                                Log.w("facelike",link);
                                facebookProfileLink=link;
                            }

                            //Log.i("Link",link);
                            Log.i("Login" + "ID",  id);
                            Log.i("Login" + "Email",  email);
                            Log.i("Login"+ "FirstName", firstName);
                            Log.i("Login" + "LastName", lastName);



                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,email,first_name,last_name,gender,birthday,picture.type(large),link");
        request.setParameters(parameters);
        request.executeAsync();
    }


    void signupGoogle(){
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
        }
    }

    private void updateUI(GoogleSignInAccount account) {
        if(account!=null){
            Picasso.with(Signup.this).load(account.getPhotoUrl().toString()).into(add_personal_photo);
            add_personal_photo.setPadding(20,20,20,20);

            //Put Data in EditText
            FirstName.setText(account.getGivenName());
            LastName.setText(account.getFamilyName());
            Email.setText(account.getEmail());
            LoginMethod="google";
        }else {
            Toast.makeText(Signup.this, "Failed", Toast.LENGTH_SHORT).show();
        }

    }

    private boolean checkIfAlreadyhavePermission() {
        int result = ContextCompat.checkSelfPermission(Signup.this, android.Manifest.permission.READ_PHONE_STATE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestForSpecificPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE}, 109);
        }
    }

    //save image
    public static void imageDownload(Context ctx, String url){
        Picasso.with(ctx)
                .load("http://blog.concretesolutions.com.br/wp-content/uploads/2015/04/Android1.png")
                .into(getTarget(url));
    }

    //target to download img using picasso
    private static Target getTarget(final String url){
        Target target = new Target(){

            @Override
            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                new Thread(new Runnable() {

                    @Override
                    public void run() {

                        File file = new File(Environment.getExternalStorageDirectory().getPath() + "/" + url);
                        try {
                            file.createNewFile();
                            FileOutputStream ostream = new FileOutputStream(file);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, ostream);
                            ostream.flush();
                            ostream.close();
                        } catch (IOException e) {
                            Log.e("IOException", e.getLocalizedMessage());
                        }
                    }
                }).start();

            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };
        return target;
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

    // method for bitmap to base64
    public static String encodeTobase64(Bitmap image) {
        Bitmap immage = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immage.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

        Log.d("Image Log:", imageEncoded);
        return imageEncoded;
    }

    // method for base64 to bitmap
    public static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory
                .decodeByteArray(decodedByte, 0, decodedByte.length);
    }

}
