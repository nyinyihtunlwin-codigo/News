package com.nyinyihtunlwin.news.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class AppUtils {

    private static AppUtils sObjInstance;
    private Context mContext;

    private AppUtils(Context context) {
        this.mContext = context;
    }

    public static void initAppUtils(Context context) {
        sObjInstance = new AppUtils(context);
    }

    public static AppUtils getObjInstance() {
        return sObjInstance;
    }


    public boolean hasConnection() {
        ConnectivityManager cm =
                (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        return isConnected;
    }
}
