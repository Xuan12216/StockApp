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
import retrofit2.http.Url;

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
    Single<ResponseBody> 回後準進場(@Body RequestBody var1, @Header("accept") String var0, @Header("Authorization") String authorization);

    @POST("api/logic/strategy/Q101")
    Single<ResponseBody> 起漲策略(@Body RequestBody var1, @Header("accept") String var0, @Header("Authorization") String authorization);

    @POST("api/logic/strategy/R101")
    Single<ResponseBody> 雙線黃金交叉(@Body RequestBody var1, @Header("accept") String var0, @Header("Authorization") String authorization);

    @POST("api/logic/strategy/C101")
    Single<ResponseBody> 長抱(@Body RequestBody var1, @Header("accept") String var0, @Header("Authorization") String authorization);

    @POST("api/logic/strategy/M101")
    Single<ResponseBody> 盤中強勢(@Body RequestBody var1, @Header("accept") String var0, @Header("Authorization") String authorization);

    @POST("api/logic/strategy/P101")
    Single<ResponseBody> 盤中排行_多(@Body RequestBody var1, @Header("accept") String var0, @Header("Authorization") String authorization);

    @POST("api/logic/strategy/N101")
    Single<ResponseBody> 一點鐘_多(@Body RequestBody var1, @Header("accept") String var0, @Header("Authorization") String authorization);

    //=====================================

    @POST("api/logic/strategy/B201")
    Single<ResponseBody> 頭低底低(@Body RequestBody var1, @Header("accept") String var0, @Header("Authorization") String authorization);

    @POST("api/logic/strategy/B202")
    Single<ResponseBody> 彈後準進場(@Body RequestBody var1, @Header("accept") String var0, @Header("Authorization") String authorization);

    @POST("api/logic/strategy/Q201")
    Single<ResponseBody> 起跌策略(@Body RequestBody var1, @Header("accept") String var0, @Header("Authorization") String authorization);

    @POST("api/logic/strategy/R201")
    Single<ResponseBody> 雙線死亡交叉(@Body RequestBody var1, @Header("accept") String var0, @Header("Authorization") String authorization);

    @POST("api/logic/strategy/M201")
    Single<ResponseBody> 盤中弱勢(@Body RequestBody var1, @Header("accept") String var0, @Header("Authorization") String authorization);

    @POST("api/logic/strategy/P201")
    Single<ResponseBody> 盤中排行_空(@Body RequestBody var1, @Header("accept") String var0, @Header("Authorization") String authorization);

    @POST("api/logic/strategy/N201")
    Single<ResponseBody> 一點鐘_空(@Body RequestBody var1, @Header("accept") String var0, @Header("Authorization") String authorization);

    //=====================================

    @POST("api/logic/strategy/A101")
    Single<ResponseBody> 等突破(@Body RequestBody var1, @Header("accept") String var0, @Header("Authorization") String authorization);

    @POST("api/logic/strategy/A103")
    Single<ResponseBody> 高檔等回檔(@Body RequestBody var1, @Header("accept") String var0, @Header("Authorization") String authorization);

    @POST("api/logic/strategy/A104")
    Single<ResponseBody> 回檔等上漲(@Body RequestBody var1, @Header("accept") String var0, @Header("Authorization") String authorization);

    @POST("api/logic/strategy/A201")
    Single<ResponseBody> 等跌破(@Body RequestBody var1, @Header("accept") String var0, @Header("Authorization") String authorization);

    @POST("api/logic/strategy/A203")
    Single<ResponseBody> 低檔等反彈(@Body RequestBody var1, @Header("accept") String var0, @Header("Authorization") String authorization);

    @POST("api/logic/strategy/A204")
    Single<ResponseBody> 反彈等下跌(@Body RequestBody var1, @Header("accept") String var0, @Header("Authorization") String authorization);

    //=====================================

    @POST
    Single<ResponseBody> 操盤綫_趨勢綫(@Url String url, @Body RequestBody var1, @Header("accept") String var0, @Header("Authorization") String authorization);
}

