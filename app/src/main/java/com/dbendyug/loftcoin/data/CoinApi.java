package com.dbendyug.loftcoin.data;

import com.dbendyug.loftcoin.BuildConfig;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CoinApi {

    String KEY_HEADER = "X-CMC_PRO_API_KEY";

    @GET("cryptocurrency/listings/latest")
    Call<Listings> listings (@Query("convert") String convert);
    class KeyInterceptor implements Interceptor{

        @NotNull
        @Override
        public Response intercept(@NotNull Chain chain) throws IOException {
            return chain.proceed(chain.request().newBuilder().addHeader(KEY_HEADER, BuildConfig.COIN_API_KEY).build());
        }
    }
}
