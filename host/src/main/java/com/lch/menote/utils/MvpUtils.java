package com.lch.menote.utils;

import android.os.Looper;

import com.lchli.arch.clean.UseCase;

import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by Administrator on 2019/1/29.
 */

@SuppressWarnings("unchecked")
public final class MvpUtils {

    public static <T> T newUiThreadProxy(final T mvpView) {
        return (T) Proxy.newProxyInstance(mvpView.getClass().getClassLoader(), mvpView.getClass().getInterfaces(), new InvocationHandler() {
            @Override
            public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
                if (Looper.myLooper() != Looper.getMainLooper()) {
                    UseCase.executeOnMainThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                method.invoke(mvpView, args);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } else {
                    method.invoke(mvpView, args);
                }

                return null;
            }
        });
    }


    public static <T> T newUiThreadWeakProxy(final T mvpView) {
        final WeakReference<T> ref = new WeakReference<>(mvpView);

        return (T) Proxy.newProxyInstance(mvpView.getClass().getClassLoader(), mvpView.getClass().getInterfaces(), new InvocationHandler() {
            @Override
            public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
                final T raw = ref.get();
                if (raw == null) {
                    return null;
                }

                if (Looper.myLooper() != Looper.getMainLooper()) {
                    UseCase.executeOnMainThread(new Runnable() {
                        @Override
                        public void run() {
                            final T raw2 = ref.get();
                            if (raw2 == null) {
                                return;
                            }

                            try {
                                method.invoke(raw2, args);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } else {
                    method.invoke(raw, args);
                }

                return null;
            }
        });
    }


}
