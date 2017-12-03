package com.lch.menote.common.netkit.string;

import com.lch.menote.common.netkit.NetKit;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by lichenghang on 2017/10/3.
 */

public final class Retrofits {

    private static final Map<String, Retrofit> fits = new HashMap<>();

    public static Retrofit get(String baseUrl) {
        Retrofit r = fits.get(baseUrl);

        if (r == null) {
            synchronized (Retrofits.class) {
                if (fits.get(baseUrl) == null) {
                    r = new Retrofit.Builder()
                            .baseUrl(baseUrl)
                            .addConverterFactory(GsonConverterFactory.create())
                            .callFactory(NetKit.client())
                            .build();
                    fits.put(baseUrl, r);
                }
            }
        }

        return r;
    }


}
