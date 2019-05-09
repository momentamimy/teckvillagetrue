package teckvillage.developer.khaled_pc.teckvillagetrue.Services;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v4.app.JobIntentService;
import android.util.Log;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import teckvillage.developer.khaled_pc.teckvillagetrue.Model.Get_Calls_Log;
import teckvillage.developer.khaled_pc.teckvillagetrue.Model.PhoneContactNumbersModel;
import teckvillage.developer.khaled_pc.teckvillagetrue.Model.SharedPref_Arraylist_Save_Get;
import teckvillage.developer.khaled_pc.teckvillagetrue.Model.database.Database_Helper;
import teckvillage.developer.khaled_pc.teckvillagetrue.Model.retrofit.ApiAccessToken;
import teckvillage.developer.khaled_pc.teckvillagetrue.Model.retrofit.JSON_Mapping.ResultModelUploadVCF;
import teckvillage.developer.khaled_pc.teckvillagetrue.Model.retrofit.JSON_Mapping.Send_BlockList_JSON_Arraylist;
import teckvillage.developer.khaled_pc.teckvillagetrue.Model.retrofit.JSON_Mapping.Send_Top_Ten_Contacts_JSON;
import teckvillage.developer.khaled_pc.teckvillagetrue.Model.retrofit.JSON_Mapping.Send_Top_Ten_Contacts_JSON_Arraylist;
import teckvillage.developer.khaled_pc.teckvillagetrue.Model.retrofit.JSON_Mapping.datamodel;
import teckvillage.developer.khaled_pc.teckvillagetrue.Model.retrofit.JSON_Mapping.datamodelBlocklist;
import teckvillage.developer.khaled_pc.teckvillagetrue.Model.retrofit.WhoCallerApi;
import teckvillage.developer.khaled_pc.teckvillagetrue.Model.retrofit.retrofitHead;
import teckvillage.developer.khaled_pc.teckvillagetrue.View.CheckNetworkConnection;
import teckvillage.developer.khaled_pc.teckvillagetrue.View.ConnectionDetector;

/**
 * Created by khaled-pc on 4/23/2019.
 */

public class UploadnewContactAdded extends JobIntentService {

    private static final String TAG = "UploadnewContact";


    /**
     * Unique job ID for this service.
     */
    private static final int JOB_ID = 125;

    public static void enqueueWork(Context context, Intent intent) {
        enqueueWork(context, UploadnewContactAdded.class, JOB_ID, intent);
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



            ArrayList<Send_Top_Ten_Contacts_JSON_Arraylist> listts = new ArrayList<>();

            // get Top Ten contact from Device
            ArrayList<PhoneContactNumbersModel> list = checkNewContact(getApplicationContext());




            //Check if Empty
            if (list == null||list.size()<=0){
                Log.e(TAG, "onHandleWork: Invalid ");
                return;
            }


            //Add all
            for(int i = 0; i< list.size(); i++){
                ArrayList<String> listss = new ArrayList<String>();
                listss.add(list.get(i).getPhoneNumber());
                listts.add(new Send_Top_Ten_Contacts_JSON_Arraylist(list.get(i).getContactName(),listss));
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
                    Call<ResultModelUploadVCF> uploadVCF = whoCallerApi.UploadNewContacts("application/json", ApiAccessToken.getAPIaccessToken(getApplicationContext()), datamodel);

                    uploadVCF.enqueue(new Callback<ResultModelUploadVCF>() {
                        @Override
                        public void onResponse(Call<ResultModelUploadVCF> call, Response<ResultModelUploadVCF> response) {

                            if(response.isSuccessful()){

                                String mesf = response.body().getMsg();
                                boolean me = response.body().isResponse();
                                Log.w("success", mesf);
                                Log.w("success", String.valueOf(me));

                                //Top Ten Upload success
                                SharedPreferences sharedPref = getSharedPreferences("WhoCaller?", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPref.edit();
                                editor.putBoolean("NewContact",false);
                                editor.commit();

                            }else {
                                SharedPreferences sharedPref = getSharedPreferences("WhoCaller?", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPref.edit();
                                editor.putBoolean("NewContact",true);
                                editor.commit();
                            }

                        }

                        @Override
                        public void onFailure(Call<ResultModelUploadVCF> call, Throwable t) {
                            Log.w("onFailure", t.getMessage());
                            Log.w("onFailure", t.getCause());


                            SharedPreferences sharedPref = getSharedPreferences("WhoCaller?", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putBoolean("NewContact",true);
                            editor.commit();


                        }
                    });


                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }



    Get_Calls_Log get_calls_log;
    ArrayList<PhoneContactNumbersModel> phoneContactNumbersModels;
    ArrayList<String> phoneNumbers=new ArrayList<>();
    ArrayList<PhoneContactNumbersModel> updatedContactNumbers;
    SharedPreferences sharedPref;
    public ArrayList<PhoneContactNumbersModel> checkNewContact(Context context)
    {
        updatedContactNumbers=new ArrayList<>();
        get_calls_log=new Get_Calls_Log(context);
        phoneContactNumbersModels=get_calls_log.getAllNumbers();
        sharedPref=context.getSharedPreferences("PhoneNumberIDs",MODE_PRIVATE);
        int size=sharedPref.getInt("IDSize",-1);
        if (size!=-1)
        {
            Log.d("hlklkisfafafqadd","  ");
            if (size<phoneContactNumbersModels.size())
            {
                phoneNumbers= SharedPref_Arraylist_Save_Get.getArrayList(context,"ArrayNumbers");

                for (int i=0;i<phoneContactNumbersModels.size();i++)
                {
                    boolean add=true;
                    for (int j=0;j<phoneNumbers.size();j++) {
                        if (phoneContactNumbersModels.get(i).getPhoneNumber().equals(phoneNumbers.get(j))) {
                            add=false;
                            Log.d("hlklkisadd","  "+add);
                            break;
                        }
                    }
                    if (add)
                    {
                        updatedContactNumbers.add(phoneContactNumbersModels.get(i));
                        Log.d("hlklkadd","  "+add+"  "+phoneContactNumbersModels.get(i).getPhoneNumber());
                    }
                }

            }
            phoneNumbers=new ArrayList<>();
            for (int i=0;i<phoneContactNumbersModels.size();i++)
            {
                phoneNumbers.add(phoneContactNumbersModels.get(i).getPhoneNumber());
                Log.d("hlklklklklklklklk",""+phoneContactNumbersModels.get(i).getPhoneNumber());
            }

            SharedPref_Arraylist_Save_Get.saveArrayList(context,phoneNumbers,"ArrayNumbers");

            SharedPreferences.Editor editor= sharedPref.edit();
            editor.putInt("IDSize",phoneContactNumbersModels.size());
            editor.commit();
        }
        else
        {

            for (int i=0;i<phoneContactNumbersModels.size();i++)
            {
                phoneNumbers.add(phoneContactNumbersModels.get(i).getPhoneNumber());
                Log.d("hlklklklklklklklk",""+phoneContactNumbersModels.get(i).getPhoneNumber());
            }

            SharedPref_Arraylist_Save_Get.saveArrayList(context,phoneNumbers,"ArrayNumbers");

            SharedPreferences.Editor editor= sharedPref.edit();
            editor.putInt("IDSize",phoneContactNumbersModels.size());
            editor.commit();
        }
        return updatedContactNumbers;
    }
}