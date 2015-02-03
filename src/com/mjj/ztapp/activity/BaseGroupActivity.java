package com.mjj.ztapp.activity;

import com.mjj.ztapp.util.ActManager;

import android.app.Activity;
import android.app.ActivityGroup;
import android.os.Bundle;

public class BaseGroupActivity extends ActivityGroup
{
    ActManager actManager =ActManager.getAppManager();
    @Override
    public Activity getCurrentActivity()
    {
        // TODO Auto-generated method stub
        return super.getCurrentActivity();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        actManager.addActivity(this);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        actManager.finishActivity(this);
    }

    @Override
    protected void onPause()
    {
        // TODO Auto-generated method stub
        super.onPause();
    }

    @Override
    protected void onResume()
    {
        // TODO Auto-generated method stub
        super.onResume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        // TODO Auto-generated method stub
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onStop()
    {
        // TODO Auto-generated method stub
        super.onStop();
    }

    public void exit()
    {
        actManager.AppExit(this);
    }

}
