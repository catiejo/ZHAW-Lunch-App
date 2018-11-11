package com.zhaw.catiejo.whatsforlunch._campusinfo.helper;

/**
 * Marks an object as Dagger container.
 */
public interface DaggerContainer {
    /**
     * Injects all dependencies into the given object.
     *
     * @param object Object to inject the dependencies into.
     */
    void inject(Object object);
}
