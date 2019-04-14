package teckvillage.developer.khaled_pc.teckvillagetrue.Services;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.JobIntentService;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import teckvillage.developer.khaled_pc.teckvillagetrue.model.Get_Calls_Log;
import teckvillage.developer.khaled_pc.teckvillagetrue.model.retrofit.ApiAccessToken;
import teckvillage.developer.khaled_pc.teckvillagetrue.model.retrofit.JSON_Mapping.ResultModelUploadVCF;
import teckvillage.developer.khaled_pc.teckvillagetrue.model.retrofit.JSON_Mapping.Send_Top_Ten_Contacts_JSON;
import teckvillage.developer.khaled_pc.teckvillagetrue.model.retrofit.JSON_Mapping.Send_Top_Ten_Contacts_JSON_Arraylist;
import teckvillage.developer.khaled_pc.teckvillagetrue.model.retrofit.JSON_Mapping.datamodel;
import teckvillage.developer.khaled_pc.teckvillagetrue.model.retrofit.WhoCallerApi;
import teckvillage.developer.khaled_pc.teckvillagetrue.model.retrofit.retrofitHead;

/**
 * Created by khaled-pc on 4/11/2019.
 */

public class UploadTopTenContactsService extends JobIntentService {

    private static final String TAG = "UploadTopTenContacts";
    Get_Calls_Log get_calls_log;

    /**
     * Unique job ID for this service.
     */
    private static final int JOB_ID = 111;

    public static void enqueueWork(Context context, Intent intent) {
        enqueueWork(context, UploadTopTenContactsService.class, JOB_ID, intent);
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


        get_calls_log = new Get_Calls_Log(getApplicationContext());
        ArrayList<Send_Top_Ten_Contacts_JSON_Arraylist> listts = new ArrayList<>();;

        // get Top Ten contact from Device
        ArrayList<Send_Top_Ten_Contacts_JSON> list = get_calls_log.getTopTenContactsToServer();

        //Check if Empty
        if (list == null||list.size()<0){
            Log.e(TAG, "onHandleWork: Invalid ");
            return;
        }


        //Add all
        for(int i = 0; i< 10; i++){
            ArrayList<String> listss = new ArrayList<String>();
            listss.add(list.get(i).getPhones());
            listts.add(new Send_Top_Ten_Contacts_JSON_Arraylist(list.get(i).getName(),listss));
        }


        //Correct Solution
        datamodel datamodel=new datamodel();
        datamodel.setContacts(listts);

        Retrofit retrofit = retrofitHead.headOfGetorPostReturnRes();
        WhoCallerApi whoCallerApi = retrofit.create(WhoCallerApi.class);
        Call<ResultModelUploadVCF> uploadVCF = whoCallerApi.UploadTopTenContacts("application/json",ApiAccessToken.getAPIaccessToken(getApplicationContext()), datamodel);

        uploadVCF.enqueue(new Callback<ResultModelUploadVCF>() {
            @Override
            public void onResponse(Call<ResultModelUploadVCF> call, Response<ResultModelUploadVCF> response) {

               if(response.code()==200){
                   String mesf = response.body().getMsg();
                   boolean me = response.body().isResponse();
                   Log.w("success", mesf);
                   Log.w("success", String.valueOf(me));

               }else {
                   Log.w("Fail", String.valueOf(response.body()));
               }

            }

            @Override
            public void onFailure(Call<ResultModelUploadVCF> call, Throwable t) {
                Log.w("onFailure", t.getMessage());
                Log.w("onFailure", t.getCause());
            }
        });

    }

}