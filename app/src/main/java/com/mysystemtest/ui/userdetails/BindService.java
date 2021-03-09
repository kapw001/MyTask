package com.mysystemtest.ui.userdetails;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;

public class BindService extends Service {
    private int count = 5;
    private boolean quit;
    private MyBinder binder = new MyBinder();

    private ObservableEmitter<Integer> countObserver;
    private Observable<Integer> countObservable;

    // My Binder
    public class MyBinder extends Binder {


        public Observable<Integer> getCountObserber() {
            return observePressure();
        }


        public int getCount() {
            // get the counting status：count
            return count;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public Observable<Integer> observePressure() {
        if (countObservable == null) {
            countObservable = Observable.create(emitter -> countObserver = emitter);
            countObservable = countObservable.share();
        }
        return countObservable;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // counting work
        new Thread() {
            @Override
            public void run() {
                while (!quit) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                    }

                    if (count > 0) {
                        count--;

                        if (countObserver != null) {
                            countObserver.onNext(count);
                        }

                    }
                }
            }
        }.start();
    }

    // invoke when the service unbind
    @Override
    public boolean onUnbind(Intent intent) {
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.quit = true;
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
        this.quit = true;
    }

}