package com.mysystemtest.ui.userlist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mysystemtest.R;
import com.mysystemtest.Utils;
import com.mysystemtest.network.response.User;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.UserListViewHolder> {

    private Context mContext;
    private List<User> mUserList;
    private OnItemClickListener mOnItemClickListener;

    public UserListAdapter(Context mContext, List<User> mUserList) {
        this.mContext = mContext;
        this.mUserList = mUserList;
    }

    public void updateList(List<User> userList) {
        this.mUserList = new ArrayList<>();
        this.mUserList.addAll(userList);
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    @NonNull
    @Override
    public UserListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.user_row_item, parent, false);

        return new UserListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserListViewHolder holder, final int position) {

        final User user = mUserList.get(position);
        holder.bind(user);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mOnItemClickListener != null)
                    mOnItemClickListener.onItemClicked(view, user, position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mUserList.size();
    }

    public static class UserListViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.user_id)
        AppCompatTextView mUserId;
        @BindView(R.id.user_name)
        AppCompatTextView mUserName;

        public UserListViewHolder(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

        }

        public void bind(User user) {

            mUserId.setText(Utils.getId(user.getId()));
            mUserName.setText(Utils.getName(user.getFirstName(), user.getLastName()));

        }
    }


    public interface OnItemClickListener {

        void onItemClicked(View view, User user, int position);

    }

}
