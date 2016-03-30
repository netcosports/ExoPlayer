package com.google.android.exoplayer.util;

import android.content.Context;

import com.google.android.exoplayer.upstream.DefaultHttpDataSource;
import com.google.android.exoplayer.upstream.DefaultUriDataSource;
import com.google.android.exoplayer.upstream.OkHttpDataSource;
import com.google.android.exoplayer.upstream.TransferListener;
import com.google.android.exoplayer.upstream.UriDataSource;

import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.concurrent.TimeUnit;

import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;

/**
 * Created by eric on 30/03/16.
 */
public class OkHttpUtil {
    public static DefaultUriDataSource getDefaultUriDataSource(Context context, String userAgent) {
        return getDefaultUriDataSource(context, null, userAgent, false);
    }

    public static DefaultUriDataSource getDefaultUriDataSource(Context context, TransferListener listener, String userAgent) {
        return getDefaultUriDataSource(context, listener, userAgent, false);
    }

    public static DefaultUriDataSource getDefaultUriDataSource(Context context, TransferListener listener, String userAgent,
                                                               boolean allowCrossProtocolRedirects) {
        return getDefaultUriDataSource(context, listener,
                new OkHttpDataSource(getDefaultOkHttpClient(allowCrossProtocolRedirects), userAgent, null, listener));
    }

    public static DefaultUriDataSource getDefaultUriDataSource(Context context, TransferListener bandwidthMeter,
                                                               UriDataSource httpDataSource) {
        return new DefaultUriDataSource(context, bandwidthMeter, httpDataSource);
    }

    private static OkHttpClient getDefaultOkHttpClient(boolean allowCrossProtocolRedirects) {
        OkHttpClient okHttpClient = new OkHttpClient();
        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        okHttpClient.newBuilder().followRedirects(true).followSslRedirects(allowCrossProtocolRedirects)
                .readTimeout(DefaultHttpDataSource.DEFAULT_READ_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)
                .connectTimeout(DefaultHttpDataSource.DEFAULT_CONNECT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)
                .cookieJar(new JavaNetCookieJar(cookieManager));
        return okHttpClient;
    }

}
