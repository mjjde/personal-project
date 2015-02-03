package com.mjj.ztapp.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.os.Environment;

import com.mjj.ztapp.ioc.IocContext;

/**
 * 
 * @ClassName: FileUtils
 * @Description: TODO(File操作类)
 * @author meijianjian
 * @date 2015年1月29日 下午5:43:26
 * @version 1.0
 */
public class FileUtils
{
    /**
     * 
     *
     * @Title: getDir
     * @Description: TODO(获取项目保存路径)
     * @param @return 设定文件
     * @return File 返回类型
     * @throws
     */
    public static File getDir()
    {
        Context context = IocContext.getContext().getApplicationContext();
        String packname = context.getPackageName();
        String name = packname.substring(packname.lastIndexOf(".") + 1,
                packname.length());
        File dir = null;
        if ((!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)))
        {
            dir = context.getCacheDir();
        }
        else
        {
            dir = new File(Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + "/ztapp/" + name);
        }
        dir.mkdirs();
        return dir;
    }

    /**
     * 
     *
     * @Title: write
     * @Description: TODO(流写入文件)
     * @param @param is
     * @param @param file 设定文件
     * @return void 返回类型
     * @throws
     */
    public static void write(InputStream is, File file)
    {
        if (file.exists())
        {
            file.delete();
        }
        try
        {
            file.createNewFile();
            FileOutputStream fs = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            while (is.read(buffer) > -1)
            {
                fs.write(buffer);
            }
            fs.flush();
            is.close();
            fs.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }

    }
}
