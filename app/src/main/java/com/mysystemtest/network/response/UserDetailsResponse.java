package com.mysystemtest.network.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UserDetailsResponse implements Serializable {

    @SerializedName("data")
    @Expose
    private UserDetails data;
    @SerializedName("support")
    @Expose
    private Support support;
    private final static long serialVersionUID = -3990917082012917627L;

    public UserDetails getData() {
        return data;
    }

    public void setData(UserDetails data) {
        this.data = data;
    }

    public Support getSupport() {
        return support;
    }

    public void setSupport(Support support) {
        this.support = support;
    }

}