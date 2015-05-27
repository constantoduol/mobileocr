package com.quest.mobileocr;

import android.util.Log;
import android.webkit.JavascriptInterface;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Connie on 07-Dec-14.
 */
public class JavascriptExtensions {
    @JavascriptInterface
    public String detect(){
        File fileStorage = MainActivity.getInstance().getPlugin().getFileStorage();
        File imagePath = new File(fileStorage, "img.jpg");
        String finalPath = "file://"+imagePath.getAbsolutePath();
        Log.i("APP","Path : "+finalPath);
        String resp = MainActivity.getInstance().getPlugin().recogniseText(finalPath);
        Log.i("OCR",resp);
        return resp;
    }


}
