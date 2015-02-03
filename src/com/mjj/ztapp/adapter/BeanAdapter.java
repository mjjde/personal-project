package com.mjj.ztapp.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class BeanAdapter<T> extends BaseAdapter
{
    Context context;

    int resourceId;

    int mDropDownResource;

    boolean isViewReuse;

    List<T> mValues = null;

    boolean canNotify = true;

    private final Object mLock = new Object();

    LayoutInflater mInflater;

    Map<Integer, inViewEventListener> clickEvent;

    public BeanAdapter(Context context, int resourceId, boolean isViewReuse)
    {
        this.context = context;
        this.resourceId = resourceId;
        this.isViewReuse = isViewReuse;
        this.mDropDownResource = resourceId;
        mInflater = LayoutInflater.from(context);
        mValues = new ArrayList<T>();
    }

    public BeanAdapter(Context context, int resourceId)
    {
        this(context, resourceId, true);
    }

    public void canNotify(boolean canNotify)
    {
        this.canNotify = canNotify;
    }

    @SuppressWarnings({ "unchecked", "hiding" })
    public <T> List<T> getValues()
    {
        return (List<T>) mValues;
    }

    public void add(T t)
    {
        synchronized (mLock)
        {
            mValues.add(t);
        }
        if (canNotify)
        {
            notifyDataSetChanged();
        }
    }

    public void addMore(List<T> list)
    {
        synchronized (mLock)
        {
            mValues.addAll(list);
        }
        if (canNotify)
        {
            notifyDataSetChanged();
        }
    }

    public void insert(int index, T t)
    {
        synchronized (mLock)
        {
            if (index < 0)
                index = 0;
            mValues.add(index, t);
        }
        if (canNotify)
        {
            notifyDataSetChanged();
        }

    }

    public void delete(int index)
    {
        synchronized (mLock)
        {
            if (index >= 0 && index < mValues.size())
            {
                mValues.remove(index);
            }
        }
        if (canNotify)
        {
            notifyDataSetChanged();
        }
    }

    public void clear()
    {
        synchronized (mLock)
        {
            mValues.clear();
        }
        if (canNotify)
        {
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount()
    {
        return mValues.size();
    }

    @Override
    public Object getItem(int position)
    {
        if (position >= 0 && position < mValues.size())
        {
            return mValues.get(position);
        }
        return null;
    }

    @SuppressWarnings({ "hiding", "unchecked" })
    public <T> T getTItem(int position)
    {
        if (position >= 0 && position < mValues.size())
        {
            return (T) mValues.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    public String getTItemId(int position)
    {

        return position + "";
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View view;
        if (isViewReuse)
        {
            if (convertView == null)
            {
                view = mInflater.inflate(resourceId, parent, false);
            }
            else
            {
                view = convertView;
            }

        }
        else
        {
            if (convertView != null)
            {
                parent.removeView(convertView);
            }
            view = mInflater.inflate(resourceId, parent, false);
        }
        bindView(view, position, mValues.get(position));
        bindInViewListener(view, position, mValues.get(position));
        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent)
    {
        View view;
        if (convertView == null)
        {
            view = mInflater.inflate(resourceId, parent, false);
        }
        else
        {
            view = convertView;
        }
        bindView(view, position, mValues.get(position));
        bindInViewListener(view, position, mValues.get(position));
        return view;
    }

    public abstract void bindView(View view, int position, T t);

    public void bindInViewListener(final View view, final int position,
            final Object obj)
    {
        if (clickEvent != null)
        {
            Set<Integer> keys = clickEvent.keySet();
            for (Iterator<Integer> iterator = keys.iterator(); iterator
                    .hasNext();)
            {
                int id = iterator.next();
                View inView = view.findViewById(id);
                final inViewEventListener eventListener = clickEvent.get(id);
                if (inView != null && eventListener != null)
                {
                    inView.findViewById(id).setOnClickListener(
                            new OnClickListener()
                            {
                                @Override
                                public void onClick(View v)
                                {
                                    eventListener.OnClickListener(view, v,
                                            position, obj);
                                }
                            });
                }
            }
        }
    }

    @SuppressLint("UseSparseArrays")
    public void setInViewEventListener(int key,
            inViewEventListener eventListener)
    {
        if (clickEvent == null)
        {
            clickEvent = new HashMap<Integer, inViewEventListener>();
        }
        clickEvent.put(key, eventListener);
    }

    public interface inViewEventListener
    {
        public void OnClickListener(View parentV, View v, Integer position,
                Object values);
    }

}
