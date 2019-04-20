package teckvillage.developer.khaled_pc.teckvillagetrue.Services;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.JobIntentService;
import android.util.Log;
import android.widget.Toast;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import teckvillage.developer.khaled_pc.teckvillagetrue.Model.retrofit.ApiAccessToken;
import teckvillage.developer.khaled_pc.teckvillagetrue.Model.retrofit.JSON_Mapping.ResultModelUploadVCF;
import teckvillage.developer.khaled_pc.teckvillagetrue.Model.retrofit.WhoCallerApi;
import teckvillage.developer.khaled_pc.teckvillagetrue.Model.retrofit.retrofitHead;

/**
 * Created by khaled-pc on 4/11/2019.
 */

public class FileUploadService extends JobIntentService {

    private static final String TAG = "FileUploadService";
    Context context;
    String  vfile = "/ContactWhoCaller.vcf";
    ArrayList<String> vCard ;
    private Cursor cursor;
    boolean stateVcf;

    /**
     * Unique job ID for this service.
     */
    private static final int JOB_ID = 131;

    public static void enqueueWork(Context context, Intent intent) {
        enqueueWork(context, FileUploadService.class, JOB_ID, intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        /**
         * Download/Upload of file
         * The system or framework is already holding a wake lock for us at this point
         */



        try {

            getVcardString();

        } catch (IOException e) {
            Log.w("Error",e.getCause());
            Log.w("Error",e.getMessage());

        }catch (Exception e) {
            Log.w("Other Error",e.getMessage());
            Log.w("Other Error",e.getCause());
        }


        File file = null;
        MultipartBody.Part fileToUpload=null;
        RequestBody mFile=null;

        try {

        file= exportVCF();
        mFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        fileToUpload = MultipartBody.Part.createFormData("contacts", file.getName(), mFile);

        }catch (Exception e){
            e.printStackTrace();
        }

        Retrofit retrofit = retrofitHead.headOfGetorPostReturnRes();
        WhoCallerApi whoCallerApi = retrofit.create(WhoCallerApi.class);
        Log.w("apiaccesstoken",ApiAccessToken.getAPIaccessToken(getApplicationContext()));
        Call<ResultModelUploadVCF> uploadVCF = whoCallerApi.UploadVCF(ApiAccessToken.getAPIaccessToken(getApplicationContext()),fileToUpload);

       uploadVCF.enqueue(new Callback<ResultModelUploadVCF>() {
           @Override
           public void onResponse(Call<ResultModelUploadVCF> call, Response<ResultModelUploadVCF> response) {

               String mesf = response.body().getMsg();
               boolean me = response.body().isResponse();
               Log.w("success", mesf);
               Log.w("success", String.valueOf(me));
               //Toast.makeText(getApplicationContext(),"successUploadVCF",Toast.LENGTH_LONG).show();
           }

           @Override
           public void onFailure(Call<ResultModelUploadVCF> call, Throwable t) {
               Log.w("onFailure", t.getMessage());
               Log.w("onFailure", t.getCause());
           }
       });

    }


    private void getVcardString()throws IOException {
        vCard = new ArrayList<String>();  // Its global....;
        cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        if(cursor!=null&& cursor.moveToFirst()&&cursor.getCount()>0)
        {
            stateVcf=true;
            int i;
            String storage_path = Environment.getExternalStorageDirectory().getAbsolutePath() + vfile;
            FileOutputStream mFileOutputStream = new FileOutputStream(storage_path, false);
            cursor.moveToFirst();
            for(i = 0;i<cursor.getCount();i++)
            {


                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                    getOreo(cursor);
                }else {
                    get(cursor);
                }

                cursor.moveToNext();
                Log.d("TAGVCF", "Contact "+(i+1)+"VcF String is"+vCard.get(i));

                mFileOutputStream.write(vCard.get(i).toString().getBytes());
            }
            mFileOutputStream.close();
            cursor.close();
        }
        else
        {
            Log.d("TAG", "No Contacts in Your Phone");
            stateVcf=false;
        }
    }




    private File exportVCF() {
         File CSVFile = null;
        File CSVFile2 = null;
        File CSVFile3 = null;
        if(stateVcf){
            Log.w("sss",Environment.getExternalStorageDirectory().getAbsolutePath() );
            CSVFile = new File( Environment.getExternalStorageDirectory().getAbsolutePath() +  vfile);
            Log.w("CSVFile", String.valueOf(CSVFile));
            CSVFile2 = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/send_contacts.vcf");
            Log.w("CSVFile", String.valueOf(CSVFile2));

        } else {
            //Toast.makeText(getApplicationContext(), "Information not available to create CSV.", Toast.LENGTH_SHORT).show();
            Log.w("meassage","Information not available to create CSV.");
        }

        return CSVFile;
    }

   public synchronized void get(Cursor cursor) {

        String lookupKey = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));
        Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_VCARD_URI, lookupKey);
        AssetFileDescriptor fd;
        try {
            if(this.getContentResolver().openAssetFileDescriptor(uri, "r")!=null){
                fd = this.getContentResolver().openAssetFileDescriptor(uri, "r");

                FileInputStream fis = fd.createInputStream();
                byte[] buf = new byte[(int) fd.getDeclaredLength()];
                fis.read(buf);
                String vcardstring= new String(buf);
                vCard.add(vcardstring);
            }

        } catch (Exception e1)
        {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }

    public void getOreo(Cursor cursor) {


        String lookupKey = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));
        Log.w("lookkey",lookupKey);
        Log.w("lookket", cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)));
        Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_VCARD_URI, lookupKey);
        Log.w("URI", String.valueOf(uri));
        ParcelFileDescriptor fd;
        try {


            fd = this.getContentResolver().openFileDescriptor(uri, "r");
            // Your Complex Code and you used function without loop so how can you get all Contacts Vcard.??

            FileInputStream fis = new FileInputStream(fd.getFileDescriptor());
            byte[] buf = new byte[fis.available()];
            fis.read(buf);
            String vcardstring = new String(buf);
            vCard.add(vcardstring);

            String storage_path = Environment.getExternalStorageDirectory().toString() + vfile;
            FileOutputStream mFileOutputStream = new FileOutputStream(storage_path, false);
            mFileOutputStream.write(vcardstring.toString().getBytes());

    } catch (FileNotFoundException e) {
            Log.e(TAG, "Vcard for the contact " + lookupKey + " not found", e);
    } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }

}