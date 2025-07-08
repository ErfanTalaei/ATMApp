package org.bihe.finalproject.utils;

public interface ResultListener<T> {
    void onSuccess(T t);

    void onError(Throwable throwable);
}
