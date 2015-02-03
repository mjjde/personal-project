package com.mjj.ztapp.db;

import java.util.Date;
import java.util.Iterator;
import java.util.Set;

import android.content.ContentValues;

import com.mjj.ztapp.util.BeanUtils;

public class InsertProxy
{
    String table;

    ContentValues contentValues;

    public static InsertProxy insert(Object object)
    {
        InsertProxy insertProxy = new InsertProxy();
        Class<?> clazz = object.getClass();
        EntityInfo entityInfo = new EntityInfo(clazz);
        insertProxy.table = entityInfo.table;
        ContentValues values = new ContentValues();
        insertProxy.contentValues = values;
        Set<String> keys = entityInfo.columns.keySet();
        for (Iterator<String> iterator = keys.iterator(); iterator.hasNext();)
        {
            String key = iterator.next();
            if (key.equals(entityInfo.pk) && entityInfo.pkAuto)
            {
                continue;
            }
            Object value = BeanUtils.getValueFormObj(object, key);
            if (value != null)
            {
                if (value.getClass().equals(String.class))
                {
                    String v = (String) value;
                    values.put(entityInfo.columns.get(key), v);
                }
                if (value.getClass().equals(Integer.class)
                        || value.getClass().equals(int.class))
                {
                    Integer v = (Integer) value;
                    values.put(entityInfo.columns.get(key), v);
                }
                if (value.getClass().equals(Long.class)
                        || value.getClass().equals(long.class))
                {
                    Long v = (Long) value;
                    values.put(entityInfo.columns.get(key), v);
                }
                if (value.getClass().equals(Double.class)
                        || value.getClass().equals(double.class))
                {
                    Double v = (Double) value;
                    values.put(entityInfo.columns.get(key), v);
                }
                if (value.getClass().equals(Float.class)
                        || value.getClass().equals(float.class))
                {
                    Float v = (Float) value;
                    values.put(entityInfo.columns.get(key), v);
                }
                if (value.getClass().equals(Boolean.class)
                        || value.getClass().equals(boolean.class))
                {
                    Boolean v = (Boolean) value;
                    values.put(entityInfo.columns.get(key), v);
                }
                if (value.getClass().equals(Date.class))
                {
                    Date v = (Date) value;
                    values.put(entityInfo.columns.get(key), v.getTime());
                }
            }
        }
        return insertProxy;
    }
}
