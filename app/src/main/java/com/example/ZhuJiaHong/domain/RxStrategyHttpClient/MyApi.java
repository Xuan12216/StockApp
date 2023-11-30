package com.example.ZhuJiaHong.domain.RxStrategyHttpClient;

import io.reactivex.Single;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface MyApi {
    @FormUrlEncoded
    @POST("auth") // 这里根据实际情况填写路径
    Single<TokenResponse> getStrategyToken(
            @Field("appId") String appId,
            @Field("appSecret") String appSecret
    );

    //
    @POST("api/logic/strategy/B101")
    Single<ResponseBody> 頭高底高(@Body RequestBody var1, @Header("accept") String var0, @Header("Authorization") String authorization);

    @POST("api/logic/strategy/B102")
    Single<ResponseBody> 回後進場(@Body RequestBody var1, @Header("accept") String var0, @Header("Authorization") String authorization);

    @POST("api/logic/strategy/C101")
    Single<ResponseBody> 長抱(@Body RequestBody var1, @Header("accept") String var0, @Header("Authorization") String authorization);

    @POST("api/logic/strategy/M101")
    Single<ResponseBody> 盤中強勢(@Body RequestBody var1, @Header("accept") String var0, @Header("Authorization") String authorization);

    @POST("api/logic/strategy/P101")
    Single<ResponseBody> 盤中排行(@Body RequestBody var1, @Header("accept") String var0, @Header("Authorization") String authorization);

    @POST("api/logic/strategy/N101")
    Single<ResponseBody> 一點鐘(@Body RequestBody var1, @Header("accept") String var0, @Header("Authorization") String authorization);
}
