package teckvillage.developer.khaled_pc.teckvillagetrue.Services;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.JobIntentService;
import android.util.Log;
import android.widget.Toast;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
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
    private static final int JOB_ID = 101;

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
        vCard = new ArrayList<String>();  // Its global....
        cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        if(cursor!=null&&cursor.getCount()>0)
        {
            stateVcf=true;
            int i;
            String storage_path = Environment.getExternalStorageDirectory().getAbsolutePath() + vfile;
            FileOutputStream mFileOutputStream = new FileOutputStream(storage_path, false);
            cursor.moveToFirst();
            for(i = 0;i<cursor.getCount();i++)
            {
                get(cursor);
                Log.d("TAGVCF", "Contact "+(i+1)+"VcF String is"+vCard.get(i));
                cursor.moveToNext();
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


    public void get(Cursor cursor)
    {
        String lookupKey = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));
        Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_VCARD_URI, lookupKey);
        AssetFileDescriptor fd;
        try {
            fd = this.getContentResolver().openAssetFileDescriptor(uri, "r");

            FileInputStream fis = fd.createInputStream();
            byte[] buf = new byte[(int) fd.getDeclaredLength()];
            fis.read(buf);
            String vcardstring= new String(buf);
            vCard.add(vcardstring);
        } catch (Exception e1)
        {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }

    private File exportVCF() {
         File CSVFile = null;
        File CSVFile2 = null;
        if(stateVcf){
            Log.w("sss","d5lll");
            Log.w("sss",Environment.getExternalStorageDirectory().getAbsolutePath() );
            CSVFile = new File( Environment.getExternalStorageDirectory().getAbsolutePath() +  vfile);
            Log.w("CSVFile", String.valueOf(CSVFile));
            CSVFile2 = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/send_contacts.vcf");
            Log.w("CSVFile", String.valueOf(CSVFile2));
        } else {
            Toast.makeText(getApplicationContext(), "Information not available to create CSV.", Toast.LENGTH_SHORT).show();
        }

        return CSVFile;
    }



}