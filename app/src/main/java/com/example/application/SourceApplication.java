package com.example.application;


import android.app.Application;
import android.content.Context;

/**
 * Created by jack on 2015/6/13.
 */
public class SourceApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        context=getApplicationContext();
    }

    public static Context getContext(){
        return context;
    }
}
