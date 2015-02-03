package com.mjj.ztapp.thread;

import java.util.HashMap;
import java.util.Map;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
/**
 * 
* @ClassName: Task 
* @Description: TODO(与线程绑定的任务类有doInUI，doInError，doInBackground三种处理方法) 
* @author meijianjian 
* @date 2015年1月29日 下午5:40:38 
* @version 1.0
 */
public abstract class Task
{
    public static final int DOINUI = -1000;

    public static final int DOINERROR = -2000;

    public static final int DOINBG = -3000;

    public Dialog dialog;

    Context context;

    public Task(Context context)
    {
        super();
        this.context = context;
    }

    public void doInBackground()
    {

    }

    public void doInError(Object obj, Integer what)
    {

    }

    public abstract void doInUI(Object obj, Integer what);

    static Handler handler = new Handler()
    {
        @SuppressWarnings("unchecked")
        public void handleMessage(android.os.Message msg)
        {
            HashMap<String, Object> map = (HashMap<String, Object>) msg.obj;
            Task task = (Task) map.get("task");
            Object obj = map.get("obj");
            if (msg.what == DOINBG)
            {
                if (task.dialog != null)
                {
                    task.dialog.dismiss();
                    task.dialog = null;
                }
                task.doInBackground();
            }
            else if (msg.what == DOINERROR)
            {
                if (task.dialog != null)
                {
                    task.dialog.dismiss();
                    task.dialog = null;
                }
                task.doInError(obj, msg.what);
            }
            else if (msg.what == DOINUI)
            {
                task.doInUI(obj, msg.what);
            }
        };
    };

    public void transfer(Object obj, Integer what)
    {
        Message message = handler.obtainMessage();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("task", this);
        map.put("obj", obj);
        message.obj = map;
        message.what = what;
        handler.sendMessage(message);
    }

}
