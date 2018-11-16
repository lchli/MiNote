package com.lch.menote;

import com.lch.netkit.common.mvc.ControllerCallback;
import com.lch.netkit.common.mvc.ResponseValue;

public abstract class UseCase<P, R> {
    private static final TaskExecutor def = new DefaultTaskExecutor();

    public TaskExecutor getTaskExecutor() {
        return def;
    }

    public void invokeAsync(final P params, final ControllerCallback<R> result) {

        getTaskExecutor().executeOnDiskIO(new Runnable() {
            @Override
            public void run() {

                final ResponseValue<R> r = execute(params);

                getTaskExecutor().executeOnMainThread(new Runnable() {
                    @Override
                    public void run() {
                        if (r.hasError()) {
                            result.onError(r.code, r.getErrorMsg());
                        } else {
                            result.onSuccess(r.data);
                        }
                    }
                });

            }
        });
    }


    public ResponseValue<R> invokeSync(P params) {
        return execute(params);
    }


    protected abstract ResponseValue<R> execute(P parameters);

}
