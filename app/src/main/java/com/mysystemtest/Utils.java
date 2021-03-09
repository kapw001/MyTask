package com.mysystemtest;

import android.content.Context;

import com.bumptech.glide.Glide;

import de.hdodenhof.circleimageview.CircleImageView;

public class Utils {


    public static String getId(Integer id) {

        return String.valueOf(id);
    }

    public static String getName(String firstName, String lastName) {

        return firstName + " " + lastName;
    }

    public static void loadImage(Context context, String avatar, CircleImageView circleImageView) {

        Glide.with(context).load(avatar).into(circleImageView);

    }
}
