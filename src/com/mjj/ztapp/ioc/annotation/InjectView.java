package com.mjj.ztapp.ioc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface InjectView
{
    // 注解view的id
    public int id() default 0;

    // 注解view 的layout
    public int layout() default 0;

    // 注解view 的父view
    public String inView() default "";

    // 注解view 的点击事件
    public String click() default "";

    // 注解view 的长按事件
    public String longClick() default "";

    // itemview 的点击事件
    public String itemClick() default "";

    // itemview 的长按事件
    public String itemLongClick() default "";
}
