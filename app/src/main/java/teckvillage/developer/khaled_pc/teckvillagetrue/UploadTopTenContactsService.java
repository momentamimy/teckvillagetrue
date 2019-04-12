package teckvillage.developer.khaled_pc.teckvillagetrue;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.JobIntentService;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

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
        //ArrayList<Send_Top_Ten_Contacts_JSON> list=new ArrayList<>();

        get_calls_log = new Get_Calls_Log(getApplicationContext());
        ArrayList<Send_Top_Ten_Contacts_JSON_Arraylist> listts = new ArrayList<>();;


        // get file list here
        ArrayList<Send_Top_Ten_Contacts_JSON> list = get_calls_log.getTopTenContactsToServer();
        Send_Top_Ten_Contacts_JSON_Arraylist send_top_ten_contacts_json_arraylist=new Send_Top_Ten_Contacts_JSON_Arraylist();

        if (list == null||list.size()<0){
            Log.e(TAG, "onHandleWork: Invalid ");
            return;
        }


        ArrayList<String> listss = new ArrayList<String>();
        listss.add("\"012809456\"");
        //listss.add("\"010573845\"");
        for(int i = 0; i< 10; i++){
            Log.w("ohhh", String.valueOf(list.get(i).getId())+" || "+list.get(i).getName()+" || "+ list.get(i).getPhones());
            send_top_ten_contacts_json_arraylist.setId(list.get(i).getId());
            send_top_ten_contacts_json_arraylist.setName("\"khaled\"");
            send_top_ten_contacts_json_arraylist.setPhones(listss);
            Log.w("ohhh2", send_top_ten_contacts_json_arraylist.getId()+" || "+send_top_ten_contacts_json_arraylist.getName()+" || "+String.valueOf(send_top_ten_contacts_json_arraylist.getPhones()));

            listts.add(send_top_ten_contacts_json_arraylist);
        }


      /*  ArrayList<Send_Top_Ten_Contacts_JSON_Arraylist> listts = null;
        listts.add(new Send_Top_Ten_Contacts_JSON_Arraylist(0,"wla",listss));*/

        datamodel datamodel=new datamodel();
        datamodel.setContacts(listts);
        Log.w("aaaaaaaa", String.valueOf(datamodel.getContacts()));


        Retrofit retrofit = retrofitHead.headOfGetorPostReturnRes();
        WhoCallerApi whoCallerApi = retrofit.create(WhoCallerApi.class);
        Log.w("apiaccesstoken",ApiAccessToken.getAPIaccessToken(getApplicationContext()));
        Call<ResultModelUploadVCF> uploadVCF = whoCallerApi.UploadTopTenContacts(ApiAccessToken.getAPIaccessToken(getApplicationContext()), datamodel);

        uploadVCF.enqueue(new Callback<ResultModelUploadVCF>() {
            @Override
            public void onResponse(Call<ResultModelUploadVCF> call, Response<ResultModelUploadVCF> response) {

               if(response.code()==200){
                   String mesf = response.body().getMsg();
                   boolean me = response.body().isResponse();
                   Log.w("success", mesf);
                   Log.w("success", String.valueOf(me));
                   Toast.makeText(getApplicationContext(),"successUploadTopTenContacts",Toast.LENGTH_LONG).show();
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