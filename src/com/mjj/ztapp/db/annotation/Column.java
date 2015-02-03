package com.mjj.ztapp.db.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Column
{
    /**
     * ��Ӧ��
     * 
     * @return
     */
    public String name() default "";

    /**
     * �Ƿ�������
     * 
     * @return
     */
    public boolean pk() default false;

    /**
     * �����Ƿ�����,ֻ��long,int ��������Ч
     * 
     * @return
     */
    public boolean auto() default true;

}
