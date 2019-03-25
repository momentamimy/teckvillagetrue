package teckvillage.developer.khaled_pc.teckvillagetrue;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.hardware.Camera;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;


public class Camera_Recognition extends AppCompatActivity  {

    private static final int MY_CAMERA_REQUEST_CODE = 109;
    private static final String TAG ="true" ;
    private static final String PUBLIC_STATIC_STRING_IDENTIFIER = "com.techvillage";
    CameraSource mCameraSource;
    TextView mTextView;
    private SurfaceView cameraView;
    StringBuilder stringBuilder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera__recognition);

        mTextView=findViewById(R.id.text_view);
        cameraView = findViewById(R.id.surfaceViewCamera);
        startCameraSource();



    }

    private void startCameraSource() {

        //Create the TextRecognizer
        final TextRecognizer textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();

        if (!textRecognizer.isOperational()) {
            Log.w(TAG, "Detector dependencies not loaded yet");
        } else {

            //Initialize camerasource to use high resolution and set Autofocus on.
            mCameraSource = new CameraSource.Builder(getApplicationContext(), textRecognizer)
                    .setFacing(CameraSource.CAMERA_FACING_BACK)
                    .setRequestedPreviewSize(550, 360)
                    .setAutoFocusEnabled(true)
                    .setRequestedFps(10.0f)
                    .build();

            /**
             * Add call back to SurfaceView and check if camera permission is granted.
             * If permission is granted we can start our cameraSource and pass it to surfaceView
             */
            cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(SurfaceHolder holder) {
                    try {

                        if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                            ActivityCompat.requestPermissions(Camera_Recognition.this,
                                    new String[]{android.Manifest.permission.CAMERA},
                                    MY_CAMERA_REQUEST_CODE);
                            return;
                        }
                        mCameraSource.start(cameraView.getHolder());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                }



                /**
                 * Release resources for cameraSource
                 */
                @Override
                public void surfaceDestroyed(SurfaceHolder holder) {
                    mCameraSource.stop();
                }
            });

            //Set the TextRecognizer's Processor.
            textRecognizer.setProcessor(new Detector.Processor<TextBlock>() {
                @Override
                public void release() {
                }




                /**
                 * Detect all the text from camera using TextBlock and the values into a stringBuilder
                 * which will then be set to the textView.
                 * */
                @Override
                public void receiveDetections(Detector.Detections<TextBlock> detections) {
                    final SparseArray<TextBlock> items = detections.getDetectedItems();
                    if (items.size() != 0 ){

                        mTextView.post(new Runnable() {
                            @Override
                            public void run() {
                                 stringBuilder = new StringBuilder();

                                TextBlock item = items.valueAt(0);
                                for (int j = 0; j < item.getValue().length(); j++) {

                                    if (Character.isDigit(item.getValue().charAt(j))){

                                        stringBuilder.append(item.getValue().charAt(j));

                                    }

                                }
                                if(stringBuilder.toString().length()>3){
                                    Intent resultIntent = new Intent();
                                    resultIntent.putExtra(PUBLIC_STATIC_STRING_IDENTIFIER, stringBuilder.toString());
                                    setResult(Activity.RESULT_OK, resultIntent);
                                    finish();
                                }

                                mTextView.setText(stringBuilder.toString());
                            }
                        });
                    }
                }
            });
        }


    }



    /**
     * Take care of popping the fragment back stack or finishing the activity
     * as appropriate.
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();

    }




}
