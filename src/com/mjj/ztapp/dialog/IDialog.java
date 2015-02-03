package com.mjj.ztapp.dialog;

import android.app.Dialog;
import android.content.Context;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;

/**
 * 
 * @ClassName: IDialog
 * @Description: TODO(各种弹出框的接口)
 * @author meijianjian
 * @date 2015年1月29日 下午3:51:19
 * @version 1.0
 */
public interface IDialog
{
    public static final int YES = 1; // 确定

    public static final int NO = 2; // 取消

    public void showToastShort(Context context, String msg);

    public void showToastLong(Context context, String msg);

    public Dialog showDialog(Context context, String title, String msg,
            DialogCallBack callback);

    public Dialog showDialog(Context context, int ioc, String title,
            String msg, DialogCallBack callback);

    public Dialog showItemDialog(Context context, String title,
            CharSequence[] items, DialogCallBack callback);

    public Dialog showProgressDialog(Context context, String title, String msg);

    public Dialog showProgressDialog(Context context, String msg);

    public Dialog showProgressDialog(Context context);

    public Dialog showAdapterDialoge(Context context, String title,
            ListAdapter adapter, OnItemClickListener itemClickListener);
}
