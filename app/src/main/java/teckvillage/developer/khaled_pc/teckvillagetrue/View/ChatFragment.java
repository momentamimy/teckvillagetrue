package teckvillage.developer.khaled_pc.teckvillagetrue.View;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import teckvillage.developer.khaled_pc.teckvillagetrue.Controller.CustomListViewAdapter;
import teckvillage.developer.khaled_pc.teckvillagetrue.Controller.CustomListViewChatAdapter;
import teckvillage.developer.khaled_pc.teckvillagetrue.FragMessageOthers;
import teckvillage.developer.khaled_pc.teckvillagetrue.R;
import teckvillage.developer.khaled_pc.teckvillagetrue.SMS_MessagesChat;
import teckvillage.developer.khaled_pc.teckvillagetrue.model.MessageInfo;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends Fragment {

    ArrayList<MessageInfo> chatMessageInfos=new ArrayList<>();
    ArrayList<MessageInfo> allMessageChatInfos=new ArrayList<>();
    ListView chatListView;
    // ArrayAdapter arrayAdapter;
    CustomListViewChatAdapter customListViewChatAdapter;
    boolean endChatsList=false;

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

        MessageInfo info=new MessageInfo("", "mo'men", "tyb",0, "464545","1");
        info.setType("2");
        MessageInfo info1=new MessageInfo("", "mo'men", "7ader",0, "4546045","1");
        info1.setType("2");
        MessageInfo info2=new MessageInfo("", "mo'men", "mashy",0, "454546","1");
        info2.setType("2");

        chatMessageInfos.add(info);
        chatMessageInfos.add(info1);
        chatMessageInfos.add(info2);

        chatListView = (ListView) view.findViewById(R.id.ChatList);
        customListViewChatAdapter =new CustomListViewChatAdapter(chatMessageInfos,getActivity());

        chatListView.setAdapter(customListViewChatAdapter);
        chatListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(getContext(), SMS_MessagesChat.class);
                intent.putExtra("LogSMSName",chatMessageInfos.get(position).logName);
                intent.putExtra("LogSMSAddress",chatMessageInfos.get(position).logAddress);
                startActivity(intent);

                MessageInfo info=chatMessageInfos.get(position);
                info.setRead("true");
                chatMessageInfos.set(position,info);
                customListViewChatAdapter.notifyDataSetChanged();
            }
        });
        chatListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                Log.d("2a5er_position", String.valueOf(chatListView.getLastVisiblePosition()));
                if (chatListView.getLastVisiblePosition()>=chatMessageInfos.size()-1&&!endChatsList)
                {

                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }

        });
}
}
