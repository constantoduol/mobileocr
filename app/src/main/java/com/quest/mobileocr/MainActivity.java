package com.quest.mobileocr;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.SurfaceView;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;


public class MainActivity extends Activity{

    private static WebView wv = null;

    private static AbbyyPlugin plugin;

    private static MainActivity activity;

    private boolean safeToTakePicture = false;

    private int REQUEST_IMAGE_CAPTURE = 3;

    private ImageView view;

    private ArrayList<String> delayedCalls;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        wv = (WebView) findViewById(R.id.html_viewer);
        WebSettings settings = wv.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setDatabasePath("/data/data/" + wv.getContext().getPackageName() + "/databases/");
        WebChromeClient webChrome = new WebChromeClient() {
            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                Log.i("JCONSOLE", consoleMessage.lineNumber()
                        + ": " + consoleMessage.message());
                return true;
            }
        };
        wv.addJavascriptInterface(new JavascriptExtensions(), "jse");
        wv.setWebChromeClient(webChrome);
        wv.loadUrl("file:///android_asset/index.html");
        plugin = new AbbyyPlugin(this);
        activity = this;
        delayedCalls = new ArrayList();
    }


    public static MainActivity getInstance(){
        return activity;
    }

    public AbbyyPlugin getPlugin(){
        return plugin;
    }

    public void startCameraPreview(){
        Intent intent = new Intent(this,PreviewActivity.class);
        startActivity(intent);
    }

//    public void takePhoto(){
//        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
//            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
//        }
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
//            try {
//                Bundle extras = data.getExtras();
//                Bitmap imageBitmap = (Bitmap) extras.get("data");
//                //File image = new File(plugin.getFileStorage(), "img.jpg");
//                //image.createNewFile();
//                //FileOutputStream out = new FileOutputStream(image);
//                //imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
//                //String finalPath = "file://"+image.getAbsolutePath();
//                String resp = plugin.recogniseText(imageBitmap);
//                resp = resp.replaceAll("[^\\w\\s]","");
//                wv.loadUrl("javascript:callback('"+resp+"')");
//                delayedCall("javascript:callback('"+resp+"')"); //this was added because the page takes time
//                //wv.loadUrl("javascript:test()");
//                //to load and sometimes the javascript has not loaded
//                Log.i("OCR", resp);
//            }
//            catch(Exception e){
//               e.printStackTrace();
//            }
//        }
//    }







    public void delayedCall(String js){
        //first of all check whether the document has loaded
        //if loaded execute the call
        //if it has not loaded schedule a delayed call
        delayedCalls.add(js);

    }



    public void executeDelayedCalls(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for(String js : delayedCalls){
                    wv.loadUrl(js);
                }
                delayedCalls = new ArrayList<String>();
            }
        });

    }


}



