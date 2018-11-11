package com.zhaw.catiejo.whatsforlunch._campusinfo.helper.retrofit;

import com.google.common.base.Optional;
import com.squareup.okhttp.OkHttpClient;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.Converter;

import java.net.ResponseCache;

/**
 * Factory that creates a Retrofit proxy to the given endpoint using the supplied proxy interface and converter
 * instance.
 */
public class DefaultRetrofitProxyFactory implements IRetrofitProxyFactory {

    private final Optional<ResponseCache> optionalCache;

    public DefaultRetrofitProxyFactory() {
        this.optionalCache = Optional.absent();
    }

    public DefaultRetrofitProxyFactory(ResponseCache cache) {
        this.optionalCache = Optional.fromNullable(cache);
    }

    @Override
    public <T> T createProxy(String endpoint, Class<T> type, Converter converter) {
        OkHttpClient okHttpClient = new OkHttpClient();
        if (optionalCache.isPresent()) {
            okHttpClient.setResponseCache(optionalCache.get());
        }

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setServer(endpoint)
                .setClient(new OkClient(okHttpClient))
                .setConverter(converter)
                .build();

        return restAdapter.create(type);
    }
}
