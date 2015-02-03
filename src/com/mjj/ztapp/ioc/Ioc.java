package com.mjj.ztapp.ioc;

import android.app.Application;
import android.content.Context;
/**
 * 
* @ClassName: Ioc 
* @Description: TODO(IocContext类的封装) 
* @author meijianjian 
* @date 2015年1月29日 下午4:07:03 
* @version 1.0
 */
public class Ioc
{

    public static void initApplication(Application application)
    {
        IocContext.getContext().initApplication(application);
    }

    public static <T extends Application> T getApplication()
    {
        return IocContext.getContext().getApplication();
    }

    public static Context getApplicationContext()
    {
        return IocContext.getContext().getApplicationContext();
    }

    public static <T> T get(String name)
    {
        return IocContext.getContext().get(name);
    }

    public static <T> T get(Class<T> clazz)
    {
        return IocContext.getContext().get(clazz);
    }

    public static <T> T get(Class<T> clazz, String tag)
    {
        return IocContext.getContext().get(clazz, tag);
    }

    public static IocInstance bind(Class clazz)
    {
        return IocContext.getContext().bind(clazz);
    }

}
