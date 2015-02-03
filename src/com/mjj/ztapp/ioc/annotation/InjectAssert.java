package com.mjj.ztapp.ioc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface InjectAssert
{
    public String path() default "";

    // �ļ�ע��ʱ���첽,�ļ�ע����ɺ��ص��������
    public String fileInjected() default "";
}
