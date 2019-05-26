package com.developer.whocaller.net.Camera_Recognition_package;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;
import android.widget.Toast;


import com.developer.whocaller.net.R;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.Text;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.IOException;
import java.util.ArrayList;




public class Camera_Recognition extends AppCompatActivity  {

    private static final int MY_CAMERA_REQUEST_CODE = 109;
    private static final String TAG ="true" ;
    private static final String PUBLIC_STATIC_STRING_IDENTIFIER = "com.techvillage";
    CameraSource mCameraSource;
    TextView mTextView;
    private SurfaceView cameraView;
    StringBuilder strBuilder;
    ArrayList<String> intList = new ArrayList<>();


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
            // Check for low storage.  If there is low storage, the native library will not be
            // downloaded, so detection will not become operational.
            IntentFilter lowstorageFilter = new IntentFilter(Intent.ACTION_DEVICE_STORAGE_LOW);
            boolean hasLowStorage = registerReceiver(null, lowstorageFilter) != null;

            if (hasLowStorage) {
                Toast.makeText(this, R.string.low_storage_error, Toast.LENGTH_LONG).show();
                Log.w(TAG, getString(R.string.low_storage_error));
            }

        } else {

            //Initialize camerasource to use high resolution and set Autofocus on.
            mCameraSource = new CameraSource.Builder(getApplicationContext(), textRecognizer)
                    .setFacing(CameraSource.CAMERA_FACING_BACK)
                    .setRequestedPreviewSize(500, 1024)
                    .setAutoFocusEnabled(true)
                    .setRequestedFps(15.0f)
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
                    try {
                        final SparseArray<TextBlock> items = detections.getDetectedItems();
                        if (items.size() != 0) {

                            mTextView.post(new Runnable() {
                                @Override
                                public void run() {

                                    strBuilder = new StringBuilder();

                                    //TextBlock item2 = (TextBlock)items.valueAt(0);
                                    // The following Process is used to show how to use lines & elements as well
                                    for (int j = 0; j < items.size(); j++) {
                                        TextBlock textBlock = (TextBlock) items.valueAt(j);

                                        //for To Lines
                                        for (Text line : textBlock.getComponents()) {

                                            //extract scanned text of lines here
                                            for (int k = 0; k < line.getValue().length(); k++) {

                                                //Ensure That all Char is Number Only Not Character
                                                if (Character.isDigit(line.getValue().charAt(k))) {

                                                    strBuilder.append(line.getValue().charAt(k));

                                                }

                                            }

                                            //Don't Add num If Size smaller than 7 Digit
                                            if (strBuilder.length() > 7) {
                                                Log.v("lines", strBuilder.toString());
                                                intList.add(strBuilder.toString());
                                            }

                                            strBuilder.setLength(0);

                                        }
                                    }


                                    //if ArrayList Not 0 Close this activity and Display result
                                    if (intList.size() > 0) {
                                        Log.w("Send", "send");
                                        Intent resultIntent = new Intent();
                                        resultIntent.putStringArrayListExtra(PUBLIC_STATIC_STRING_IDENTIFIER, intList);
                                        setResult(Activity.RESULT_OK, resultIntent);
                                        finish();
                                    }

                                    //mTextView.setText(strBuilder.toString());
                                }
                            });
                        }


                }catch (Exception e){
                        e.printStackTrace();
                }

            }});
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
