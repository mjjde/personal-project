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
 * @Description: TODO(相关初始化工作，新项目必须调用)
 * @author meijianjian
 * @date 2015年1月19日 上午11:26:21
 * @version 1.0
 */
public class Ztapp
{
    public static void ztappInit(Application app)
    {
        // 初始化库
        Ioc.initApplication(app);
        // 默认的弹出框
        Ioc.bind(DialogImp.class).to(IDialog.class)
                .scope(InstanceScope.SCOPE_PROTOTYPE);
        ZtDB ztDB = IocContext.getContext().get(ZtDB.class);
        ztDB.init("ztapp", Const.DATABASE_VERSION);
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                app)
                .threadPoolSize(3)
                // 线程池内加载的数量
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .memoryCacheSize(10 * 1024 * 1024)
                .discCacheSize(50 * 1024 * 1024)//
                .discCacheFileCount(100)// 缓存一百张图片
                .writeDebugLogs().build();
        ImageLoader.getInstance().init(config);
    }
}
