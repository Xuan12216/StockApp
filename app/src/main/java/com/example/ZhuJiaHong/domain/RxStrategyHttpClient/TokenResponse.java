package com.example.ZhuJiaHong.domain.RxStrategyHttpClient;

import com.google.gson.annotations.SerializedName;

public class TokenResponse {
    @SerializedName("token")
    private String token;

    // 可能还有其他与 Token 相关的字段，根据实际情况定义

    public String getToken() {
        return token;
    }
}
