package com.mjj.ztapp.ioc;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mjj.ztapp.ioc.annotation.Inject;
import com.mjj.ztapp.ioc.annotation.InjectAssert;
import com.mjj.ztapp.ioc.annotation.InjectExtra;
import com.mjj.ztapp.ioc.annotation.InjectResource;
import com.mjj.ztapp.ioc.annotation.InjectView;
import com.mjj.ztapp.thread.Task;
import com.mjj.ztapp.thread.ThreadWorker;
import com.mjj.ztapp.util.FileUtils;

/**
 * 
 * @ClassName: InjectUtil
 * @Description: TODO(注入工具类，包括view,标准类,资源,Assert,bundle)
 * @author meijianjian
 * @date 2015年1月29日 下午3:57:52
 * @version 1.0
 */
public class InjectUtil
{
    public static final String LOG_TAG = "meijianjian_InjectUtil";

    public static void inject(Object obj)
    {
        if (obj == null)
            return;
        Field[] fields = getDeclaredFields(obj.getClass());
        if (fields != null && fields.length > 0)
        {
            for (Field field : fields)
            {
                field.setAccessible(true);
                try
                {
                    if (field.get(obj) != null)
                    {
                        continue;
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

                InjectView injectView = field.getAnnotation(InjectView.class); // view注入
                if (injectView != null)
                {
                    indectView(obj, field, injectView);
                }

                Inject inject = field.getAnnotation(Inject.class);// 标准注入
                if (inject != null)
                {
                    indectStand(obj, field, inject);
                }

                if (obj instanceof Activity || obj instanceof Fragment)// Activity和Fragment的传值注入
                {
                    InjectExtra extra = field.getAnnotation(InjectExtra.class);
                    if (extra != null)
                    {
                        indectExtar(obj, field, extra);
                    }
                }

                InjectResource injectResource = field
                        .getAnnotation(InjectResource.class); // 资源注入
                if (injectResource != null)
                {
                    indectResource(obj, field, injectResource);
                }

                InjectAssert injectAssert = field
                        .getAnnotation(InjectAssert.class);// Assert注入
                if (injectAssert != null)
                {
                    indectAssert(obj, field, injectAssert);
                }
            }
        }
    }

    /**
     * 
     *
     * @Title: getDeclaredFields
     * @Description: TODO(获取class中的所以属性，包括各级别父类)
     * @param @param clazz
     * @param @return 设定文件
     * @return Field[] 返回类型
     * @throws
     */
    public static Field[] getDeclaredFields(Class clazz)
    {
        List<Field> fieldList = new ArrayList<Field>();
        try
        {
            Field[] fields = clazz.getDeclaredFields();
            if (fields != null && fields.length > 0)
            {
                for (Field field : fields)
                {
                    if (Modifier.FINAL == field.getModifiers())
                    {
                        continue;
                    }
                    if (Modifier.STATIC == field.getModifiers())
                    {
                        continue;
                    }
                    if ("serialVersionUID".equals(field.getName()))
                    {
                        continue;
                    }
                    fieldList.add(field);
                }
            }
        }
        catch(Exception e)
        {
            Log.e(LOG_TAG, LOG_TAG + " error for " + e.getMessage());
        }

        clazz = clazz.getSuperclass();
        for (; isCommClass(clazz); clazz = clazz.getSuperclass())
        {
            try
            {
                Field[] fields = clazz.getDeclaredFields();
                if (fields != null && fields.length > 0)
                {
                    for (Field field : fields)
                    {
                        if (Modifier.FINAL == field.getModifiers())
                        {
                            continue;
                        }
                        if (Modifier.STATIC == field.getModifiers())
                        {
                            continue;
                        }
                        if ("serialVersionUID".equals(field.getName()))
                        {
                            continue;
                        }
                        fieldList.add(field);
                    }
                }
            }
            catch(Exception e)
            {
                Log.e(LOG_TAG, LOG_TAG + " error for " + e.getMessage());
            }
        }
        return fieldList.toArray(new Field[0]);
    }

    /**
     * 
     *
     * @Title: isCommClass
     * @Description: TODO(判断是否对该clazz进行注入判断条件是jar和自己项目中的类return true)
     * @param @param clazz
     * @param @return 设定文件
     * @return boolean 返回类型
     * @throws
     */
    public static boolean isCommClass(Class clazz)
    {
        String pkgName = clazz.getPackage().getName();
        String pkg = IocContext.getContext().getApplication().getPackageName();
        boolean isOk = pkgName.startsWith("com.mjj.ztapp")
                || pkgName.startsWith(pkg);
        if (isOk)
        {
            return true;
        }
        return false;
    }

    public static void indectView(Object obj, Field field, InjectView injectView)
    {
        View view = null;
        if (injectView == null)
            return;
        int layout = injectView.layout();
        // 布局
        if (layout != 0)
        {
            view = LayoutInflater
                    .from(IocContext.getContext().getApplication()).inflate(
                            layout, null);
        }
        else
        {
            String inView = injectView.inView();
            if (!TextUtils.isEmpty(inView))
            {
                Field inViewField;
                try
                {
                    inViewField = obj.getClass().getDeclaredField(inView);
                    inViewField.setAccessible(true);
                    View parentView = (View) inViewField.get(obj);
                    if (parentView == null)
                    {
                        InjectView inject = inViewField
                                .getAnnotation(InjectView.class);
                        indectView(obj, inViewField, inject);
                        parentView = (View) inViewField.get(obj);
                    }
                    view = parentView.findViewById(injectView.id());
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
            else
            {
                if (obj instanceof Activity)
                {
                    Activity act = (Activity) obj;
                    view = act.findViewById(injectView.id());
                }
                else if (obj instanceof Dialog)
                {
                    Dialog dia = (Dialog) obj;
                    dia.findViewById(injectView.id());
                }
                else if (obj instanceof Fragment)
                {
                    Fragment fragment = (Fragment) obj;
                    view = ((Fragment) obj).getView().findViewById(
                            injectView.id());
                }
                else if (obj instanceof View)
                {
                    View v = (View) obj;
                    view = v.findViewById(injectView.id());
                }
            }
        }
        try
        {
            field.set(obj, view);
        }
        catch(IllegalAccessException e)
        {
            e.printStackTrace();
        }
        catch(IllegalArgumentException e)
        {
            e.printStackTrace();
        }

        // 事件绑定

        // 点击
        String click = injectView.click();
        if (!TextUtils.isEmpty(click))
        {
            bindClick(obj, field, click);
        }
        // 长按
        String longClick = injectView.longClick();
        if (!TextUtils.isEmpty(longClick))
        {
            bindLongClick(obj, field, longClick);
        }
    }

    public static void bindClick(Object obj, Field field, String clickMethod)
    {
        if (obj == null)
            return;
        try
        {
            Object o = field.get(obj);
            if (o == null)
                return;
            if (o instanceof View)
            {
                ((View) o).setOnClickListener(new EventListener(obj)
                        .click(clickMethod));
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
    }

    public static void bindLongClick(Object obj, Field field, String clickMethod)
    {

        if (obj == null)
            return;
        try
        {
            Object o = field.get(obj);
            if (o == null)
                return;
            if (o instanceof View)
            {
                ((View) o).setOnLongClickListener(new EventListener(obj)
                        .longClick(clickMethod));
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

    }

    public static void indectStand(Object obj, Field field, Inject inject)
    {
        String name = null;
        String tag = null;
        Object o;
        if (obj == null)
            return;
        name = inject.name();
        if (!TextUtils.isEmpty(name))
        {
            o = IocContext.getContext().get(name);
        }
        else
        {
            Class clazz = field.getType();
            tag = inject.tag();
            if (!TextUtils.isEmpty(tag))
            {
                o = IocContext.getContext().get(clazz, tag);
            }
            else
            {
                o = IocContext.getContext().get(clazz);
            }
        }
        try
        {
            field.set(obj, o);
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

    @SuppressWarnings("unchecked")
    public static void indectExtar(Object obj, Field field, InjectExtra inject)
    {
        if (obj == null)
            return;
        Bundle bundle = null;
        if (obj instanceof Activity)
        {
            Activity atc = (Activity) obj;
            bundle = atc.getIntent().getExtras();
        }
        if (obj instanceof Fragment)
        {
            Fragment fragment = (Fragment) obj;
            bundle = fragment.getArguments();
        }
        if (bundle == null)
        {
            bundle = new Bundle();
        }
        Object o = null;
        Class clazz = field.getType();
        if (clazz.equals(Integer.class))
        {
            if (!TextUtils.isEmpty(inject.def()))
            {
                o = bundle
                        .getInt(inject.name(), Integer.parseInt(inject.def()));
            }
            else
            {
                o = bundle.getInt(inject.name(), 0);
            }
        }
        else if (clazz.equals(String.class))
        {
            o = bundle.getString(inject.name());
            if (o == null)
            {
                if (!TextUtils.isEmpty(inject.def()))
                {
                    o = inject.def();
                }
            }
        }
        else if (clazz.equals(Long.class))
        {

            if (!TextUtils.isEmpty(inject.def()))
            {
                o = bundle.getLong(inject.name(), Long.parseLong(inject.def()));
            }
            else
            {
                o = bundle.getLong(inject.name(), 0);
            }

        }
        else if (clazz.equals(Double.class))
        {

            if (!TextUtils.isEmpty(inject.def()))
            {
                o = bundle.getDouble(inject.name(),
                        Double.parseDouble(inject.def()));
            }
            else
            {
                o = bundle.getDouble(inject.name(), 0);
            }

        }
        else if (clazz.equals(Float.class))
        {

            if (!TextUtils.isEmpty(inject.def()))
            {
                o = bundle.getFloat(inject.name(),
                        Float.parseFloat(inject.def()));
            }
            else
            {
                o = bundle.getFloat(inject.name(), 0);
            }

        }
        else if (clazz.equals(Boolean.class))
        {

            if (!TextUtils.isEmpty(inject.def()))
            {
                o = bundle.getBoolean(inject.name(),
                        Boolean.parseBoolean(inject.def()));
            }
            else
            {
                o = bundle.getBoolean(inject.name(), false);
            }

        }
        else if (clazz.equals(JSONObject.class))
        {
            String value = bundle.getString(inject.name());
            if (!TextUtils.isEmpty(value))
            {
                try
                {
                    o = new JSONObject(value);
                }
                catch(JSONException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        else if (clazz.equals(JSONArray.class))
        {
            String value = bundle.getString(inject.name());
            if (!TextUtils.isEmpty(value))
            {
                try
                {
                    o = new JSONArray(value);
                }
                catch(JSONException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        else
        {
            String value = bundle.getString(inject.name());
            if (!TextUtils.isEmpty(value))
            {
                o = new Gson().fromJson(value, clazz);
            }
        }
        if (o != null)
        {
            try
            {
                field.set(obj, o);
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

    public static void indectResource(Object obj, Field field,
            InjectResource inject)
    {
        if (obj == null)
            return;
        Object o = null;
        Resources resources = IocContext.getContext().getApplicationContext()
                .getResources();
        if (inject.drawable() != 0)
        {
            o = resources.getDrawable(inject.drawable());
        }
        else if (inject.color() != 0)
        {
            o = resources.getColor(inject.color());
        }
        else if (inject.string() != 0)
        {
            o = resources.getString(inject.string());
        }
        else if (inject.dimen() != 0)
        {
            o = resources.getDimensionPixelSize(inject.dimen());
        }
        if (o != null)
        {
            try
            {
                field.set(obj, o);
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

    public static void indectAssert(final Object object, final Field field,
            final InjectAssert injectAssert)
    {
        if (object == null)
            return;
        Object o = null;
        Class clazz = field.getType();
        AssetManager assetManager = IocContext.getContext()
                .getApplicationContext().getAssets();
        if (injectAssert.path() != null)
        {
            try
            {
                final InputStream inputStream = assetManager.open(injectAssert
                        .path());
                if (clazz.equals(InputStream.class))
                {
                    o = inputStream;
                }
                else if (clazz.equals(String.class)
                        || clazz.equals(JSONArray.class)
                        || clazz.equals(JsonObject.class))
                {
                    Scanner scanner = new Scanner(inputStream);
                    StringBuffer sb = new StringBuffer();
                    if (scanner.hasNext())
                    {
                        sb.append(scanner.nextLine());
                    }
                    inputStream.close();
                    scanner.close();
                    o = sb.toString();
                    if (clazz.equals(JSONArray.class))
                    {
                        try
                        {
                            o = new JSONArray(o.toString());
                        }
                        catch(JSONException e)
                        {
                            e.printStackTrace();
                        }
                    }
                    else if (clazz.equals(JsonObject.class))
                    {
                        try
                        {
                            o = new JSONObject(o.toString());
                        }
                        catch(JSONException e)
                        {
                            e.printStackTrace();
                        }
                    }

                }
                else if (clazz.equals(File.class))
                {
                    File dir = FileUtils.getDir();
                    final File file = new File(dir, injectAssert.path());
                    if (!file.exists())
                    {
                        ThreadWorker.execute(false,
                                new Task(Ioc.getApplicationContext())
                                {
                                    @Override
                                    public void doInBackground()
                                    {
                                        super.doInBackground();
                                        FileUtils.write(inputStream, file);
                                        try
                                        {
                                            field.set(object, file);
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

                                    @Override
                                    public void doInUI(Object obj, Integer what)
                                    {
                                        if (!TextUtils.isEmpty(injectAssert
                                                .fileInjected()))
                                        {
                                            try
                                            {
                                                Method method = object
                                                        .getClass()
                                                        .getMethod(
                                                                injectAssert
                                                                        .fileInjected());
                                                method.invoke(object);
                                            }
                                            catch(NoSuchMethodException e)
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
                                            catch(InvocationTargetException e)
                                            {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                });
                        return;
                    }
                    else
                    {
                        if (!TextUtils.isEmpty(injectAssert.fileInjected()))
                        {
                            try
                            {
                                Method method = object.getClass().getMethod(
                                        injectAssert.fileInjected());
                                method.invoke(object);
                            }
                            catch(NoSuchMethodException e)
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
                            catch(InvocationTargetException e)
                            {
                                e.printStackTrace();
                            }
                        }
                    }
                    o = file;
                }
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
            if (o != null)
            {
                try
                {
                    field.set(object, o);
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
    }
}
