package com.mjj.ztapp.util;

import java.lang.reflect.Field;

public class BeanUtils
{
    public static Field getDeclaredField(Class clazz, String name)
    {
        try
        {
            Field field = clazz.getDeclaredField(name);
            return field;
        }
        catch(NoSuchFieldException e)
        {
            e.printStackTrace();
        }
        return null;

    }

    public static Object getValueFormObj(Object object, String name)
    {
        Field field = getDeclaredField(object.getClass(), name);
        try
        {
            Object o = field.get(object);
            return o;
        }
        catch(IllegalAccessException e)
        {
            e.printStackTrace();
        }
        catch(IllegalArgumentException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public static void setValueToObj(Object object, String name, Object value)
    {
        try
        {
            Field field = object.getClass().getDeclaredField(name);
            field.setAccessible(true);
            field.set(object, value);
        }
        catch(NoSuchFieldException e)
        {
            e.printStackTrace();
        }
        catch(IllegalAccessException e)
        {
            e.printStackTrace();
        }
        catch(IllegalArgumentException e)
        {
            e.printStackTrace();
        }

    }
}
