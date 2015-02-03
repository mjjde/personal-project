package com.mjj.ztapp.ioc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface InjectView
{
    // ע��view��id
    public int id() default 0;

    // ע��view ��layout
    public int layout() default 0;

    // ע��view �ĸ�view
    public String inView() default "";

    // ע��view �ĵ���¼�
    public String click() default "";

    // ע��view �ĳ����¼�
    public String longClick() default "";

    // itemview �ĵ���¼�
    public String itemClick() default "";

    // itemview �ĳ����¼�
    public String itemLongClick() default "";
}
