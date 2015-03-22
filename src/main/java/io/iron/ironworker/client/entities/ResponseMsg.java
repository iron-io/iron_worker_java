package io.iron.ironworker.client.entities;

import com.google.gson.annotations.SerializedName;

public class ResponseMsg {
    @SerializedName("msg")
    String msg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
