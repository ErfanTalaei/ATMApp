package org.bihe.finalproject.data;

import android.app.Application;

public class AppData extends Application {

    private static AppData appData;

    public static AppData getInstance() {
        return appData;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        appData = this;
    }
}
