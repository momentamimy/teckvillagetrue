package teckvillage.developer.khaled_pc.teckvillagetrue;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import teckvillage.developer.khaled_pc.teckvillagetrue.Controller.LetterComparator;
import teckvillage.developer.khaled_pc.teckvillagetrue.Controller.SendToContactsAdapters;
import teckvillage.developer.khaled_pc.teckvillagetrue.Controller.UserContactsAdapters;
import teckvillage.developer.khaled_pc.teckvillagetrue.model.Get_User_Contacts;

public class SendToActivity extends AppCompatActivity {

    ImageView back;
    EditText sendToEditText;
    Get_User_Contacts get_user_contacts;
    RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_to);
        back=findViewById(R.id.back_Send_To);
        sendToEditText=findViewById(R.id.Send_To_edittext);
        mRecyclerView=findViewById(R.id.Send_To_RecyclerView);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        get_user_contacts=new Get_User_Contacts(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(new SendToContactsAdapters(this,new LetterComparator().sortList(get_user_contacts.getContactListContactsRecycleview())));

    }
}
