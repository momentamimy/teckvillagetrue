package com.developer.whocaller.net.View;

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
import android.support.v4.app.ActivityCompat;
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

import com.developer.whocaller.net.Model.retrofit.ApiAccessToken;
import com.developer.whocaller.net.Model.retrofit.JSON_Mapping.DataReceived;
import com.developer.whocaller.net.Model.retrofit.JSON_Mapping.TokenBodyModel;
import com.developer.whocaller.net.Model.retrofit.JSON_Mapping.TokenDataReceived;
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
import com.google.firebase.iid.FirebaseInstanceId;
import com.jaredrummler.android.device.DeviceName;
import com.sdsmdg.tastytoast.TastyToast;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
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
import com.developer.whocaller.net.MainActivity;
import com.developer.whocaller.net.R;

import com.developer.whocaller.net.Model.retrofit.JSON_Mapping.ResultModel;
import com.developer.whocaller.net.Model.retrofit.WhoCallerApi;
import com.developer.whocaller.net.Model.retrofit.retrofitHead;

import static android.media.MediaRecorder.VideoSource.CAMERA;

public class Signup extends AppCompatActivity {

    private static final int GALLERY = 0;
    private static final String IMAGE_DIRECTORY = "/WhoCaller";
    private static final int RC_SIGN_IN = 255;
    private static final String TAG = "signIn";
    private static final int MY_CAMERA_REQUEST_CODE = 143;
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

        try {

            getIntentDetails();
            tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE); // for getting phone id

            if ((int) Build.VERSION.SDK_INT < 23) {
                //this is a check for build version below 23
                phoneID = tm.getDeviceId();
            } else {
                //this is a check for build version above 23
                if (!checkIfAlreadyhavePermission()) {
                    requestForSpecificPermission();
                } else {
                    phoneID = tm.getDeviceId();
                }
            }

            devicename = DeviceName.getDeviceName();

            Log.w("showunent", phNumber + "  ||  " + Codecountry);
            Log.w("phone", PhoneModel + "  ||  " + AndroidVersion + "  ||  " + phoneID + "  ||  " + devicename);
            // Configure sign-in to request the user's ID, email address, and basic
            // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
            // Build a GoogleSignInClient with the options specified by gso.
            mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

            //init View
            add_personal_photo = findViewById(R.id.add_personal_image);
            FirstName = findViewById(R.id.first_name);
            LastName = findViewById(R.id.last_name);
            Email = findViewById(R.id.Email);
            FirstNameError = findViewById(R.id.input_layout_first_name);
            LastNameError = findViewById(R.id.input_layout_last_name);
            EmailError = findViewById(R.id.input_layout_Email);
            continue_btn = findViewById(R.id.btn_continue);
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
            loginButton = findViewById(R.id.login_button);
            //*********************************************when click on facebook login ImageButtom(ClickListener)*****************************************************
            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LoginManager.getInstance().logInWithReadPermissions(Signup.this, Arrays.asList(EMAIL, PROFILE_PIC, USER_LINK));//USER_FRIEND removed
                }
            });

            //loginButton.setReadPermissions(Arrays.asList(EMAIL,PROFILE_PIC,USER_FRIEND));
            //LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));
            //If you are using in a fragment, call loginButton.setFragment(this);

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
                    Toast.makeText(getApplicationContext(), "Error to Login Facebook" + exception.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });


            //On click Continue btn
            continue_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String fistname = FirstName.getText().toString().trim();
                    String lastname = LastName.getText().toString().trim();
                    String email = Email.getText().toString().trim();

                    File file = null;
                    MultipartBody.Part fileToUpload = null;
                    RequestBody mFile = null;

                    try {

                        Drawable drawable = add_personal_photo.getDrawable();
                        boolean hasImage = (drawable != null);

                        if (hasImage && (drawable instanceof BitmapDrawable)) {

                            bitmap = ((BitmapDrawable) add_personal_photo.getDrawable()).getBitmap();// bitmapDrawable.getBitmap();

                            if (bitmap != null) {


                                file = loadImageFromStorage(saveToInternalStorage(bitmap));
                                //resizeAndCompressImageBeforeSend(Signup.this,file.getPath(),file.getName());
                                //file =new File(saveImage(bitmap));
                                file=new File(Signup.resizeAndCompressImageBeforeSend(Signup.this,file.getPath(),file.getName()));
                                mFile = RequestBody.create(MediaType.parse("image/*"), file);
                                fileToUpload = MultipartBody.Part.createFormData("img", file.getName(), mFile);

                                Log.w("fileimg", file.getPath() + " || " + file.getName() + " || " + String.valueOf(file));
                            }


                        } else {
                            fileToUpload = null;
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    //File file = null;
                    if (validation(fistname, lastname, email)) {

                        //Convert text to RequestBody
                        RequestBody emailRequest = RequestBody.create(MediaType.parse("text/plain"), email);
                        RequestBody countryRequest = RequestBody.create(MediaType.parse("text/plain"), countryname);
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
                                mProgressDialog.setCancelable(false);
                                mProgressDialog.show();

                                Retrofit retrofit = retrofitHead.headOfGetorPostReturnRes();
                                WhoCallerApi whoCallerApi = retrofit.create(WhoCallerApi.class);
                                Call<ResultModel> register_user = whoCallerApi.registeruser(codecounRequest, phNumberRequest, fnameRequest, emailRequest,countryRequest, fileToUpload, LoginMethodRequest, facebookProfileLinkRequest, devicenameRequest, AndroidVersioncounRequest);//,devicename,AndroidVersion

                                register_user.enqueue(new Callback<ResultModel>() {
                                    @Override
                                    public void onResponse(Call<ResultModel> call, Response<ResultModel> response) {
                                        try {
                                            if (mProgressDialog.isShowing())
                                                mProgressDialog.dismiss();

                                            if (response.isSuccessful()) {

                                                if (response.body() != null) {

                                                    // Do your success stuff...
                                                    //Toast.makeText(getApplicationContext(), response.body().toString(), Toast.LENGTH_SHORT).show();
                                                    DataReceived user = response.body().getUser();

                                                   /* Log.w("success", user.getEmail());
                                                    Log.w("success", user.getName());
                                                    Log.w("success", user.getPhone());
                                                    Log.w("success", user.getApi_token());*/

                                                    //if respone retreive img name it mean user upload img
                                                    /*if (user.getImg() != null) {
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

                                                    //retreive img and convert from string to bitmap to display it
                                                    //add_personal_photo.setImageBitmap(decodeBase64(UserProfImgInString));

                                                    //when Login Success
                                                    SharedPreferences sharedPref = getSharedPreferences("WhoCaller?", MODE_PRIVATE);
                                                    SharedPreferences.Editor editor = sharedPref.edit();
                                                    editor.putBoolean("UserLogin", true);

                                                    if (user.getId() != 0) {
                                                        editor.putInt("User_ID", user.getId());
                                                    }
                                                    if (user.getApi_token() != null) {
                                                        editor.putString("User_API_token", user.getApi_token());
                                                    }
                                                    if (user.getPhone() != null) {
                                                        editor.putString("User_phone", user.getPhone());
                                                    }
                                                    if (user.getName() != null) {
                                                        editor.putString("User_name", user.getName());
                                                    }
                                                    if (user.getEmail() != null) {
                                                        editor.putString("User_email", user.getEmail());
                                                    }
                                                    if (user.getImg() != null) {
                                                        editor.putString("User_img_profile", "http://whocaller.net/whocallerAdmin/uploads/" + user.getImg());
                                                    }
                                                    if (user.getCountry() != null) {
                                                        editor.putString("UserCountry", user.getCountry());
                                                    }
                                                    editor.apply();

                                                    //uploadFirebaseToken
                                                    uploadFirebaseToken();

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
                                    }
                                });

                            } else {
                                TastyToast.makeText(Signup.this, "Internet not access Please connect to the internet", TastyToast.LENGTH_LONG, TastyToast.ERROR);
                            }
                        } else {
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

        }catch (Exception e){
            e.printStackTrace();
        }
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
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            EmailError.setError("Enter Correct Email address");
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
            ActivityCompat.requestPermissions(Signup.this, new String[]{android.Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);
            //Toast.makeText(this, "Camera Permission Error", Toast.LENGTH_SHORT).show();
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
                    //String path = saveImage(bitmap);
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
            //saveImage(thumbnail);
            //Toast.makeText(Signup.this, "Image Saved!", Toast.LENGTH_SHORT).show();
        }
    }


    public String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
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

    //*******************user data from login facebook************************************
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

                            String firstName = "";
                            if(response.getJSONObject().getString("first_name")!=null) {
                                firstName = response.getJSONObject().getString("first_name");
                            }

                            String lastName = "";
                            if(response.getJSONObject().getString("last_name")!=null){
                                lastName = response.getJSONObject().getString("last_name");
                            }

                            String email="";
                            if (response.getJSONObject().has("email")) {
                                email =response.getJSONObject().getString("email");
                            }

                            String idfacebook="";
                            if(response.getJSONObject().has("id")){
                                idfacebook=response.getJSONObject().getString("id");
                                Log.w("idfacebook",idfacebook);

                                //Set Photo if ID Found
                                URL imageURL = new URL("https://graph.facebook.com/" + idfacebook + "/picture?type=large");
                                Picasso.with(Signup.this).load(imageURL.toString()).into(add_personal_photo);
                                add_personal_photo.setPadding(20,20,20,20);
                            }

                            String linkpars="";
                            if (response.getJSONObject().has("link")) {
                                linkpars =response.getJSONObject().getString("link");
                                Log.w("linkfaceee",linkpars);
                            }


                            Profile profile = null;
                            String id   = null;
                            String link = null;
                            if(Profile.getCurrentProfile()!=null){
                                profile = Profile.getCurrentProfile();

                                if( profile.getId()!=null){
                                    id= profile.getId();
                                }

                                if(profile.getLinkUri().toString()!=null){
                                    link = profile.getLinkUri().toString();
                                }

                            }


                            /*if (Profile.getCurrentProfile()!=null)
                            {
                                Log.w("aspic","aapic");
                                // imageView.setImageURI(p);
                                Log.w("Login", "ProfilePic" + Profile.getCurrentProfile().getProfilePictureUri(200, 200));
                                Uri l = Profile.getCurrentProfile().getProfilePictureUri(200, 200);
                                add_personal_photo.setImageURI(l);
                            }*/


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
                            // Log.i("Login" + "ID",  id);
                            Log.i("Login" + "Email",  email);
                            Log.i("Login"+ "FirstName", firstName);
                            Log.i("Login" + "LastName", lastName);

                            //Logout after get Result
                            LoginManager.getInstance().logOut();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,email,first_name,last_name,link,gender,birthday,picture.type(large)");
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
            if(account.getPhotoUrl()!=null){
                Picasso.with(Signup.this).load(account.getPhotoUrl()).into(add_personal_photo);
                add_personal_photo.setPadding(20,20,20,20);
            }

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
        File directory = cw.getDir("imageDire", Context.MODE_PRIVATE);
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

    public void uploadFirebaseToken() {
        if (CheckNetworkConnection.hasInternetConnection(Signup.this)) {

            //Check internet Access
            if (ConnectionDetector.hasInternetConnection(Signup.this)) {
                Retrofit retrofit = retrofitHead.headOfGetorPostReturnRes();
                WhoCallerApi whoCallerApi = retrofit.create(WhoCallerApi.class);
                Call<TokenDataReceived> tokenDataReceivedCall = whoCallerApi.uploadFirbaseToken("application/json", ApiAccessToken.getAPIaccessToken(Signup.this), new TokenBodyModel(FirebaseInstanceId.getInstance().getToken()));

                tokenDataReceivedCall.enqueue(new Callback<TokenDataReceived>() {
                    @Override
                    public void onResponse(Call<TokenDataReceived> call, Response<TokenDataReceived> response) {
                        if (response.isSuccessful())
                        {
                            Log.d("firebaseCodeSent",response.body().getMsg());
                        }
                    }

                    @Override
                    public void onFailure(Call<TokenDataReceived> call, Throwable t) {

                    }
                });
            }
        }
    }

    public static String resizeAndCompressImageBeforeSend(Context context,String filePath,String fileName){
        final int MAX_IMAGE_SIZE = 700 * 1024; // max final file size in kilobytes

        // First decode with inJustDecodeBounds=true to check dimensions of image
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath,options);

        // Calculate inSampleSize(First we are going to resize the image to 800x800 image, in order to not have a big but very low quality image.
        //resizing the image will already reduce the file size, but after resizing we will check the file size and start to compress image
        options.inSampleSize = calculateInSampleSize(options, 800, 800);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        options.inPreferredConfig= Bitmap.Config.ARGB_8888;

        Bitmap bmpPic = BitmapFactory.decodeFile(filePath,options);


        int compressQuality = 100; // quality decreasing by 5 every loop.
        int streamLength;
        do{
            ByteArrayOutputStream bmpStream = new ByteArrayOutputStream();
            Log.d("compressBitmap", "Quality: " + compressQuality);
            bmpPic.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream);
            byte[] bmpPicByteArray = bmpStream.toByteArray();
            streamLength = bmpPicByteArray.length;
            compressQuality -= 5;
            Log.d("compressBitmap", "Size: " + streamLength/1024+" kb");
        }while (streamLength >= MAX_IMAGE_SIZE);

        try {
            //save the resized and compressed file to disk cache
            Log.d("compressBitmap","cacheDir: "+context.getCacheDir());
            FileOutputStream bmpFile = new FileOutputStream(context.getCacheDir()+fileName);
            bmpPic.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpFile);
            bmpFile.flush();
            bmpFile.close();
        } catch (Exception e) {
            Log.e("compressBitmap", "Error on saving file");
        }
        //return the path of resized and compressed file
        return  context.getCacheDir()+fileName;
    }



    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        String debugTag = "MemoryInformation";
        // Image nin islenmeden onceki genislik ve yuksekligi
        final int height = options.outHeight;
        final int width = options.outWidth;
        Log.d(debugTag,"image height: "+height+ "---image width: "+ width);
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        Log.d(debugTag,"inSampleSize: "+inSampleSize);
        return inSampleSize;
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_CAMERA_REQUEST_CODE : {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                    showPictureDialog();

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

}
