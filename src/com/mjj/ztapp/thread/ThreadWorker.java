package com.mjj.ztapp.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import android.app.Dialog;

import com.mjj.ztapp.Const;
import com.mjj.ztapp.dialog.IDialog;
import com.mjj.ztapp.ioc.IocContext;

/**
 * 
 * @ClassName: ThreadWorker
 * @Description: TODO(线程池，Task各步骤的执行)
 * @author meijianjian
 * @date 2015年1月29日 下午5:42:24
 * @version 1.0
 */
public class ThreadWorker
{
    static ExecutorService executorService;

    public static Future<?> executeRunalle(Runnable runnable)
    {
        if (executorService == null)
        {
            executorService = Executors.newFixedThreadPool(Const.net_pool_size);
        }
        return executorService.submit(runnable);
    }

    public static Future<?> execute(boolean dialog, final Task task)
    {
        if (dialog)
        {
            IDialog Idialog = IocContext.getContext().get(IDialog.class);
            Dialog d = Idialog.showProgressDialog(task.context);
            d.setCancelable(false);
            task.dialog = d;
        }
        Future<?> future = executeRunalle(new Runnable()
        {

            @Override
            public void run()
            {
                try
                {
                    task.doInBackground();
                }
                catch(Exception e)
                {
                    task.transfer(null, Task.DOINERROR);
                }
                task.transfer(null, Task.DOINUI);
            }
        });
        return future;
    }
}
