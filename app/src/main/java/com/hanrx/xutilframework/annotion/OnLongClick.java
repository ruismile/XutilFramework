package com.hanrx.xutilframework.annotion;

import android.view.View;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 设置长按事件
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@EventBase(listenerSetter = "setOnLongClickListener", listenerType = View.OnLongClickListener.class, callBackMethod = "onLongClick")
public @interface OnLongClick {
    int[] value() default -1;
}
