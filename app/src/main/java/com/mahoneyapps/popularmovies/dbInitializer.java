package com.mahoneyapps.popularmovies;

import android.app.Application;

import com.orm.SugarContext;

/**
 * Created by Brendan on 5/5/2016.
 */
public class dbInitializer extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // Initialize Sugar
        SugarContext.init(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        // Terminate Sugar
        SugarContext.terminate();
    }
}
