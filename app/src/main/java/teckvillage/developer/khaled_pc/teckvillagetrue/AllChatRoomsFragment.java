package teckvillage.developer.khaled_pc.teckvillagetrue;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import teckvillage.developer.khaled_pc.teckvillagetrue.Controller.AllChatRoomsAdapters;
import teckvillage.developer.khaled_pc.teckvillagetrue.Controller.SendToChatContactsAdapters;
import teckvillage.developer.khaled_pc.teckvillagetrue.Model.retrofit.ApiAccessToken;
import teckvillage.developer.khaled_pc.teckvillagetrue.Model.retrofit.JSON_Mapping.DataReceivedChatUsers;
import teckvillage.developer.khaled_pc.teckvillagetrue.Model.retrofit.JSON_Mapping.RoomModel;
import teckvillage.developer.khaled_pc.teckvillagetrue.Model.retrofit.WhoCallerApi;
import teckvillage.developer.khaled_pc.teckvillagetrue.Model.retrofit.retrofitHead;
import teckvillage.developer.khaled_pc.teckvillagetrue.View.CheckNetworkConnection;
import teckvillage.developer.khaled_pc.teckvillagetrue.View.ConnectionDetector;


/**
 * A simple {@link Fragment} subclass.
 */
public class AllChatRoomsFragment extends Fragment {


    public AllChatRoomsFragment() {
        // Required empty public constructor
    }

    RecyclerView allChatRoomsRecyclerView;
    AllChatRoomsAdapters allChatAdapter;
    public List<RoomModel> allChatContact;
    TextView noResult;
    ImageView refreshButton;
    ProgressDialog mProgressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_all_chat_rooms, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        mProgressDialog = new ProgressDialog(getContext());
        noResult=view.findViewById(R.id.NoResult);
        allChatRoomsRecyclerView=view.findViewById(R.id.All_Chat_Rooms_RecyclerView);

        refreshButton=view.findViewById(R.id.refresh);
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressDialog.setIndeterminate(true);
                mProgressDialog.setMessage("Loading...");
                mProgressDialog.setCancelable(false);
                mProgressDialog.show();
                getAllChatRooms();
            }
        });

        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
        getAllChatRooms();

    }

    public void getAllChatRooms()
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
                            Log.d("dadadawqffsxzdf","sucees");
                            allChatContact= new ArrayList<>();

                            allChatContact=response.body().getRoom();

                            allChatAdapter=new AllChatRoomsAdapters(getContext(),allChatContact);
                            allChatRoomsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                            allChatRoomsRecyclerView.setAdapter(allChatAdapter);
                        }
                        else
                        {
                            Log.d("dadadawqffsxzdf","failed");
                            if (mProgressDialog.isShowing())
                                mProgressDialog.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<DataReceivedChatUsers> call, Throwable t) {
                        Log.d("dadadawqffsxzdf","failure");
                        if (mProgressDialog.isShowing())
                            mProgressDialog.dismiss();
                    }
                });
            }else {
                noResult.setVisibility(View.VISIBLE);
                refreshButton.setVisibility(View.VISIBLE);
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
            }
        } else {
            refreshButton.setVisibility(View.VISIBLE);
            noResult.setVisibility(View.VISIBLE);
            if (mProgressDialog.isShowing())
                mProgressDialog.dismiss();
        }
    }
}
