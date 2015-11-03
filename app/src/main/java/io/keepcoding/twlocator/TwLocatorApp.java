package io.keepcoding.twlocator;

import android.app.Application;
import android.content.Context;

import java.lang.ref.WeakReference;

public class TwLocatorApp extends Application{

    private static WeakReference<Context> sContext;


    @Override
    public void onCreate() {
        super.onCreate();

        sContext = new WeakReference<Context>(getApplicationContext());
    }

    public static Context getAppContext(){

        return sContext.get();

    }
}
