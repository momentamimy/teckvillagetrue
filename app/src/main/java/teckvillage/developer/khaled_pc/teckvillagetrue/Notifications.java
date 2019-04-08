package teckvillage.developer.khaled_pc.teckvillagetrue;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import java.util.ArrayList;

import teckvillage.developer.khaled_pc.teckvillagetrue.Controller.CustomNotificationRecyclerViewAdapter;
import teckvillage.developer.khaled_pc.teckvillagetrue.model.MessageInfo;

public class Notifications extends AppCompatActivity {

    RecyclerView NotificationRecyclerview;

    ArrayList<MessageInfo> messageInfos;
    CustomNotificationRecyclerViewAdapter customNotificationRecyclerViewAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        messageInfos=new ArrayList<>();


        MessageInfo info=new MessageInfo("","WhoCaller","you can send SMS messages to multiple Contacts",System.currentTimeMillis());
        MessageInfo info1=new MessageInfo("","WhoCaller","you can modify position of Caller Dialoghkhkhkh khkhkhkhkhkh khkhkhkhkhkh khkhkhkhkhkhkh bkgjgjgjgjgjgjgjgjgjgjgj",System.currentTimeMillis());


        messageInfos.add(info);
        messageInfos.add(info1);

        getSupportActionBar().setTitle("Notification");
        getSupportActionBar().setDisplayShowHomeEnabled(true);   //back button on App Bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);   //back button on App Bar

        NotificationRecyclerview=findViewById(R.id.notificationRecyclerview);
        customNotificationRecyclerViewAdapter=new CustomNotificationRecyclerViewAdapter(messageInfos,getApplicationContext());

        LinearLayoutManager lLayout = new LinearLayoutManager(getApplicationContext());
        NotificationRecyclerview.setLayoutManager(lLayout);
        NotificationRecyclerview.setAdapter(customNotificationRecyclerViewAdapter);

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
}
