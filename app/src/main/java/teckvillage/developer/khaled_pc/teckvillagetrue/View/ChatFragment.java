package teckvillage.developer.khaled_pc.teckvillagetrue.View;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import teckvillage.developer.khaled_pc.teckvillagetrue.Controller.CustomRecyclerViewChatAdapter;
import teckvillage.developer.khaled_pc.teckvillagetrue.R;
import teckvillage.developer.khaled_pc.teckvillagetrue.Model.MessageInfo;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends Fragment {

    ArrayList<MessageInfo> chatMessageInfos=new ArrayList<>();
    RecyclerView chatRecyclerView;
    // ArrayAdapter arrayAdapter;
    CustomRecyclerViewChatAdapter customRecyclerViewChatAdapter;
    FloatingActionButton fab;

    public ChatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        fab=view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(),SendToChatActivity.class));
            }
        });
        MessageInfo info=new MessageInfo("", "mo'men", "tyb",0, "464545","1");
        info.setType("2");
        MessageInfo info1=new MessageInfo("", "mo'men", "7ader",0, "4546045","1");
        info1.setType("2");
        MessageInfo info2=new MessageInfo("", "mo'men", "mashy",0, "454546","1");
        info2.setType("2");

        chatMessageInfos.add(info);
        chatMessageInfos.add(info1);
        chatMessageInfos.add(info2);

        chatRecyclerView=view.findViewById(R.id.ChatRecycler);
        chatRecyclerView.setVisibility(View.GONE);
        customRecyclerViewChatAdapter=new CustomRecyclerViewChatAdapter(chatMessageInfos,getActivity());

        chatRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        chatRecyclerView.setAdapter(customRecyclerViewChatAdapter);

    }
}
