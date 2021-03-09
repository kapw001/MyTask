package com.mysystemtest.ui.userdetails;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;

import com.mysystemtest.R;
import com.mysystemtest.Utils;
import com.mysystemtest.base.BaseActivity;
import com.mysystemtest.dao.DatabaseClient;
import com.mysystemtest.helper.CallbackWrapper;
import com.mysystemtest.network.RetrofitAdapter;
import com.mysystemtest.network.response.UserDetails;
import com.mysystemtest.network.response.UserDetailsResponse;

import androidx.appcompat.widget.AppCompatTextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class UserDetailsActivity extends BaseActivity {

    private static final String EXTRA_USER_ID = "user_id";
    @BindView(R.id.user_img)
    CircleImageView userImg;
    @BindView(R.id.user_name)
    AppCompatTextView userName;
    @BindView(R.id.email)
    AppCompatTextView email;
    @BindView(R.id.count_down)
    AppCompatTextView countDown;

    private BindService.MyBinder binder;

    private Intent bindIntent;
    private int id;

    public static void startUserDetails(Context context, int id) {
        Intent intent = new Intent(context, UserDetailsActivity.class);
        intent.putExtra(EXTRA_USER_ID, id);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);
        ButterKnife.bind(this);

        bindIntent = new Intent(this, BindService.class);


        setBackButtonEnabledWithTitle(getString(R.string.title));

        Intent intent = getIntent();

        if (intent != null && intent.hasExtra(EXTRA_USER_ID)) {

            id = intent.getIntExtra(EXTRA_USER_ID, 0);

            fetchUserDetails(id);

        }
        //bind Serivce
        bindService(bindIntent, conn, Service.BIND_AUTO_CREATE);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(conn);

    }

    private void updateUI(int count) {

        countDown.setText(String.format("%s Sec", Utils.getId(count)));

        if (count <= 0) {

            onBackPressed();

        }

    }

    private void fetchUserDetails(int id) {

        disposable.add(RetrofitAdapter.getNetworkApiServiceClient().fetchUserDetails(id)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new CallbackWrapper<UserDetailsResponse>(this) {
                    @Override
                    protected void onSuccess(UserDetailsResponse response) {

                        handleUserDetailsResponse(response);
                    }
                }));


    }

    private void handleUserDetailsResponse(UserDetailsResponse response) {

        if (response != null && response.getData() != null) {


            UserDetails userDetails = response.getData();

            if (userDetails != null) {

                showDetails(userDetails);
                AsyncTask.execute(() -> DatabaseClient.getInstance(this)
                        .getAppDatabase().userDetailsDao().insert(userDetails));

            }

        }

    }

    private void fetchOfflineData(int id) {

        disposable.add(DatabaseClient.getInstance(this)
                .getAppDatabase().userDetailsDao().getUserDetails(id).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(this::showDetails));


    }


    private void showDetails(UserDetails userDetails) {


        userName.setText(Utils.getName(userDetails.getFirstName(), userDetails.getLastName()));
        email.setText(userDetails.getEmail());

        Utils.loadImage(this, userDetails.getAvatar(), userImg);

    }



    @Override
    public void fetchOfflineData() {

        fetchOfflineData(id);


    }


    private ServiceConnection conn = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name
                , IBinder service) {

            binder = (BindService.MyBinder) service;

            disposable.add(binder.getCountObserber()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(integer -> updateUI(integer)));


        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };


}
