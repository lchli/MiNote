package com.lch.menote.utils;

import android.os.Looper;

import com.lchli.arch.clean.UseCase;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by Administrator on 2019/1/29.
 */

public final class MvpViewUtils {

    public static <T> T newProxy(T mvpView) {
        return (T) Proxy.newProxyInstance(mvpView.getClass().getClassLoader(), mvpView.getClass().getInterfaces(), new InvocationHandler() {
            @Override
            public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
                if (Looper.myLooper() != Looper.getMainLooper()) {
                    UseCase.executeOnMainThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                method.invoke(proxy, args);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } else {
                    method.invoke(proxy, args);
                }

                return null;
            }
        });
    }


}
