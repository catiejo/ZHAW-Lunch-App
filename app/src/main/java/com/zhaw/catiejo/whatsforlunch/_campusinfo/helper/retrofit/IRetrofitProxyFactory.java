package com.zhaw.catiejo.whatsforlunch._campusinfo.helper.retrofit;

import retrofit.converter.Converter;

/**
 * Factory to create a new Retrofit proxy.
 */
public interface IRetrofitProxyFactory {

    /**
     * Creates new a new Retrofit proxy from the given interface definition that calls the specified endpoint and
     * converts requests and responses using the given converter.
     *
     * @param endpoint Base URL of the remote server API to call.
     * @param type Proxy interface definition.
     * @param converter Converter to use to convert requests and responses to and from the desired format.
     * @param <T> Type of the proxy interface.
     * @return Retrofit proxy.
     */
    <T> T createProxy(String endpoint, Class<T> type, Converter converter);
}
