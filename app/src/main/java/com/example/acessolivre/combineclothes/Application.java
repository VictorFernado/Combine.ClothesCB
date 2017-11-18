package com.example.acessolivre.combineclothes;

import android.content.Context;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by jandersonlemos on 17/11/17.
 */

public class Application extends android.app.Application {
    private static Application mInstance;
    private static Context mAppContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        this.setAppContext(getApplicationContext());
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }


    public static Application getInstance(){
        return mInstance;
    }
    public static Context getAppContext() {
        return mAppContext;
    }
    public void setAppContext(Context mAppContext) {
        this.mAppContext = mAppContext;
    }

}
