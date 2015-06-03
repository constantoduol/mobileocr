package com.quest.mobileocr;

import android.util.Log;
import android.webkit.JavascriptInterface;

import java.io.File;

/**
 * Created by Connie on 07-Dec-14.
 */
public class JavascriptExtensions {
    @JavascriptInterface
    public void detect(){
        MainActivity.getInstance().startCameraPreview();
    }

    @JavascriptInterface
    public void execute(){
        MainActivity.getInstance().executeDelayedCalls();
    }

}
