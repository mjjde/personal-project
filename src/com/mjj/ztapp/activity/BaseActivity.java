package com.mjj.ztapp.activity;

import android.app.Activity;
import android.os.Bundle;

import com.mjj.ztapp.Const;
import com.mjj.ztapp.ioc.InjectUtil;
import com.mjj.ztapp.util.ActManager;

/**
 * 
 * @ClassName: BaseActivity
 * @Description: TODO(activity�Ļ��� �̳�֮)
 * @author meijianjian
 * @date 2015��1��19�� ����11:29:30
 * @version 1.0
 */

public class BaseActivity extends Activity
{
    ActManager actManager = ActManager.getAppManager();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        actManager.addActivity(this);// �������ջ
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        actManager.finishActivity(this); // ����ջ�е�act����
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
            InjectUtil.inject(this); // �ڵ���setContentView(int id)��ʼע��
        }

    }

    public void exit()
    {
        actManager.AppExit(this);
    }

}
