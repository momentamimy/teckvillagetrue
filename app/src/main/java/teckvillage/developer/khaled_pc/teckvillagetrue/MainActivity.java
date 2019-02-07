package teckvillage.developer.khaled_pc.teckvillagetrue;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import teckvillage.developer.khaled_pc.teckvillagetrue.Controller.ContactAdapter;
import teckvillage.developer.khaled_pc.teckvillagetrue.Controller.LogAdapter;
import teckvillage.developer.khaled_pc.teckvillagetrue.R;
import teckvillage.developer.khaled_pc.teckvillagetrue.model.ContactInfo;
import teckvillage.developer.khaled_pc.teckvillagetrue.model.LogInfo;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    RecyclerView contacts,logs;
    LinearLayoutManager lLayout;
    LinearLayoutManager lLayout1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        contacts=findViewById(R.id.contact_recycleview);
        logs=findViewById(R.id.Logs_recycleview);

        lLayout = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false);
        lLayout1 = new LinearLayoutManager(this);

        List<ContactInfo> contactInfos=new ArrayList<>();

        contactInfos.add(new ContactInfo("http://i.imgur.com/DvpvklR.png","khaled","Mobile"));
        contactInfos.add(new ContactInfo("https://firebasestorage.googleapis.com/v0/b/chatapp-f5386.appspot.com/o/imgs%2F3C8oCUSQjlZqV0kivIBhpwT5fca2.jpg?alt=media&token=a9e39edd-6d27-4995-969d-a76ddbfb1aab","khaled","Mobile"));
        contactInfos.add(new ContactInfo(null,"khaled","Mobile"));
        contactInfos.add(new ContactInfo(null,"khaled eltarabily","Mobile"));
        contactInfos.add(new ContactInfo(null,"khaled","Mobile"));
        contactInfos.add(new ContactInfo(null,"khaled","Mobile"));
        contactInfos.add(new ContactInfo(null,"khaled","Mobile"));

        contacts.setLayoutManager(lLayout);
        contacts.setItemAnimator(new DefaultItemAnimator());
        ContactAdapter adapter=new ContactAdapter(this,contactInfos);
        contacts.setAdapter(adapter);



        List<LogInfo> logInfos=new ArrayList<>();
        logInfos.add(new LogInfo("http://i.imgur.com/DvpvklR.png","khaled","income",1235465464,"Mobile"));
        logInfos.add(new LogInfo("https://firebasestorage.googleapis.com/v0/b/chatapp-f5386.appspot.com/o/imgs%2F3C8oCUSQjlZqV0kivIBhpwT5fca2.jpg?alt=media&token=a9e39edd-6d27-4995-969d-a76ddbfb1aab","khaled","income",1235465464,"Mobile"));
        logInfos.add(new LogInfo(null,"khaled","income",1235465464,"Mobile"));
        logInfos.add(new LogInfo(null,"momen","outcome",1235465464,"Mobile"));
        logInfos.add(new LogInfo(null,"khaled","income",1235465464,"Mobile"));
        logInfos.add(new LogInfo(null,"momen","outcome",1235465464,"Mobile"));

        logs.setLayoutManager(lLayout1);
        logs.setItemAnimator(new DefaultItemAnimator());
        LogAdapter adapter1=new LogAdapter(this,logInfos);
        logs.setAdapter(adapter1);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void getkeyhash() {

        try {
            PackageInfo info=getPackageManager().getPackageInfo("teckvillage.developer.khaled_pc.teckvillagetrue", PackageManager.GET_SIGNATURES);
            for(Signature signature:info.signatures){
                MessageDigest md=MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.w("KEYHASH", Base64.encodeToString(md.digest(),Base64.DEFAULT));
            }
        }catch (Exception e){

        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
