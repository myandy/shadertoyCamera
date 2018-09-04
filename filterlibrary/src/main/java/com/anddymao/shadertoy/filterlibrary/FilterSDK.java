package com.anddymao.shadertoy.filterlibrary;

import android.content.Context;

public class FilterSDK {
    public static Context sContext;

    public static void init(Context context) {
        sContext = context;
    }

    private FilterSDK() {

    }
}
