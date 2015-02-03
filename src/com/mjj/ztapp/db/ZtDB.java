package com.mjj.ztapp.db;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.mjj.ztapp.ioc.Ioc;
import com.mjj.ztapp.util.BeanUtils;

public class ZtDB
{
    public static final String TAG = "ZtDB";

    SQLiteDatabase sqliteDB;

    public ZtDB()
    {
        super();
    }

    public void init(String name, int version)
    {
        sqliteDB = new SQLiteDBhelper(Ioc.getApplicationContext(), name,
                version).getWritableDatabase();
    }

    public void initInSDcard(String path, String name, int version)
    {
        sqliteDB = createToSdcard(path, name);
        if (sqliteDB == null)
            init(name, version);

    }

    public void save(Object obj)
    {
        if (obj == null)
            return;
        Class<?> clazz = obj.getClass();
        createTable(clazz);
        EntityInfo info = EntityInfo.builder(clazz);
        String pk = info.pk;
        Object pkvalue = BeanUtils.getValueFormObj(obj, pk);
        if (pkvalue != null && !pkvalue.toString().equals("0"))
        {
            SqlProxy proxy = SqlProxy.update(obj);
            sqliteDB.execSQL(proxy.getSql(), proxy.getParamsArr());
        }
        else
        {
            InsertProxy insertProxy = InsertProxy.insert(obj);
           Long pkv = sqliteDB.insert(insertProxy.table, null, insertProxy.contentValues);
           if(pkv>0)
           BeanUtils.setValueToObj(obj, pk, pkv);
        }
    }

    public void update(Object obj)
    {
        if (obj == null)
            return;
        createTable(obj.getClass());
        SqlProxy proxy = SqlProxy.update(obj);
        sqliteDB.execSQL(proxy.getSql(), proxy.getParamsArr());
    }

    public void delete(Object obj)
    {
        if (obj == null)
            return;
        createTable(obj.getClass());
        SqlProxy proxy = SqlProxy.delete(obj);
        sqliteDB.execSQL(proxy.getSql(), proxy.getParamsArr());
    }

    public void delete(Object obj, String where, Object... args)
    {
        if (obj == null)
            return;
        createTable(obj.getClass());
        SqlProxy proxy = SqlProxy.delete(obj, where, args);
        sqliteDB.execSQL(proxy.getSql(), proxy.getParamsArr());
    }

    public void deleteAll(Class<?> clazz)
    {
        if (clazz == null)
            return;
        createTable(clazz);
        EntityInfo entityInfo = new EntityInfo(clazz);
        String sql = "DELETE FROM " + entityInfo.table;
        sqliteDB.execSQL(sql);
    }

    public <T> T load(Class<?> clazz, Object id)
    {
        EntityInfo info = EntityInfo.builder(clazz);
        return queryFirst(clazz, info.pk + " =? ", id);
    }

    public <T> T queryFirst(Class<?> clazz, String where, Object id)
    {
        if (where.indexOf("limit") < -1)
        {
            where = where + " limit 0,1";
        }
        List<T> list = queryList(clazz, where, id);
        if (list != null && list.size() > 0)
        {
            return list.get(0);
        }
        return null;
    }

    public <T> List<T> queryList(Class<?> clazz, String where, Object... params)
    {
        createTable(clazz);
        SqlProxy proxy = SqlProxy.select(clazz, where, params);
        return queryList(proxy);
    }

    public <T> List<T> queryAll(Class<?> clazz)
    {
        createTable(clazz);
        SqlProxy proxy = SqlProxy.select(clazz, null, null);
        return queryList(proxy);
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> queryList(SqlProxy proxy)
    {
        if (proxy == null)
            return null;
        Cursor cursor = sqliteDB.rawQuery(proxy.getSql(), proxy.getParamsArr());
        List<T> list = new ArrayList<T>();
        try
        {
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
                    .moveToNext())
            {
                T t = (T) buildObject(cursor, proxy.clazz);
                list.add(t);
            }
            return list;
        }
        catch(Exception e)
        {
            e.printStackTrace();
        } finally
        {
            cursor.close();
            cursor = null;
        }
        return null;

    }

    public <T> T buildObject(Cursor cursor, Class<T> clazz)
    {
        EntityInfo entityInfo = new EntityInfo(clazz);
        Set<String> keys = entityInfo.columns.keySet();
        T obj = null;
        try
        {
            obj = clazz.newInstance();
            for (Iterator<String> iterator = keys.iterator(); iterator
                    .hasNext();)
            {
                String key = iterator.next();
                String column = entityInfo.getColumns().get(key);
                Field field = BeanUtils.getDeclaredField(clazz, key);
                Class<?> claz = field.getType();
                if (claz.equals(Integer.class) || claz.equals(int.class))
                {
                    BeanUtils.setValueToObj(obj, key,
                            cursor.getInt(cursor.getColumnIndex(column)));
                }
                else if (claz.equals(Double.class) || claz.equals(double.class))
                {
                    BeanUtils.setValueToObj(obj, key,
                            cursor.getDouble(cursor.getColumnIndex(column)));
                }
                else if (claz.equals(Float.class) || claz.equals(float.class))
                {
                    BeanUtils.setValueToObj(obj, key,
                            cursor.getFloat(cursor.getColumnIndex(column)));
                }
                else if (claz.equals(String.class))
                {
                    BeanUtils.setValueToObj(obj, key,
                            cursor.getString(cursor.getColumnIndex(column)));
                }
                else if (claz.equals(Long.class) || claz.equals(long.class))
                {
                    BeanUtils.setValueToObj(obj, key,
                            cursor.getLong(cursor.getColumnIndex(column)));
                }
                else if (field.getType().equals(Boolean.class)
                        || field.getType().equals(boolean.class))
                {
                    BeanUtils.setValueToObj(obj, key, cursor.getInt(cursor
                            .getColumnIndex(column)) == 0 ? false : true);
                }
                else if (claz.equals(Date.class))
                {
                    try
                    {
                        Long l = cursor.getLong(cursor.getColumnIndex(column));
                        Date date = new Date(l);
                        BeanUtils.setValueToObj(obj, key, date);
                    }
                    catch(Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }
        catch(InstantiationException e)
        {
            e.printStackTrace();
        }
        catch(IllegalAccessException e)
        {
            e.printStackTrace();
        }
        return obj;
    }

    public void createTable(Class<?> clazz)
    {
        EntityInfo entityInfo = EntityInfo.builder(clazz);
        if (entityInfo.isExist)
        {
            return;
        }
        else
        {
            boolean isCreateTable = cheackTable(entityInfo.table);
            if (!isCreateTable)
            {
                String sql = createTableFormClazz(clazz);
                sqliteDB.execSQL(sql);
            }
            entityInfo.isExist = true;
        }

    }

    public String createTableFormClazz(Class<?> clazz)
    {
        EntityInfo entityInfo = EntityInfo.builder(clazz);
        StringBuffer sql = new StringBuffer();
        sql.append("CREATE TABLE IF NOT EXISTS ");
        sql.append(entityInfo.table);
        sql.append(" ( ");
        Map<String, String> propertys = entityInfo.getColumns();
        Set<String> keys = propertys.keySet();
        for (Iterator<String> iterator = keys.iterator(); iterator.hasNext();)
        {
            String key = iterator.next();
            sql.append(propertys.get(key));
            Class<?> clzz;
            try
            {
                clzz = clazz.getDeclaredField(key).getType();
                if (clzz.equals(int.class) || clzz.equals(Integer.class)
                        || clzz.equals(long.class) || clzz.equals(Long.class))
                {
                    sql.append(" INTEGER");
                }
                else if (clzz.equals(Double.class) || clzz.equals(double.class)
                        || clzz.equals(Float.class) || clzz.equals(float.class))
                {
                    sql.append(" REAL");
                }
                else if (clzz.equals(Boolean.class)
                        || clzz.equals(boolean.class))
                {
                    sql.append(" NUMERIC");
                }
                if (key.equals(entityInfo.pk))
                {
                    sql.append(" PRIMARY KEY");
                    if (entityInfo.pkAuto)
                    {
                        sql.append(" AUTOINCREMENT");
                    }
                }
            }
            catch(NoSuchFieldException e)
            {
                e.printStackTrace();
            }
            sql.append(",");
        }
        sql = sql.deleteCharAt(sql.length() - 1);
        sql.append(" ) ");
        return sql.toString();
    }

    public boolean cheackTable(String table)
    {
        Cursor cursor = null;
        try
        {
            String sql = "SELECT  COUNT(*) FROM sqlite_master WHERE type ='table' AND name ='"
                    + table + "'";
            Log.i(TAG, sql);
            cursor = sqliteDB.rawQuery(sql, null);
            if (cursor != null && cursor.moveToNext())
            {
                int count = cursor.getInt(0);
                if (count > 0)
                    return true;
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        } finally
        {
            if (cursor != null)
                cursor.close();
            cursor = null;
        }
        return false;

    }

    public SQLiteDatabase createToSdcard(String path, String name)
    {
        File file = new File(path, name);
        if (!file.exists())
        {
            try
            {
                if (file.createNewFile())
                {
                    return SQLiteDatabase.openOrCreateDatabase(file, null);
                }
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            return SQLiteDatabase.openOrCreateDatabase(file, null);
        }
        return null;
    }

    class SQLiteDBhelper extends SQLiteOpenHelper
    {

        public SQLiteDBhelper(Context context, String name, int version)
        {
            super(context, name, null, version);
        }

        @Override
        public void onCreate(SQLiteDatabase arg0)
        {

        }

        @Override
        public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2)
        {
            dropDb();
        }

    }

    /**
     * 删除所有数据表
     */
    public void dropDb()
    {
        Cursor cursor = sqliteDB
                .rawQuery(
                        "SELECT name FROM sqlite_master WHERE type ='table' AND name != 'sqlite_sequence'",
                        null);
        if (cursor != null)
        {
            while (cursor.moveToNext())
            {
                sqliteDB.execSQL("DROP TABLE " + cursor.getString(0));
            }
        }
        if (cursor != null)
        {
            cursor.close();
            cursor = null;
        }
    }
}
