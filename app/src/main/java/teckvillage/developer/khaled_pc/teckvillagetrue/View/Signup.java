package teckvillage.developer.khaled_pc.teckvillagetrue.View;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.facebook.login.widget.LoginButton;
import com.squareup.picasso.Picasso;

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

import teckvillage.developer.khaled_pc.teckvillagetrue.MainActivity;
import teckvillage.developer.khaled_pc.teckvillagetrue.R;

import static android.media.MediaRecorder.VideoSource.CAMERA;

public class Signup extends AppCompatActivity {

    private static final int GALLERY = 0;
    private static final String IMAGE_DIRECTORY = "/WhoCaller"; ;
    ImageView add_personal_photo;
    TextView  FirstName,LastName,Email;
    TextInputLayout FirstNameError,LastNameError,EmailError;
    Button continue_btn;
    CallbackManager callbackManager;
    LoginButton loginButton;
    private static final String EMAIL = "email";
    private static final String PROFILE_PIC = "public_profile";
    private static final String USER_FRIEND = "user_friends";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_signup);

        //init View
        add_personal_photo=findViewById(R.id.add_personal_image);
        FirstName=findViewById(R.id.first_name);
        LastName=findViewById(R.id.last_name);
        Email=findViewById(R.id.Email);
        FirstNameError=findViewById(R.id.input_layout_first_name);
        LastNameError=findViewById(R.id.input_layout_last_name);
        EmailError=findViewById(R.id.input_layout_Email);
        continue_btn=findViewById(R.id.btn_continue);




        //FACEBOOK
        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList(EMAIL,PROFILE_PIC,USER_FRIEND));
        //LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));
        // If you are using in a fragment, call loginButton.setFragment(this);

        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
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

                if(validation(FirstName.getText().toString().trim(),LastName.getText().toString().trim(),Email.getText().toString().trim())) {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
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
                    add_personal_photo.setPadding(0,0,0,0);
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(Signup.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == CAMERA) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            add_personal_photo.setImageBitmap(thumbnail);
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
                            Picasso

                            Log.i("Link",link);
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
        parameters.putString("fields", "id,email,first_name,last_name,gender,birthday,picture.type(large)");
        request.setParameters(parameters);
        request.executeAsync();
    }

}
