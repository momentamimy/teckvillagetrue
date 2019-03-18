package teckvillage.developer.khaled_pc.teckvillagetrue;

import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;





public class Camera_Recognition extends AppCompatActivity {

    private static final int MY_CAMERA_REQUEST_CODE = 109;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera__recognition);

        /*

        ScannerView scanner = findViewById(R.id.scanner);
        scanner.setOnDetectedListener(this, new ScannerListener() {
            @Override
            public void onDetected(String detections) {
                Toast.makeText(Camera_Recognition.this, detections, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStateChanged(String state, int i) {
                Log.d("state", state);
            }
        });*/
    }
}
