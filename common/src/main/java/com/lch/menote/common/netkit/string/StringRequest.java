package com.lch.menote.common.netkit.string;

import android.support.annotation.NonNull;

import com.lch.menote.common.netkit.NetKit;

import java.util.Iterator;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by lichenghang on 2017/12/3.
 */

public class StringRequest {

    public <T> void get(@NonNull final StringRequestParams params, @NonNull final Parser<T> parser, final Callback<T> callback) {

        NetKit.runAsync(new Runnable() {
            @Override
            public void run() {
                try {
                    Request.Builder requestBuilder = new Request.Builder();


                    String url = params.getUrl();
                    if (!url.contains("?")) {
                        url += "?";
                    }
                    if (!url.endsWith("?")) {
                        url += "&";
                    }

                    StringBuilder sb = new StringBuilder(url);

                    Iterator<Map.Entry<String, String>> paramIter = params.textParams();
                    while (paramIter.hasNext()) {
                        Map.Entry<String, String> next = paramIter.next();
                        sb.append(next.getKey()).append("=").append(next.getValue()).append("&");
                    }
                    url = sb.toString();

                    Iterator<Map.Entry<String, String>> headers = params.headers();
                    while (headers.hasNext()) {
                        Map.Entry<String, String> next = paramIter.next();
                        requestBuilder.addHeader(next.getKey(), next.getValue());
                    }

                    requestBuilder.url(url)
                            .get();


                    final Response response = NetKit.client().newCall(requestBuilder.build()).execute();
                    if (!response.isSuccessful()) {
                        if (callback != null) {
                            NetKit.runInUI(new Runnable() {
                                @Override
                                public void run() {
                                    callback.onFail(response.message());
                                }
                            });
                        }
                        return;
                    }

                    ResponseBody body = response.body();
                    if (body == null) {
                        if (callback != null) {
                            NetKit.runInUI(new Runnable() {
                                @Override
                                public void run() {
                                    callback.onFail("response body is null.");
                                }
                            });
                        }
                        return;
                    }

                    final T result = parser.parse(body.string());

                    if (callback != null) {
                        NetKit.runInUI(new Runnable() {
                            @Override
                            public void run() {
                                callback.onSuccess(result);
                            }
                        });
                    }


                } catch (final Throwable e) {
                    e.printStackTrace();

                    if (callback != null) {
                        NetKit.runInUI(new Runnable() {
                            @Override
                            public void run() {
                                callback.onFail(e.getMessage());
                            }
                        });
                    }
                }


            }
        });
    }


    public <T> void post(@NonNull final StringRequestParams params, @NonNull final Parser<T> parser, final Callback<T> callback) {

        NetKit.runAsync(new Runnable() {
            @Override
            public void run() {
                try {
                    Request.Builder requestBuilder = new Request.Builder();
                    FormBody.Builder formBuilder = new FormBody.Builder();

                    String url = params.getUrl();


                    Iterator<Map.Entry<String, String>> paramIter = params.textParams();
                    while (paramIter.hasNext()) {
                        Map.Entry<String, String> next = paramIter.next();
                        formBuilder.add(next.getKey(), next.getValue());
                    }

                    Iterator<Map.Entry<String, String>> headers = params.headers();
                    while (headers.hasNext()) {
                        Map.Entry<String, String> next = paramIter.next();
                        requestBuilder.addHeader(next.getKey(), next.getValue());
                    }

                    if (params.getRequestBody() != null) {
                        requestBuilder.url(url)
                                .post(params.getRequestBody());
                    } else {
                        requestBuilder.url(url)
                                .post(formBuilder.build());
                    }


                    final Response response = NetKit.client().newCall(requestBuilder.build()).execute();
                    if (!response.isSuccessful()) {
                        if (callback != null) {
                            NetKit.runInUI(new Runnable() {
                                @Override
                                public void run() {
                                    callback.onFail(response.message());
                                }
                            });
                        }
                        return;
                    }

                    ResponseBody body = response.body();
                    if (body == null) {
                        if (callback != null) {
                            NetKit.runInUI(new Runnable() {
                                @Override
                                public void run() {
                                    callback.onFail("response body is null.");
                                }
                            });
                        }
                        return;
                    }

                    final T result = parser.parse(body.string());

                    if (callback != null) {
                        NetKit.runInUI(new Runnable() {
                            @Override
                            public void run() {
                                callback.onSuccess(result);
                            }
                        });
                    }


                } catch (final Throwable e) {
                    e.printStackTrace();

                    if (callback != null) {
                        NetKit.runInUI(new Runnable() {
                            @Override
                            public void run() {
                                callback.onFail(e.getMessage());
                            }
                        });
                    }
                }


            }
        });
    }


}
