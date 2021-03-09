package com.mysystemtest.base;

import android.view.MenuItem;
import android.widget.Toast;

import com.mysystemtest.ProgressUtils;

import androidx.appcompat.app.AppCompatActivity;
import io.reactivex.disposables.CompositeDisposable;

public abstract class BaseActivity extends AppCompatActivity implements BaseView {

    protected CompositeDisposable disposable = new CompositeDisposable();


    public void setTitle(String title) {

        if (getSupportActionBar() != null) {

            getSupportActionBar().setTitle(title);
        }

    }

    public void setBackButtonEnabledWithTitle(String title) {

        if (getSupportActionBar() != null) {

            getSupportActionBar().setTitle(title);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void showToast(String msg) {

        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

    }

    public void showProgress() {
        ProgressUtils.showProgress(BaseActivity.this, "Loading...");
    }

    public void hideProgress() {

        ProgressUtils.hideProgress();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (disposable != null) {
            disposable.clear();
        }
    }

    @Override
    public void onUnknownError(String errorMessage) {

        showToast(errorMessage);
        fetchOfflineData();
    }

    @Override
    public void onTimeout() {
        showToast("TimeOut");
        fetchOfflineData();
    }

    @Override
    public void onNetworkError() {
        showToast("No network");
        fetchOfflineData();
    }


    public abstract void fetchOfflineData();
}
