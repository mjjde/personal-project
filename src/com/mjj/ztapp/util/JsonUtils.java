package com.mjj.ztapp.util;

import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

public class JsonUtils
{
    public static void put(JSONObject jo, String name, String value)
    {
        if (jo == null || TextUtils.isEmpty(name))
            return;
        try
        {
            jo.put(name, value);
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }
    }
}
