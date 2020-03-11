package com.hanrx.xutilframework;

import android.content.Context;
import android.view.View;

import com.hanrx.xutilframework.annotion.ContentView;
import com.hanrx.xutilframework.annotion.EventBase;
import com.hanrx.xutilframework.annotion.ViewInject;
import com.hanrx.xutilframework.proxy.ListenerInvocationHandler;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

public class InjectUtils {

    public static void inject(Context context) {
        injectLayout(context);
        injectView(context);
        injectEvents(context);
    }

    /**
     * 注入事件
     * @param context
     */
    public static void injectEvents(Context context) {
        Class<?> clazz = context.getClass();
        //获取activity所有方法
        Method[] methods = clazz.getDeclaredMethods();
        //遍历activity所有的方法
        for(Method method : methods) {
            //获取方法上所有的注解
            Annotation[] annotations = method.getAnnotations();
            for (Annotation annotation : annotations) {
                //获取注解 anntionType  OnClick
                Class<?> anntionType = annotation.annotationType();
                //获取注解的注解 onClick注解上面的EventBase
                EventBase eventBase = anntionType.getAnnotation(EventBase.class);
                if (eventBase == null) {
                    continue;
                }
                //开始获取事件三要素，通过反射注入进去
                //1 . 返回 setOnClickListener字符串
                String listenerSetter = eventBase.listenerSetter();
                //2 . 得到View.OnClickListener.class
                Class<?> listenerType = eventBase.listenerType();
                //3 . 得到回调的方法 --- onClick
                String callMethod = eventBase.callBackMethod();
                //方法名与方法Method对应关系
                Map<String, Method> methodMap = new HashMap<>();
                methodMap.put(callMethod, method);
                try {
                    Method valueMethod = anntionType.getDeclaredMethod("value");
                    int[] viewIds = (int[]) valueMethod.invoke(annotation);
                    for (int viewId : viewIds) {
                        //通过反射拿到TextView
                        Method findViewById = clazz.getMethod("findViewById", int.class);
                        View view = (View) findViewById.invoke(context, viewId);
                        if (view == null) {
                            continue;
                        }
                        //listenerSetter ：setOnClickListener     listenerType   ： View.OnClickListener.class
                        Method setOnClickListener = view.getClass().getMethod(listenerSetter,listenerType);
                        ListenerInvocationHandler handler = new ListenerInvocationHandler(context, methodMap);
                        //proxy已经实现的listenerType 接口
                        Object proxy = Proxy.newProxyInstance(listenerType.getClassLoader(),
                                new Class[]{listenerType},handler);
                        //类比textView.setOnClickListener(new View.OnClickLisntener{});)
                        setOnClickListener.invoke(view, proxy);
                    }
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
