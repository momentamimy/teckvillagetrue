package teckvillage.developer.khaled_pc.teckvillagetrue;

import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG ="fields" ;

    public static FragmentManager fragmentManager;
    FrameLayout frameLayout;
    private static final String TAG_ANDROID_CONTACTS = "ANDROID_CONTACTS";

    public BottomNavigationView navigationView2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        navigationView2=findViewById(R.id.botnav);


        frameLayout=(FrameLayout) findViewById(R.id.fragment_container_main);//connect Framelayout




        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //****************************************************Set HomeFragment as default*********************************************************
        navigationView2.setSelectedItemId(R.id.nav_phone);
        fragmentManager=getSupportFragmentManager();
        final FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        if(findViewById(R.id.fragment_container_main) !=null){
            if(savedInstanceState !=null){
                return;
            }
            Main_Fagment fragment_1=new Main_Fagment();
            fragmentTransaction.add(R.id.fragment_container_main,fragment_1,null);
            fragmentTransaction.commit();
        }

        navigationView2.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                switch (item.getItemId())
                {
                    case R.id.nav_message:
                        selectedFragment = new Message_Fragment();
                            Fragment f = getSupportFragmentManager().findFragmentById(R.id.fragment_container_main);
                            if (f instanceof Message_Fragment) {
                                // Do something
                            }
                            else
                            {
                                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_main,
                                        selectedFragment).commit();

                            }
                        break;
                    case R.id.nav_phone:
                            selectedFragment=new Main_Fagment();
                            Fragment f1 = getSupportFragmentManager().findFragmentById(R.id.fragment_container_main);
                            if (f1 instanceof Main_Fagment) {
                                // Do something
                            }
                            else
                            {
                                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_main,
                                        selectedFragment).commit();
                            }
                        break;
                    case R.id.nav_contacts:
                        selectedFragment=new Contacts();
                            Fragment f2 = getSupportFragmentManager().findFragmentById(R.id.fragment_container_main);
                            if (f2 instanceof Contacts) {
                                // Do something
                               }
                            else
                            {
                                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_main,
                                        selectedFragment).commit();
                            }
                        break;
                }

                return true;
            }
        });

        }





    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            if (getFragmentManager().getBackStackEntryCount() != 0) {
                getFragmentManager().popBackStack();
            } else {

                super.onBackPressed();

            }

        }


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



    private void doSomethingForEachUniquePhoneNumber(Context context) {
        String[] projection = new String[] {
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER,
                ContactsContract.CommonDataKinds.Phone.TYPE,
                //plus any other properties you wish to query
        };

        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, projection, null, null, null);
        } catch (SecurityException e) {
            //SecurityException can be thrown if we don't have the right permissions
        }

        if (cursor != null) {
            try {
                HashSet<String> normalizedNumbersAlreadyFound = new HashSet<>();
                int indexOfNormalizedNumber = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER);
                int indexOfDisplayName = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                int indexOfDisplayNumber = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                int indexOfphoneType = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE);

                while (cursor.moveToNext()) {
                    String normalizedNumber = cursor.getString(indexOfNormalizedNumber);
                    if (normalizedNumbersAlreadyFound.add(normalizedNumber)) {
                        String displayName = cursor.getString(indexOfDisplayName);
                        String displayNumber = cursor.getString(indexOfDisplayNumber);
                        int  phoneType = cursor.getInt(indexOfphoneType);

                        //haven't seen this number yet: do something with this contact!
                        Log.i(TAG, "Name: " + displayName);
                        //Log.i(TAG, "Number: " + displayNumber);

                        switch (phoneType) {
                            case ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE:
                                Log.i(TAG, "Mobile Number: " + displayNumber);
                                break;
                            case ContactsContract.CommonDataKinds.Phone.TYPE_HOME:
                                Log.i(TAG, "Home Number: " + displayNumber);
                                break;
                            case ContactsContract.CommonDataKinds.Phone.TYPE_WORK:
                                Log.i(TAG, "Work Number: " + displayNumber);
                                break;
                            case ContactsContract.CommonDataKinds.Phone.TYPE_OTHER:
                                Log.i(TAG, "Other Number: " + displayNumber);
                                break;
                            default:
                                break;
                        }
                        Log.i(TAG, "------------------------------------------------------------------------------------------");


                    } else {
                        //don't do anything with this contact because we've already found this number
                    }
                }
            } finally {
                cursor.close();
            }
        }
    }




    // After user select Allow or Deny button in request runtime permission dialog
    // , this method will be invoked.
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case 1:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //granted

                    Toast.makeText(getApplicationContext(), "You allowed permission, please click the button again.", Toast.LENGTH_LONG).show();

                }
                else {

                    Toast.makeText(getApplicationContext(), "You must approve the permission", Toast.LENGTH_SHORT).show();
                }
                break;

            case 3:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //granted

                    Toast.makeText(getApplicationContext(), "You allowed permission, please click the button again.", Toast.LENGTH_LONG).show();

                }
                else {

                    Toast.makeText(getApplicationContext(), "You must approve the permission", Toast.LENGTH_SHORT).show();
                }
                break;

        }
    }




    // Return all raw_contacts _id in a list.
    private List<Integer> getRawContactsIdList()
    {
        List<Integer> ret = new ArrayList<Integer>();

        ContentResolver contentResolver = getContentResolver();

        // Row contacts content uri( access raw_contacts table. ).
        Uri rawContactUri = ContactsContract.RawContacts.CONTENT_URI;
        // Return _id column in contacts raw_contacts table.
        String queryColumnArr[] = {ContactsContract.RawContacts._ID};
        // Query raw_contacts table and return raw_contacts table _id.
        Cursor cursor = contentResolver.query(rawContactUri,queryColumnArr, null, null, null);
        if(cursor!=null)
        {
            cursor.moveToFirst();
            do{
                int idColumnIndex = cursor.getColumnIndex(ContactsContract.RawContacts._ID);
                int rawContactsId = cursor.getInt(idColumnIndex);
                ret.add(new Integer(rawContactsId));
            }while(cursor.moveToNext());
        }

        cursor.close();

        return ret;
    }

    public void openDrawer() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.openDrawer(Gravity.START);
    }

}
