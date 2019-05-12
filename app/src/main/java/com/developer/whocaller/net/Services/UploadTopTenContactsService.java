package com.developer.whocaller.net.Services;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v4.app.JobIntentService;
import android.util.Log;

import com.developer.whocaller.net.View.ConnectionDetector;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import com.developer.whocaller.net.Model.Get_Calls_Log;
import com.developer.whocaller.net.Model.retrofit.ApiAccessToken;
import com.developer.whocaller.net.Model.retrofit.JSON_Mapping.ResultModelUploadVCF;
import com.developer.whocaller.net.Model.retrofit.JSON_Mapping.Send_Top_Ten_Contacts_JSON;
import com.developer.whocaller.net.Model.retrofit.JSON_Mapping.Send_Top_Ten_Contacts_JSON_Arraylist;
import com.developer.whocaller.net.Model.retrofit.JSON_Mapping.datamodel;
import com.developer.whocaller.net.Model.retrofit.WhoCallerApi;
import com.developer.whocaller.net.Model.retrofit.retrofitHead;
import com.developer.whocaller.net.View.CheckNetworkConnection;

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

        try {

                get_calls_log = new Get_Calls_Log(getApplicationContext());
                ArrayList<Send_Top_Ten_Contacts_JSON_Arraylist> listts = new ArrayList<>();

                // get Top Ten contact from Device
                ArrayList<Send_Top_Ten_Contacts_JSON> list = get_calls_log.getTopTenContactsToServer();

                //Check if Empty
                if (list == null||list.size()<=0){
                    Log.e(TAG, "onHandleWork: Invalid ");
                    return;
                }


                //Add all
                for(int i = 0; i< list.size(); i++){
                    ArrayList<String> listss = new ArrayList<String>();
                    listss.add(list.get(i).getPhones());
                    listts.add(new Send_Top_Ten_Contacts_JSON_Arraylist(list.get(i).getName(),listss));
                }


                //Correct Solution
                datamodel datamodel=new datamodel();
                datamodel.setContacts(listts);

            //Check wifi or data available
            if (CheckNetworkConnection.hasInternetConnection(this)) {

                //Check internet Access
                if (ConnectionDetector.hasInternetConnection(this)) {


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
                               //Top Ten Upload success
                               SharedPreferences sharedPref = getSharedPreferences("WhoCaller?", MODE_PRIVATE);
                               SharedPreferences.Editor editor = sharedPref.edit();
                               editor.putString("UploadstatusTopten","success");
                               editor.commit();

                           }else {
                               Log.w("Fail", String.valueOf(response.body()));
                               SharedPreferences sharedPref = getSharedPreferences("WhoCaller?", MODE_PRIVATE);
                               SharedPreferences.Editor editor = sharedPref.edit();
                               editor.putString("UploadstatusTopten","failed");
                               editor.commit();
                           }

                        }

                        @Override
                        public void onFailure(Call<ResultModelUploadVCF> call, Throwable t) {
                            Log.w("onFailure", t.getMessage());
                            Log.w("onFailure", t.getCause());

                            SharedPreferences sharedPref = getSharedPreferences("WhoCaller?", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putString("UploadstatusTopten","failed");
                            editor.commit();
                        }
                    });


                }else {
                    SharedPreferences sharedPref = getSharedPreferences("WhoCaller?", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("UploadstatusTopten","failed");
                    editor.commit();
                }
            }else {
                SharedPreferences sharedPref = getSharedPreferences("WhoCaller?", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("UploadstatusTopten","failed");
                editor.commit();
            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }

}