package com.lzs.netwrok.http;

import android.util.Log;

import com.lzs.netwrok.app.Globle;
import com.lzs.netwrok.app.MyApp;
import com.lzs.netwrok.utils.HttpUtil;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class HttpManager {
    private static volatile HttpManager httpManager;

    private HttpManager() {
    }

    public static HttpManager getInstance() {
        if (httpManager == null) {
            synchronized (HttpManager.class) {
                if (httpManager == null) {
                    httpManager = new HttpManager();
                }
            }
        }
        return httpManager;
    }

    private Retrofit getRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(Globle.HTTP_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(getOkhttpClient())
                .build();
    }

    public <T> T getServer(Class<T> tClass) {
        return getRetrofit().create(tClass);
    }

    private OkHttpClient getOkhttpClient() {
        //日志过滤器
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                try {
                    String text = URLDecoder.decode(message, "utf-8");
                    Log.e("okhttp", "log: " + text);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    Log.e("okhttp", "log: " + message);
                }
            }
        });
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        //指定缓存路径
        Cache cache = new Cache(new File(MyApp.getMyApp().getCacheDir(), "Cache"), 1024 * 1024 * 10);
        MyCacheInterce myCacheInterce = new MyCacheInterce();
        return new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.SECONDS)
                .writeTimeout(5, TimeUnit.SECONDS)
                //失败自动重连
                .retryOnConnectionFailure(true)
                //添加日志拦截器
                .addInterceptor(httpLoggingInterceptor)
                //添加缓存拦截器
                .cache(cache)
                .addInterceptor(myCacheInterce)
                .addNetworkInterceptor(myCacheInterce)
                .build();
    }

    private class MyCacheInterce implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            if (!HttpUtil.isNetworkAvailable(MyApp.getMyApp())) {
                request = request.newBuilder().cacheControl(CacheControl.FORCE_CACHE).build();
            }
            //利用拦截器发送
            Response proceed = chain.proceed(request);
            if (HttpUtil.isNetworkAvailable(MyApp.getMyApp())) {
                int maxAge = 0;
                return proceed.newBuilder()
                        // 清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
                        .removeHeader("Pragma")
                        .header("Cache-Control", "public ,max-age=" + maxAge)
                        .build();
            } else {
                int maxStale = 15;
                return proceed.newBuilder()
                        // 清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
                        .removeHeader("Pragma")
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                        .build();
            }
        }
    }
}
