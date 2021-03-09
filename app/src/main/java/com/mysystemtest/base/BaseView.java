package com.mysystemtest.base;

public interface BaseView {
    void onUnknownError(String errorMessage);

    void onTimeout();

    void onNetworkError();

}
