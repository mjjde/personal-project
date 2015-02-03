package com.mjj.ztapp.ioc;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

/**
 * 
 * @ClassName: EventListener
 * @Description: TODO(事件的回调，目前实现了最常用的单击和长按事件，支持OnClickListener() and
 *               OnClickListener(View view))
 * @author meijianjian
 * @date 2015年1月29日 下午3:55:34
 * @version 1.0
 */
public class EventListener implements OnClickListener, OnLongClickListener,
        OnItemClickListener, OnItemLongClickListener
{

    private Object handler;

    private String clickMethod;

    private String longClickMethod;

    private String itemClickMethod;

    private String itemLongClickMehtod;

    public EventListener(Object handler)
    {
        this.handler = handler;
    }

    public EventListener click(String clickMethod)
    {
        this.clickMethod = clickMethod;
        return this;
    }

    public EventListener longClick(String longClickMethod)
    {
        this.longClickMethod = longClickMethod;
        return this;
    }

    public EventListener itemClick(String itemClickMethod)
    {
        this.itemClickMethod = itemClickMethod;
        return this;
    }

    public EventListener itemLongClick(String itemLongClickMehtod)
    {
        this.itemLongClickMehtod = itemLongClickMehtod;
        return this;
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2,
            long arg3)
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean onLongClick(View arg0)
    {
        return invokeLongClick(handler, longClickMethod, arg0);
    }

    @Override
    public void onClick(View arg0)
    {
        invokeClick(handler, clickMethod, arg0);
    }

    private long lastClickTime = 0;

    public void invokeClick(Object obj, String clickMethod, Object... params)
    {
        if (obj == null)
            return;
        Method method = null;
        try
        {
            if (System.currentTimeMillis() - lastClickTime > 300)
            {
                method = obj.getClass().getDeclaredMethod(clickMethod);
                method.invoke(obj);
                lastClickTime = System.currentTimeMillis();
                return;

            }
        }
        catch(NoSuchMethodException e)
        {
            try
            {
                method = obj.getClass().getDeclaredMethod(clickMethod,
                        View.class);
                method.invoke(obj, params);
                lastClickTime = System.currentTimeMillis();
                return;
            }
            catch(NoSuchMethodException e1)
            {
                e1.printStackTrace();
            }
            catch(IllegalAccessException e1)
            {
                e1.printStackTrace();
            }
            catch(IllegalArgumentException e1)
            {
                e1.printStackTrace();
            }
            catch(InvocationTargetException e1)
            {
                e1.printStackTrace();
            }
        }
        catch(IllegalAccessException e)
        {
            e.printStackTrace();
        }
        catch(IllegalArgumentException e)
        {
            e.printStackTrace();
        }
        catch(InvocationTargetException e)
        {
            e.printStackTrace();
        }

    }

    public boolean invokeLongClick(Object obj, String longClickMethod,
            Object... params)
    {
        Method method = null;
        if (obj == null)
            return false;
        try
        {
            method = obj.getClass().getDeclaredMethod(longClickMethod);
            Object o = method.invoke(obj);
            return o == null ? false : Boolean.valueOf(obj.toString());
        }
        catch(NoSuchMethodException e)
        {
            try
            {
                method = obj.getClass().getDeclaredMethod(longClickMethod,
                        View.class);
                Object o = method.invoke(obj, params);
                return o == null ? false : Boolean.valueOf(obj.toString());
            }
            catch(NoSuchMethodException e1)
            {
                e1.printStackTrace();
            }
            catch(IllegalAccessException e1)
            {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            catch(IllegalArgumentException e1)
            {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            catch(InvocationTargetException e1)
            {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
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
        return false;

    }

}
