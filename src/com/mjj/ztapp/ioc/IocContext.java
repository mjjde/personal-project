package com.mjj.ztapp.ioc;

import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Application;
import android.app.NotificationManager;
import android.content.Context;
import android.content.res.AssetManager;

import com.mjj.ztapp.ioc.IocInstance.AsAlians;
import com.mjj.ztapp.ioc.IocInstance.InstanceScope;

/**
 * 
 * @ClassName: IocContext
 * @Description: TODO(注入对象上下文)
 * @author meijianjian
 * @date 2015年1月29日 下午4:07:43
 * @version 1.0
 */
public class IocContext
{
    static IocContext iocContext;

    Application application;

    // 托管的实例 byName
    Map<String, IocInstance> instanceByName;

    // 托管的实例 byClazz
    Map<Class, IocInstance> instanceByClazz;

    public IocContext()
    {
        if (iocContext == null)
        {
            iocContext = this;
        }
        this.instanceByClazz = new HashMap<Class, IocInstance>();
        this.instanceByName = new HashMap<String, IocInstance>();

    }

    public Context getApplicationContext()
    {
        return application.getApplicationContext();
    }

    /**
     * @Title: getContext
     * @Description: TODO(单例出IOC上下文)
     * @param @return 设定文件
     * @return IocContext 返回类型
     * @throws
     */
    public static IocContext getContext()
    {
        if (iocContext == null)
        {
            iocContext = new IocContext();
        }
        return iocContext;

    }

    public void initApplication(Application application)
    {
        this.application = application;
    }

    @SuppressWarnings("unchecked")
    public <T extends Application> T getApplication()
    {
        return (T) application;
    }

    /**
     * 
     *
     * @Title: get
     * @Description: TODO(根据name获取实例,需在Application配置)
     * @param @param name
     * @param @return 设定文件
     * @return T 返回类型
     * @throws
     */
    @SuppressWarnings("unchecked")
    public <T> T get(String name)
    {
        return (T) instanceByName.get(name).get(
                application.getApplicationContext());
    }

    /**
     * 
     *
     * @Title: get
     * @Description: TODO(根据clazz获取实例，可以获取System服务对象)
     * @param @param clazz
     * @param @return 设定文件
     * @return T 返回类型
     * @throws
     */
    @SuppressWarnings("unchecked")
    public <T> T get(Class clazz)
    {
        if (clazz == null)
            return null;
        T t = getSysService(clazz);
        if (t != null)
        {
            return t;
        }
        IocInstance instance = instanceByClazz.get(clazz);
        if (instance != null)
        {
            return (T) instance.get(getApplicationContext());
        }
        else if (!clazz.isInterface())
        {
            bind(clazz).to(clazz).scope(InstanceScope.SCOPE_SINGLETON);
            IocInstance iocInstance = instanceByClazz.get(clazz);
            if (iocInstance != null)
            {
                return (T) iocInstance.get(getApplicationContext());
            }
        }
        return null;
    }

    /**
     * 
     *
     * @Title: get
     * @Description: TODO(根据tag获取实例 没有就创建)
     * @param @param clazz
     * @param @param tag
     * @param @return 设定文件
     * @return T 返回类型
     * @throws
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public <T> T get(Class clazz, String tag)
    {
        IocInstance instance = instanceByClazz.get(clazz);
        if (instance != null)
        {
            return (T) instance.get(getApplicationContext(), tag);
        }
        else
        {
            bind(clazz).to(clazz).scope(InstanceScope.SCOPE_SINGLETON);
            IocInstance iocInstance = instanceByClazz.get(clazz);
            return (T) iocInstance.get(getApplicationContext(), tag);
        }
    }

    // 配置 IocInstance
    @SuppressWarnings("rawtypes")
    public IocInstance bind(Class clazz)
    {
        IocInstance instance = new IocInstance(clazz);
        instance.setAsAlians(new AsAlians()
        {

            @Override
            public void as(IocInstance instance, String name, Class clazz)
            {
                if (name != null)
                {
                    if (instanceByName.containsKey(name))
                    {
                        instanceByName.remove(name);
                    }
                    instanceByName.put(name, instance);
                }
                if (clazz != null)
                {
                    if (instanceByClazz.containsKey(clazz))
                    {
                        instanceByClazz.remove(clazz);
                    }
                    instanceByClazz.put(clazz, instance);
                }
            }
        });
        return instance;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @SuppressLint("ServiceCast")
    public <T> T getSysService(Class clazz)
    {
        T t = null;
        if (clazz == NotificationManager.class)
        {
            t = (T) getApplicationContext().getSystemService(
                    Context.NOTIFICATION_SERVICE);
        }
        if (clazz == ActivityManager.class)
        {
            t = (T) getApplicationContext().getSystemService(
                    Context.ACTIVITY_SERVICE);
        }
        if (clazz == Package.class)
        {
            t = (T) getApplicationContext().getPackageManager();
        }
        if (clazz == AssetManager.class)
        {
            t = (T) getApplicationContext().getAssets();
        }
        return t;
    }
}
