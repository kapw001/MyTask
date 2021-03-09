package com.mysystemtest.helper;

import com.jakewharton.retrofit2.adapter.rxjava2.HttpException;
import com.mysystemtest.base.BaseActivity;
import com.mysystemtest.base.BaseView;

import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.SocketTimeoutException;

import io.reactivex.observers.DisposableObserver;
import okhttp3.ResponseBody;

public abstract class CallbackWrapper<T> extends DisposableObserver<T> {  //BaseView is just a reference of a View in MVP
    private WeakReference<BaseActivity> weakReference;

    public CallbackWrapper(BaseActivity view) {
        this.weakReference = new WeakReference<>(view);
        view.showProgress();
    }

    protected abstract void onSuccess(T t);

    @Override
    public void onNext(T t) {
        BaseActivity view = weakReference.get();
        view.hideProgress();
        //You can return StatusCodes of different cases from your API and handle it here. I usually include these cases on BaseResponse and iherit it from every Response
        onSuccess(t);
    }

    @Override
    public void onError(Throwable e) {
        BaseActivity view = weakReference.get();
        view.hideProgress();
        if (e instanceof HttpException) {
            ResponseBody responseBody = ((HttpException) e).response().errorBody();
            view.onUnknownError(getErrorMessage(responseBody));
        } else if (e instanceof SocketTimeoutException) {
            view.onTimeout();
        } else if (e instanceof IOException) {
            view.onNetworkError();
        } else {
            view.onUnknownError(e.getMessage());
        }
    }

    @Override
    public void onComplete() {

    }

    private String getErrorMessage(ResponseBody responseBody) {
        try {
            JSONObject jsonObject = new JSONObject(responseBody.string());
            return jsonObject.getString("message");
        } catch (Exception e) {
            return e.getMessage();
        }
    }
}