package teckvillage.developer.khaled_pc.teckvillagetrue;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

public class Notifications extends AppCompatActivity {

    RecyclerView NotificationRecyclerview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);



        getSupportActionBar().setTitle("Notification");
        getSupportActionBar().setDisplayShowHomeEnabled(true);   //back button on App Bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);   //back button on App Bar


        NotificationRecyclerview=findViewById(R.id.notificationRecyclerview);

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
