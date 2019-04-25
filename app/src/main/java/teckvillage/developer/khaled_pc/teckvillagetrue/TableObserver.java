package teckvillage.developer.khaled_pc.teckvillagetrue;

import android.accounts.Account;
import android.content.ContentResolver;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;


/**
 * Created by khaled-pc on 4/24/2019.
 */

public class TableObserver extends ContentObserver {



    /**
     * Creates a content observer.
     *
     * @param handler The handler to run {@link #onChange} on, or null if none.
     */
    public TableObserver(Handler handler) {
        super(handler);
    }

    public TableObserver() {
        super(null);
    }


    /*
         * Define a method that's called when data in the
         * observed content provider changes.
         * This method signature is provided for compatibility with
         * older platforms.
         */
    @Override
    public void onChange(boolean selfChange) {
            /*
             * Invoke the method signature available as of
             * Android platform version 4.1, with a null URI.
             */
        onChange(selfChange, null);
    }
    /*
     * Define a method that's called when data in the
     * observed content provider changes.
     */
    @Override
    public void onChange(boolean selfChange, Uri changeUri) {

        Log.w("ChangeHappenContacts","lol");

    }

}