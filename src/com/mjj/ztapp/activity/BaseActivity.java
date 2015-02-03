package com.mjj.ztapp.activity;

import android.app.Activity;
import android.os.Bundle;

import com.mjj.ztapp.Const;
import com.mjj.ztapp.ioc.InjectUtil;
import com.mjj.ztapp.util.ActManager;

/**
 * 
 * @ClassName: BaseActivity
 * @Description: TODO(activity的基类 继承之)
 * @author meijianjian
 * @date 2015年1月19日 上午11:29:30
 * @version 1.0
 */

public class BaseActivity extends Activity
{
    ActManager actManager = ActManager.getAppManager();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        actManager.addActivity(this);// 加入管理栈
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        actManager.finishActivity(this); // 管理栈中的act弹出
    }

    @Override
    protected void onPause()
    {
        super.onPause();
    }

    @Override
    protected void onRestart()
    {
        super.onRestart();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
    }

    @Override
    protected void onStart()
    {
        // TODO Auto-generated method stub
        super.onStart();
    }

    @Override
    protected void onStop()
    {
        // TODO Auto-generated method stub
        super.onStop();
    }

    @Override
    public void setContentView(int layoutResID)
    {
        super.setContentView(layoutResID);
        if (Const.auto_inject)
        {
            InjectUtil.inject(this); // 在调用setContentView(int id)后开始注入
        }

    }

    public void exit()
    {
        actManager.AppExit(this);
    }

}
