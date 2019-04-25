package teckvillage.developer.khaled_pc.teckvillagetrue.View;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import teckvillage.developer.khaled_pc.teckvillagetrue.Controller.CustomRecyclerViewChatAdapter;
import teckvillage.developer.khaled_pc.teckvillagetrue.Model.retrofit.ApiAccessToken;
import teckvillage.developer.khaled_pc.teckvillagetrue.Model.retrofit.JSON_Mapping.DataReceivedChatUsers;
import teckvillage.developer.khaled_pc.teckvillagetrue.Model.retrofit.JSON_Mapping.LastMessageModel;
import teckvillage.developer.khaled_pc.teckvillagetrue.Model.retrofit.WhoCallerApi;
import teckvillage.developer.khaled_pc.teckvillagetrue.Model.retrofit.retrofitHead;
import teckvillage.developer.khaled_pc.teckvillagetrue.R;
import teckvillage.developer.khaled_pc.teckvillagetrue.RecycleviewResult_SearchAPI;
import teckvillage.developer.khaled_pc.teckvillagetrue.sms_messages_model;

import static teckvillage.developer.khaled_pc.teckvillagetrue.Chat_MessagesChat.ChatStatusChanged;

/**
 * A simple {@link Fragment} subclass.
 */
public class LastMessasgesFragment extends Fragment {

    List<LastMessageModel> chatMessageInfos=new ArrayList<>();
    RecyclerView chatRecyclerView;
    // ArrayAdapter arrayAdapter;

    CustomRecyclerViewChatAdapter customRecyclerViewChatAdapter;
    TextView noResult;
    ImageView refreshButton;
    ProgressDialog mProgressDialog;
    public LastMessasgesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_last_messages, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mProgressDialog = new ProgressDialog(getContext());
        noResult=view.findViewById(R.id.NoResult);
        chatRecyclerView=view.findViewById(R.id.ChatRecycler);
        refreshButton=view.findViewById(R.id.refresh);
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressDialog.setIndeterminate(true);
                mProgressDialog.setMessage("Loading...");
                mProgressDialog.setCancelable(false);
                mProgressDialog.show();
                getAllLastMessages();
            }
        });
        if (CheckNetworkConnection.hasInternetConnection(getContext())) {
            if (ConnectionDetector.hasInternetConnection(getContext())) {
                mProgressDialog.setIndeterminate(true);
                mProgressDialog.setMessage("Loading...");
                mProgressDialog.setCancelable(false);
                mProgressDialog.show();
            }
        }

        getAllLastMessages();
    }


    public void getAllLastMessages()

    {
        //API Request
        //Check wifi or data available
        if (CheckNetworkConnection.hasInternetConnection(getContext())) {

            //Check internet Access
            if (ConnectionDetector.hasInternetConnection(getContext())) {
                noResult.setVisibility(View.GONE);
                refreshButton.setVisibility(View.GONE);
                Retrofit retrofit = retrofitHead.headOfGetorPostReturnRes();
                WhoCallerApi whoCallerApi = retrofit.create(WhoCallerApi.class);
                Call<DataReceivedChatUsers> MessageWhenOpenChat = whoCallerApi.getallChatContact(ApiAccessToken.getAPIaccessToken(getContext()));

                MessageWhenOpenChat.enqueue(new Callback<DataReceivedChatUsers>() {
                    @Override
                    public void onResponse(Call<DataReceivedChatUsers> call, Response<DataReceivedChatUsers> response) {
                        if (response.isSuccessful())
                        {
                            if (mProgressDialog.isShowing())
                                mProgressDialog.dismiss();
                            chatMessageInfos=response.body().getLatestMessages();
                            sort_messages(chatMessageInfos);
                            Collections.reverse(chatMessageInfos);
                            customRecyclerViewChatAdapter=new CustomRecyclerViewChatAdapter(chatMessageInfos,getActivity());

                            chatRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                            chatRecyclerView.setAdapter(customRecyclerViewChatAdapter);
                        }
                        else
                        {
                            if (mProgressDialog.isShowing())
                                mProgressDialog.dismiss();
                            Log.d("dadadawqffsxzdf","failed");
                        }
                    }

                    @Override
                    public void onFailure(Call<DataReceivedChatUsers> call, Throwable t) {
                        Log.d("dadadawqffsxzdf","failure");
                        if (mProgressDialog.isShowing())
                            mProgressDialog.dismiss();
                    }
                });
            } else {
                noResult.setVisibility(View.VISIBLE);
                refreshButton.setVisibility(View.VISIBLE);
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
            }
        } else {
            noResult.setVisibility(View.VISIBLE);
            refreshButton.setVisibility(View.VISIBLE);
            if (mProgressDialog.isShowing())
                mProgressDialog.dismiss();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (ChatStatusChanged)
        {
            getAllLastMessages();
        }
    }

    private void sort_messages(List<LastMessageModel> list) {
        Collections.sort(list, new Comparator<LastMessageModel>() {
            public int compare(LastMessageModel o1, LastMessageModel o2) {
                long l1=covertTime(o1.getDate().getDate()).getTimeInMillis();
                long l2=covertTime(o2.getDate().getDate()).getTimeInMillis();
                if (l1 == l2)
                    return 0;
                return l1 < l2 ? -1 : 1;
            }
        });
    }

    public Calendar covertTime(String strDate)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = sdf.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        Log.d("calendar",cal.get(Calendar.YEAR)+"-"+cal.get(Calendar.MONTH)+"-"+cal.get(Calendar.DAY_OF_MONTH)
                +"-"+cal.get(Calendar.HOUR_OF_DAY)+"-"+cal.get(Calendar.MINUTE)+"-"+cal.get(Calendar.SECOND)+"-"+cal.get(Calendar.AM_PM));
        Log.d("caldaldkaldajlkj",cal.get(Calendar.YEAR)+"-"+cal.get(Calendar.MONTH)+"-"+cal.get(Calendar.DAY_OF_MONTH)
                +"-"+cal.get(Calendar.HOUR)+"-"+cal.get(Calendar.MINUTE)+"-"+cal.get(Calendar.SECOND));
        return cal;
    }
}
