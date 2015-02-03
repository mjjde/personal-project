package com.mjj.ztapp.dialog;

import com.mjj.ztapp.ioc.IocContext;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * @ClassName: DialogImp
 * @Description: TODO(���ֶԻ����ʵ��)
 * @author meijianjian
 * @date 2015��1��29�� ����3:52:42
 * @version 1.0
 */
public class DialogImp implements IDialog
{

    /**
     * (�� Javadoc) ��toast
     * <p>
     * Title: showToastShort
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @param context
     * @param msg
     * @see com.mjj.ztapp.dialog.IDialog#showToastShort(android.content.Context,
     *      java.lang.String)
     */
    @Override
    public void showToastShort(Context context, String msg)
    {
        Toast toast = IocContext.getContext().get(Toast.class);
        toast.setDuration(Toast.LENGTH_SHORT);
        TextView tv = new TextView(context);
        tv.setText(msg);
        tv.setPadding(15, 10, 15, 10);
        tv.setBackgroundResource(android.R.drawable.toast_frame);
        toast.setView(tv);
        toast.show();
    }

    /**
     * (�� Javadoc) ��ʱ��toast
     * <p>
     * Title: showToastLong
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @param context
     * @param msg
     * @see com.mjj.ztapp.dialog.IDialog#showToastLong(android.content.Context,
     *      java.lang.String)
     */
    @Override
    public void showToastLong(Context context, String msg)
    {
        Toast toast = IocContext.getContext().get(Toast.class);
        toast.setDuration(Toast.LENGTH_LONG);
        TextView tv = new TextView(context);
        tv.setText(msg);
        tv.setPadding(15, 10, 15, 10);
        tv.setBackgroundResource(android.R.drawable.toast_frame);
        toast.setView(tv);
        toast.show();
    }

    @Override
    public Dialog showDialog(Context context, String title, String msg,
            DialogCallBack callback)
    {
        return showDialog(context, 0, title, msg, callback);
    }

    /**
     * �Ի���(�� Javadoc)
     * <p>
     * Title: showDialog
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @param context
     * @param icon
     * @param title
     * @param msg
     * @param callback
     * @return
     * @see com.mjj.ztapp.dialog.IDialog#showDialog(android.content.Context,
     *      int, java.lang.String, java.lang.String,
     *      com.mjj.ztapp.dialog.DialogCallBack)
     */
    @Override
    public Dialog showDialog(Context context, int icon, String title,
            String msg, final DialogCallBack callback)
    {
        Builder builder = new AlertDialog.Builder(context);
        if (icon != 0)
        {
            builder.setIcon(icon);
        }
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setNegativeButton("ȷ��", new OnClickListener()
        {
            @Override
            public void onClick(DialogInterface arg0, int arg1)
            {
                if (callback != null)
                {
                    callback.click(IDialog.YES);
                }
            }
        });
        builder.setPositiveButton("ȡ��", new OnClickListener()
        {

            @Override
            public void onClick(DialogInterface arg0, int arg1)
            {
                if (callback != null)
                {
                    callback.click(IDialog.NO);
                }
            }
        });
        return builder.show();
    }

    /**
     * item�Ի���(�� Javadoc)
     * <p>
     * Title: showItemDialog
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @param context
     * @param title
     * @param items
     * @param callback
     * @return
     * @see com.mjj.ztapp.dialog.IDialog#showItemDialog(android.content.Context,
     *      java.lang.String, java.lang.CharSequence[],
     *      com.mjj.ztapp.dialog.DialogCallBack)
     */
    @Override
    public Dialog showItemDialog(Context context, String title,
            CharSequence[] items, final DialogCallBack callback)
    {
        Dialog dialog = new AlertDialog.Builder(context)
                .setItems(items, new OnClickListener()
                {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1)
                    {
                        if (callback != null)
                        {
                            callback.click(arg1);
                        }

                    }
                }).setTitle(title).show();
        return dialog;
    }

    @Override
    public Dialog showProgressDialog(Context context, String title, String msg)
    {
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setTitle(title);
        dialog.setMessage(msg);
        dialog.setCancelable(true);
        dialog.show();
        return dialog;
    }

    @Override
    public Dialog showProgressDialog(Context context, String msg)
    {
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setMessage(msg);
        dialog.setCancelable(true);
        dialog.show();
        return dialog;
    }

    @Override
    public Dialog showProgressDialog(Context context)
    {
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setCancelable(true);
        dialog.show();
        return dialog;
    }

    @Override
    public Dialog showAdapterDialoge(Context context, String title,
            ListAdapter adapter, OnItemClickListener itemClickListener)
    {
        // TODO Auto-generated method stub
        return null;
    }

}
