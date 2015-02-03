package com.mjj.ztapp.db;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import android.text.TextUtils;

import com.mjj.ztapp.db.annotation.Column;
import com.mjj.ztapp.db.annotation.Entity;
import com.mjj.ztapp.db.annotation.Nocolumn;

public class EntityInfo
{
    String table;

    String pk;

    boolean pkAuto;

    Map<String, String> columns = new HashMap<String, String>();

    boolean isExist = false;

    private static Map<String, EntityInfo> entitys = new HashMap<String, EntityInfo>();

    public EntityInfo(Class<?> clazz)
    {
        Entity entity = clazz.getAnnotation(Entity.class);
        if (entity != null)
        {
            table = TextUtils.isEmpty(entity.table()) ? clazz.getSimpleName()
                    : entity.table();
        }
        else
        {
            table = clazz.getSimpleName();
        }
        Field[] fields = clazz.getFields();
        for (Field field : fields)
        {
            Column column = field.getAnnotation(Column.class);
            Nocolumn nocolumn = field.getAnnotation(Nocolumn.class);
            if (column != null || nocolumn == null)
            {
                String columnName;
                if (column != null)
                {
                    columnName = TextUtils.isEmpty(column.name()) ? field
                            .getName() : column.name();
                    if (column.pk())
                    {
                        pk = field.getName();
                        if (column.auto())
                        {
                            Class<?> clzz = field.getType();
                            if (clzz.equals(int.class)
                                    || clzz.equals(Integer.class)
                                    || clzz.equals(Float.class)
                                    || clzz.equals(float.class)
                                    || clzz.equals(Double.class)
                                    || clzz.equals(double.class)
                                    || clzz.equals(Long.class)
                                    || clzz.equals(long.class))
                            {
                                pkAuto = true;
                            }
                            else
                            {
                                pkAuto = false;
                            }

                        }
                    }
                }
                else
                {
                    columnName = field.getName();
                }
                columns.put(field.getName(), columnName);
            }
        }
    }

    public static EntityInfo builder(Class<?> clazz)
    {
        EntityInfo entityInfo = entitys.get(clazz.getSimpleName());
        if (entityInfo == null)
        {
            entityInfo = new EntityInfo(clazz);
            entitys.put(entityInfo.table, entityInfo);
        }
        return entityInfo;
    }

    public boolean isExist()
    {
        return isExist;
    }

    public void setExist(boolean isExist)
    {
        this.isExist = isExist;
    }

    public String getTable()
    {
        return table;
    }

    public String getPk()
    {
        return pk;
    }

    public boolean isPkAuto()
    {
        return pkAuto;
    }

    public Map<String, String> getColumns()
    {
        return columns;
    }

}
