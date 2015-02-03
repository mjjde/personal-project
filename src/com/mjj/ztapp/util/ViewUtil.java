package com.mjj.ztapp.util;

import com.mjj.ztapp.adapter.FixValue;
import com.mjj.ztapp.ioc.IocContext;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ViewUtil
{
    public static void bindView(View view, Object obj)
    {
        if (view == null || obj == null)
            return;
        if (view instanceof ImageView)
        {
            if (obj instanceof String)
            {
                ImageLoader.getInstance().displayImage((String) obj,
                        (ImageView) view);
            }
            else if (obj instanceof Drawable)
            {
                ((ImageView) view).setImageDrawable((Drawable) obj);
            }
            else if (obj instanceof Bitmap)
            {
                ((ImageView) view).setImageBitmap((Bitmap) obj);
            }
            else if (obj instanceof Integer)
            {
                ((ImageView) view).setImageResource((Integer) obj);
            }
        }
        else if (view instanceof TextView)
        {
            if (obj instanceof CharSequence)
            {
                ((TextView) view).setText((CharSequence) obj);
            }
            else
            {
                ((TextView) view).setText(obj.toString());
            }
        }
    }

    public static void bindView(View view, Object obj, String type)
    {
        if (view == null || obj == null)
            return;
        if (view instanceof ImageView)
        {
            FixValue fixValue = IocContext.getContext().get(FixValue.class);
            DisplayImageOptions options = null;
            if (fixValue != null)
            {
                options = fixValue.imageOptions(type);
            }
            if (obj instanceof String)
            {
                ImageLoader.getInstance().displayImage((String) obj,
                        (ImageView) view, options);
            }
            else if (obj instanceof Drawable)
            {
                ((ImageView) view).setImageDrawable((Drawable) obj);
            }
            else if (obj instanceof Bitmap)
            {
                ((ImageView) view).setImageBitmap((Bitmap) obj);
            }
            else if (obj instanceof Integer)
            {
                ((ImageView) view).setImageResource((Integer) obj);
            }
        }
        else if (view instanceof TextView)
        {
            FixValue fixValue = IocContext.getContext().get(FixValue.class);
            if (fixValue != null)
            {
                obj = fixValue.fix(obj, type);
            }
            if (obj instanceof CharSequence)
            {
                ((TextView) view).setText((CharSequence) obj);
            }
            else
            {
                ((TextView) view).setText(obj.toString());
            }
        }
    }
}
