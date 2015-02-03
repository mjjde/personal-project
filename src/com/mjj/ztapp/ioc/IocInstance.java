package com.mjj.ztapp.ioc;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import android.content.Context;

import com.mjj.ztapp.ioc.annotation.FieldsInjectable;

/**
 * 
 * @ClassName: IocInstance
 * @Description: TODO(Ioc实例对象)
 * @author meijianjian
 * @date 2015年1月19日 上午11:58:21
 * @version 1.0
 */
public class IocInstance
{
    /**
     * 
     * @ClassName: InstanceScope
     * @Description: TODO(IocInstance创建的类型)
     * @author meijianjian
     * @date 2015年1月19日 下午12:01:18
     * @version 1.0
     */
    public enum InstanceScope
    {
        // 应用中单例
        SCOPE_SINGLETON,
        // 每次创建一个
        SCOPE_PROTOTYPE;
    }

    // 名字key
    public String name;

    // 实例对象集合
    Map<String, Object> objs;

    // 保存的对象
    public Object obj;

    // 绑定的对象
    public Class clazz;

    // 创建类型
    public InstanceScope scope;

    // 特殊构造器接口
    public OnCustomsConstructor cons;

    // 构造后初始化
    public onPraper praper;

    // 绑定到对象
    public Class toClazz;

    // 别名
    public AsAlians asAlians;

    public static Stack<FieldsInjectable> injected = new Stack<FieldsInjectable>();

    public void setPraper(onPraper praper)
    {
        this.praper = praper;
    }

    public void setCons(OnCustomsConstructor cons)
    {
        this.cons = cons;
    }

    public IocInstance(Class clazz)
    {
        this.clazz = clazz;
    }

    /**
     * 
     *
     * @Title: to
     * @Description: TODO(作为instanceByClazz的key)
     * @param @param clazz
     * @param @return 设定文件
     * @return IocInstance 返回类型
     * @throws
     */
    public IocInstance to(Class clazz)
    {
        this.toClazz = clazz;
        if (asAlians != null)
        {
            this.asAlians.as(this, null, clazz);
        }
        return this;
    }

    public IocInstance name(String name)
    {
        this.name = name;
        if (asAlians != null)
        {
            this.asAlians.as(this, name, null);
        }
        return this;
    }

    public IocInstance scope(InstanceScope scope)
    {
        this.scope = scope;
        return this;
    }

    public void perpare(onPraper perpare)
    {
        this.praper = perpare;
    }

    /**
     * 
     *
     * @Title: get
     * @Description: TODO(根据InstanceScope获取对象没有就创建)
     * @param @param context
     * @param @return 设定文件
     * @return Object 返回类型
     * @throws
     */
    public Object get(Context context)
    {
        if (scope == InstanceScope.SCOPE_SINGLETON)
        {
            if (obj == null)
            {
                obj = buildObject(context);
                injectChild(obj);
            }
        }
        else if (scope == InstanceScope.SCOPE_PROTOTYPE)
        {
            obj = buildObject(context);
            injectChild(obj);
        }
        return obj;
    }

    /**
     * 
     *
     * @Title: get
     * @Description: TODO(根据tag获取实例没有就创建)
     * @param @param context
     * @param @param tag
     * @param @return 设定文件
     * @return Object 返回类型
     * @throws
     */
    public Object get(Context context, String tag)
    {
        if (objs == null)
        {
            objs = new HashMap<String, Object>();
        }
        Object obj = objs.get(tag);
        if (obj == null)
        {
            obj = buildObject(context);
            objs.put(tag, obj);
            injectChild(obj);
        }
        return obj;
    }

    /**
     * 
     *
     * @Title: buildObject
     * @Description: TODO(构建对象优先context让进空构造)
     * @param @param context
     * @param @return 设定文件
     * @return Object 返回类型
     * @throws
     */
    @SuppressWarnings("rawtypes")
    public Object buildObject(Context context)
    {
        Object obj = null;
        Constructor custr = null;
        Constructor[] constructors = clazz.getDeclaredConstructors();
        if (context != null)
        {
            for (int i = 0; i < constructors.length; i++)
            {
                Class[] clz = constructors[i].getParameterTypes();
                if (clz != null && clz.length == 1
                        && clz[0].equals(Context.class))
                {
                    custr = constructors[i];
                }
            }

            try
            {
                if (custr != null)
                {
                    obj = custr.newInstance(context);
                }
                else
                {
                    obj = clazz.newInstance();
                }
            }
            catch(InstantiationException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            catch(IllegalAccessException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            catch(IllegalArgumentException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            catch(InvocationTargetException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            if (obj == null && cons != null)
            {
                obj = cons.getObj();
            }
            if (obj != null && praper != null)
            {
                praper.praper(obj);
            }
        }
        return obj;
    }

    /**
     *
     * @Title: injectChild
     * @Description: TODO(依赖对象注入)
     * @param @param obj 设定文件
     * @return void 返回类型
     * @throws
     */
    public void injectChild(Object obj)
    {
        if (obj instanceof FieldsInjectable)
        {
            FieldsInjectable fieldsInjectable = (FieldsInjectable) obj;
            injected.push(fieldsInjectable);
            InjectUtil.inject(fieldsInjectable);
            if (injected.get(0) == fieldsInjectable)
            {
                while (!injected.isEmpty())
                {
                    FieldsInjectable fieldsInject = injected.pop();
                    fieldsInject.injected();
                }
            }
        }
    }

    public void setAsAlians(AsAlians asAlians)
    {
        this.asAlians = asAlians;
    }

    public interface AsAlians
    {
        public void as(IocInstance instance, String name, Class toClazz);
    }

    public interface OnCustomsConstructor
    {
        Object getObj();
    }

    public interface onPraper
    {
        void praper(Object obj);
    }
}
