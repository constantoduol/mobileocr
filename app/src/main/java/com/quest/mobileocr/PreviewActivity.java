package com.quest.mobileocr;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;


public class PreviewActivity extends Activity {

    private Camera mCamera;

    private CameraPreview mPreview;


    private FrameLayout preview;

    private SensorManager mSensorManager;

    private Sensor mAccelerometer;

    private Accelerometer meter;

    private final int CAMERA_FREQUENCY = 300;

    private final int FUTURE_PICTURE_DELAY = 3000;

    private WebView infoView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);

        // Create an instance of Camera
        mCamera = getCameraInstance();
        setCameraDisplayOrientation(this,Camera.CameraInfo.CAMERA_FACING_BACK,mCamera);
        // Create our Preview view and set it as the content of our activity.
        mPreview = new CameraPreview(this, mCamera);
        preview = (FrameLayout) findViewById(R.id.camera_preview);

        infoView = (WebView) findViewById(R.id.info_view);
        infoView.setBackgroundColor(0x80000000);
        preview.addView(mPreview);
        takeFuturePicture();

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        meter = new Accelerometer();
        mSensorManager.registerListener(meter, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);//prevent screen from dimming
    }


    public static void setCameraDisplayOrientation(Activity activity,
                                                   int cameraId, android.hardware.Camera camera) {
        android.hardware.Camera.CameraInfo info =
                new android.hardware.Camera.CameraInfo();
        android.hardware.Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay()
                .getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0: degrees = 0; break;
            case Surface.ROTATION_90: degrees = 90; break;
            case Surface.ROTATION_180: degrees = 180; break;
            case Surface.ROTATION_270: degrees = 270; break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        camera.setDisplayOrientation(result);
    }



    private void takeFuturePicture() {
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                //dont take pictures when we are moving
                //take pictures only when we are still
                if (mPreview.getSafeToTakePicture() && meter.isStill()) {
                    mCamera.takePicture(null, null, mPicture);
                    mPreview.setSafeToTakePicture(false);
                }
            }
        };
        timer.schedule(task,FUTURE_PICTURE_DELAY, CAMERA_FREQUENCY);
    }

    /**
     * A safe way to get an instance of the Camera object.
     */
    public static Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        } catch (Exception e) {
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }

    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            String resp = MainActivity.getInstance().getPlugin().recogniseText(bitmap);
            Log.i("apppp",resp);
            addText(resp);
            //FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(600, 200);
            //put the text in the webview
            camera.startPreview(); //start preview to take the next picture

            mPreview.setSafeToTakePicture(true);

        }

    };

    private void addText(String text){
        infoView.loadData(text,"text/html",null);
    }


}
