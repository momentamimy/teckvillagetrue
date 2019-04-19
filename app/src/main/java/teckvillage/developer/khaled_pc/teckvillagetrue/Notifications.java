package teckvillage.developer.khaled_pc.teckvillagetrue;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import teckvillage.developer.khaled_pc.teckvillagetrue.Controller.CustomNotificationRecyclerViewAdapter;
import teckvillage.developer.khaled_pc.teckvillagetrue.Model.MessageInfo;
import teckvillage.developer.khaled_pc.teckvillagetrue.Model.retrofit.ApiAccessToken;
import teckvillage.developer.khaled_pc.teckvillagetrue.Model.retrofit.JSON_Mapping.BodyNumberModel;
import teckvillage.developer.khaled_pc.teckvillagetrue.Model.retrofit.JSON_Mapping.FetchedUserData;
import teckvillage.developer.khaled_pc.teckvillagetrue.Model.retrofit.JSON_Mapping.NotificattionDataReceived;
import teckvillage.developer.khaled_pc.teckvillagetrue.Model.retrofit.WhoCallerApi;
import teckvillage.developer.khaled_pc.teckvillagetrue.Model.retrofit.retrofitHead;
import teckvillage.developer.khaled_pc.teckvillagetrue.View.CheckNetworkConnection;
import teckvillage.developer.khaled_pc.teckvillagetrue.View.ConnectionDetector;
import teckvillage.developer.khaled_pc.teckvillagetrue.View.Signup;

public class Notifications extends AppCompatActivity {

    RecyclerView NotificationRecyclerview;

    List<NotificattionDataReceived> messageInfos;
    CustomNotificationRecyclerViewAdapter customNotificationRecyclerViewAdapter;
    TextView noResult;
    ImageView refreshButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        messageInfos=new ArrayList<>();


        MessageInfo info=new MessageInfo("","WhoCaller","you can send SMS messages to multiple Contacts",System.currentTimeMillis());
        MessageInfo info1=new MessageInfo("","WhoCaller","you can modify position of Caller Dialoghkhkhkh khkhkhkhkhkh khkhkhkhkhkh khkhkhkhkhkhkh bkgjgjgjgjgjgjgjgjgjgjgj",System.currentTimeMillis());


        /*messageInfos.add(info);
        messageInfos.add(info1);
*/

        getSupportActionBar().setTitle("Notification");
        getSupportActionBar().setDisplayShowHomeEnabled(true);   //back button on App Bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);   //back button on App Bar

        NotificationRecyclerview=findViewById(R.id.notificationRecyclerview);
        noResult=findViewById(R.id.NoResult);
        refreshButton=findViewById(R.id.refresh);
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getallnotification();
            }
        });

        getallnotification();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public void getallnotification() {
        if (CheckNetworkConnection.hasInternetConnection(getApplicationContext())) {

            //Check internet Access
            if (ConnectionDetector.hasInternetConnection(getApplicationContext())) {
                noResult.setVisibility(View.GONE);
                refreshButton.setVisibility(View.GONE);
                final ProgressDialog mProgressDialog = new ProgressDialog(Notifications.this);
                mProgressDialog.setIndeterminate(true);
                mProgressDialog.setMessage("Loading...");
                mProgressDialog.setCancelable(false);
                mProgressDialog.show();

                Retrofit retrofit = retrofitHead.headOfGetorPostReturnRes();
                WhoCallerApi whoCallerApi = retrofit.create(WhoCallerApi.class);
                Call<List<NotificattionDataReceived>> getallNotification = whoCallerApi.getallNotification(ApiAccessToken.getAPIaccessToken(getApplicationContext()));
                getallNotification.enqueue(new Callback<List<NotificattionDataReceived>>() {
                    @Override
                    public void onResponse(Call<List<NotificattionDataReceived>> call, Response<List<NotificattionDataReceived>> response) {
                        if (mProgressDialog.isShowing())
                            mProgressDialog.dismiss();
                        if (response.isSuccessful()) {
                            Log.d("paleezRun", response.body().get(0).getBody());
                            messageInfos = response.body();
                            if (messageInfos.size()==0)
                            {
                                noResult.setText("No notification at the moment");
                                noResult.setVisibility(View.VISIBLE);
                            }
                            customNotificationRecyclerViewAdapter = new CustomNotificationRecyclerViewAdapter(messageInfos, getApplicationContext());

                            LinearLayoutManager lLayout = new LinearLayoutManager(getApplicationContext());
                            NotificationRecyclerview.setLayoutManager(lLayout);
                            NotificationRecyclerview.setAdapter(customNotificationRecyclerViewAdapter);
                        } else {
                            Log.d("paleezRun", "notfafa");
                        }


                    }

                    @Override
                    public void onFailure(Call<List<NotificattionDataReceived>> call, Throwable t) {
                        Log.d("paleezRun", "fafa");
                        if (mProgressDialog.isShowing())
                            mProgressDialog.dismiss();
                    }
                });
            } else {
                noResult.setText("No Result Check Internet Connection");
                noResult.setVisibility(View.VISIBLE);
                refreshButton.setVisibility(View.VISIBLE);
            }
        } else {
            noResult.setText("No Result Check Internet Connection");
            noResult.setVisibility(View.VISIBLE);
            refreshButton.setVisibility(View.VISIBLE);
        }
    }
}
