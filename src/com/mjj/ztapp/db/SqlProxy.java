package com.mjj.ztapp.db;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mjj.ztapp.util.BeanUtils;

import android.text.TextUtils;

public class SqlProxy
{
    StringBuffer sb;

    List<Object> params;

    Class<?> clazz;

    private SqlProxy()
    {
        sb = new StringBuffer();
        params = new ArrayList<Object>();
    }

    public String getSql()
    {
        return sb.toString();
    }

    public String[] getParamsArr()
    {
        String[] paramsArr = new String[params.size()];
        for (int i = 0; i < params.size(); i++)
        {
            Object object = params.get(i);
            if (object != null)
            {
                if (object.getClass().equals(Date.class))
                {
                    Date date = (Date) object;
                    paramsArr[i] = date.getTime() + "";
                }
                else
                {
                    paramsArr[i] = object.toString();
                }
            }
        }
        return paramsArr;
    }

    public static SqlProxy update(Object object)
    {
        SqlProxy proxy = new SqlProxy();
        EntityInfo entityInfo = new EntityInfo(object.getClass());
        proxy.clazz = object.getClass();
        String pk = entityInfo.pk;
        if (TextUtils.isEmpty(pk))
        {
            throw new RuntimeException("主键不能为空");
        }
        proxy.sb.append("UPDATE " + entityInfo.table + " SET ");
        Set<String> keys = entityInfo.columns.keySet();
        for (Iterator<String> iterator = keys.iterator(); iterator.hasNext();)
        {
            String key = iterator.next();
            proxy.sb.append(entityInfo.columns.get(key)).append(" =? ,");
            proxy.params.add(BeanUtils.getValueFormObj(object, key));
        }
        proxy.sb.deleteCharAt(proxy.sb.length() - 1);
        proxy.sb.append(" WHERE ").append(entityInfo.columns.get(pk))
                .append(" =? ");
        proxy.params.add(BeanUtils.getValueFormObj(object, pk));
        return proxy;
    }

    public static SqlProxy delete(Object object)
    {
        SqlProxy proxy = new SqlProxy();
        EntityInfo entityInfo = new EntityInfo(object.getClass());
        proxy.clazz = object.getClass();
        String pk = entityInfo.pk;
        if (TextUtils.isEmpty(pk))
        {
            throw new RuntimeException("主键不能为空");
        }
        proxy.sb.append("DELETE FROM ").append(entityInfo.table)
                .append(" WHERE ").append(entityInfo.getColumns().get(pk))
                .append(" = ? ");
        proxy.params.add(BeanUtils.getValueFormObj(object, pk));
        return proxy;
    }

    public static SqlProxy delete(Object object, String where, Object... whereargs)
    {
        SqlProxy proxy = new SqlProxy();
        EntityInfo entityInfo = new EntityInfo(object.getClass());
        proxy.clazz = object.getClass();
        String pk = entityInfo.pk;
        if (TextUtils.isEmpty(pk))
        {
            throw new RuntimeException("主键不能为空");
        }
        proxy.sb.append("DELETE FROM ").append(entityInfo.table);
        proxy.params.add(BeanUtils.getValueFormObj(object, pk));
        proxy.buildWhere(where, whereargs);
        return proxy;
    }
    
    public static SqlProxy select (Class<?> clazz, String where, Object[] p){
        SqlProxy proxy = new SqlProxy();
        EntityInfo entityInfo = new EntityInfo(clazz);
        proxy.clazz = clazz;
        proxy.sb.append("SELECT * FROM "+entityInfo.table);
        proxy.buildWhere(where, p);
        return proxy;
    }

    public void buildWhere(String where, Object[] param)
    {
        if (TextUtils.isEmpty(where))
            return;
        sb.append(" WHERE ");
        EntityInfo entityInfo = new EntityInfo(clazz);
        Pattern pattern = Pattern.compile(":([[a-zA-Z]|\\.]*)");
        Matcher matcher = pattern.matcher(where);
        while (matcher.find())
        {
            String oldChar = matcher.group();
            String newChar = matcher.group(1);
            where = where.replace(oldChar, entityInfo.getColumns().get(newChar));
        }
        sb.append(where);
        for (int i = 0; i < param.length; i++)
        {
            params.add(param[i]);
        }
    }

}
