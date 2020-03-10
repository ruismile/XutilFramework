package com.hanrx.xutilframework;

import android.content.Context;
import android.view.View;

import com.hanrx.xutilframework.annotion.ContentView;
import com.hanrx.xutilframework.annotion.ViewInject;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class InjectUtils {

    public static void inject(Context context) {
        injectLayout(context);
        injectView(context);
    }
    public static void injectView(Context context) {
        Class<?> aClass = context.getClass();
        //获取MainActivity里面所有的成员变量 包含textView
        Field[] fields = aClass.getDeclaredFields();
        for (Field field : fields) {
            //得到成员变量的注解
            ViewInject viewInject = field.getAnnotation(ViewInject.class);
            if (viewInject != null) {
                //得到id  R.id.
                int valueId = viewInject.value();
                try {
                    Method method = aClass.getMethod("findViewById", int.class);
                    //反射调用方法
                    View view = (View) method.invoke(context, valueId);
                    field.setAccessible(true);
                    field.set(context,view);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void injectLayout(Context context) {
        int layoutId = 0;
        Class<?> clazz = context.getClass();
        //拿到MainActivity类上面的注解
        ContentView contentView = clazz.getAnnotation(ContentView.class);
        if (contentView != null) {
            layoutId = contentView.value();
            try {
                Method method = clazz.getMethod("setContentView", int.class);
                method.invoke(context, layoutId);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }
}
