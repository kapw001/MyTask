package com.mysystemtest.ui.userlist;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

import com.mysystemtest.R;
import com.mysystemtest.base.BaseActivity;
import com.mysystemtest.dao.DatabaseClient;
import com.mysystemtest.helper.CallbackWrapper;
import com.mysystemtest.network.RetrofitAdapter;
import com.mysystemtest.network.response.User;
import com.mysystemtest.network.response.UserResponse;
import com.mysystemtest.ui.userdetails.UserDetailsActivity;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class UserListActivity extends BaseActivity implements UserListAdapter.OnItemClickListener {

    @BindView(R.id.userListView)
    RecyclerView mUserListView;
    private UserListAdapter mUserListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        ButterKnife.bind(this);

        setTitle(getString(R.string.title_user));

        initView();

        fetchUserList();

    }

    private void initView() {

        mUserListView.setLayoutManager(new LinearLayoutManager(this));
        mUserListAdapter = new UserListAdapter(this, new ArrayList<User>());

        mUserListView.setAdapter(mUserListAdapter);

        mUserListAdapter.setOnItemClickListener(this);

    }

    private void fetchUserList() {


        disposable.add(RetrofitAdapter.getNetworkApiServiceClient().fetchUser()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new CallbackWrapper<UserResponse>(this) {
                    @Override
                    protected void onSuccess(UserResponse userResponse) {

                        handleUserResponse(userResponse);

                    }

                }));


    }


    private void handleUserResponse(UserResponse userResponse) {

        if (userResponse != null) {

            List<User> userList = userResponse.getData();

            if (userList != null && !userList.isEmpty()) {

                showUserList(userList);

                AsyncTask.execute(() -> DatabaseClient.getInstance(this)
                        .getAppDatabase().userDao().insert(userList));


            }

        }

    }

    @Override
    public void fetchOfflineData() {

        disposable.add(DatabaseClient.getInstance(this)
                .getAppDatabase().userDao().getAll().subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(this::showUserList));


    }


    private void showUserList(List<User> userList) {

        mUserListAdapter.updateList(userList);

    }

    @Override
    public void onItemClicked(View view, User user, int position) {

        UserDetailsActivity.startUserDetails(this, user.getId());

    }


}
