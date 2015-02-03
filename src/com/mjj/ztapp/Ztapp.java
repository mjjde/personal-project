package com.mjj.ztapp;

import android.app.Application;

import com.mjj.ztapp.db.ZtDB;
import com.mjj.ztapp.dialog.DialogImp;
import com.mjj.ztapp.dialog.IDialog;
import com.mjj.ztapp.ioc.Ioc;
import com.mjj.ztapp.ioc.IocContext;
import com.mjj.ztapp.ioc.IocInstance.InstanceScope;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * 
 * @ClassName: Ztapp
 * @Description: TODO(��س�ʼ������������Ŀ�������)
 * @author meijianjian
 * @date 2015��1��19�� ����11:26:21
 * @version 1.0
 */
public class Ztapp
{
    public static void ztappInit(Application app)
    {
        // ��ʼ����
        Ioc.initApplication(app);
        // Ĭ�ϵĵ�����
        Ioc.bind(DialogImp.class).to(IDialog.class)
                .scope(InstanceScope.SCOPE_PROTOTYPE);
        ZtDB ztDB = IocContext.getContext().get(ZtDB.class);
        ztDB.init("ztapp", Const.DATABASE_VERSION);
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                app)
                .threadPoolSize(3)
                // �̳߳��ڼ��ص�����
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .memoryCacheSize(10 * 1024 * 1024)
                .discCacheSize(50 * 1024 * 1024)//
                .discCacheFileCount(100)// ����һ����ͼƬ
                .writeDebugLogs().build();
        ImageLoader.getInstance().init(config);
    }
}
