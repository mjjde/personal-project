package com.mjj.ztapp.util;

import java.util.Stack;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
/**
 * 
* @ClassName: ActManager 
* @Description: TODO(Activity�Ĺ����࣬����activity�Ĳ���������) 
* @author meijianjian 
* @date 2015��1��19�� ����11:36:11 
* @version 1.0
 */
public class ActManager
{

    
    private static Stack<Activity> activityStack; 
    private static ActManager instance; 
     
    private ActManager(){} 
    /**
     * ��һʵ��
     */ 
    public static ActManager getAppManager(){ 
        if(instance==null){ 
            instance=new ActManager(); 
        } 
        return instance; 
    } 
    /**
     * ���Activity����ջ
     */ 
    public void addActivity(Activity activity){ 
        if(activityStack==null){ 
            activityStack=new Stack<Activity>(); 
        } 
        activityStack.add(activity); 
    } 
    /**
     * ��ȡ��ǰActivity����ջ�����һ��ѹ��ģ�
     */ 
    public Activity currentActivity(){ 
        Activity activity=activityStack.lastElement(); 
        return activity; 
    } 
    /**
     * ������ǰActivity����ջ�����һ��ѹ��ģ�
     */ 
    public void finishActivity(){ 
        Activity activity=activityStack.lastElement(); 
        if(activity!=null){ 
            activity.finish(); 
            activity=null; 
        } 
    } 
    /**
     * ����ָ����Activity
     */ 
    public void finishActivity(Activity activity){ 
        if(activity!=null){ 
            activityStack.remove(activity); 
            activity.finish(); 
            activity=null; 
        } 
    } 
    /**
     * ����ָ��������Activity
     */ 
    public void finishActivity(Class<?> cls){ 
        for (Activity activity : activityStack) { 
            if(activity.getClass().equals(cls) ){ 
                finishActivity(activity); 
            } 
        } 
    } 
    /**
     * ��������Activity
     */ 
    public void finishAllActivity(){ 
        for (int i = 0, size = activityStack.size(); i < size; i++){ 
            if (null != activityStack.get(i)){ 
                activityStack.get(i).finish(); 
            } 
        } 
        activityStack.clear(); 
    } 
    /**
     * �˳�Ӧ�ó���
     */ 
    public void AppExit(Context context) { 
        try { 
            finishAllActivity(); 
            ActivityManager activityMgr= (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE); 
            activityMgr.restartPackage(context.getPackageName()); 
            System.exit(0); 
        } catch (Exception e) { } 
    } 
}
