package com.social.sign.in.data.remote;

import android.content.Context;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstance {

    /*
     * Note :  We can set timeouts settings on the underlying HTTP client.
     * If we don’t specify a client, Retrofit will create one with default connect and read timeouts.
     * By default, Retrofit uses the following timeouts :
     *                                                      Connection timeout: 10 seconds
     *                                                      Read timeout: 10 seconds
     *                                                      Write timeout: 10 seconds
     */
    public static final int HTTP_CONNECT_TIMEOUT        = 1;
    public static final int HTTP_READ_TIMEOUT           = 30;
    public static final int HTTP_WRITE_TIMEOUT          = 15;

    private static Retrofit retrofitInstance = null;

    /*
     * This is a Private Constructor
     * So that nobody can create an object with this constructor, from outside of this class.
     * We will achieve Singleton
     */
    private RetrofitInstance() {
    }

    public static Retrofit getInstance(Context context, String baseUrl) {
        if ( retrofitInstance == null)
        {
            /* thread safe Singleton implementation */
            synchronized (RetrofitInstance.class)
            {
                if (retrofitInstance == null)
                {
                    retrofitInstance = getRetrofitInstance(context, baseUrl);
                }
            }
        }
        return  retrofitInstance;
    }


    public static Retrofit getRetrofitInstance(Context context, String baseUrl) {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create();

        OkHttpClient.Builder okHttpClientBuilder = getOkHttpClientBuilderInstance(context);
        OkHttpClient okHttpClient = okHttpClientBuilder.build();

        Retrofit.Builder builder = new Retrofit.Builder();
        builder.baseUrl(baseUrl);
        builder.client(okHttpClient);
        builder.addConverterFactory(GsonConverterFactory.create(gson));

        Retrofit retrofitInstance = builder.build();
        return retrofitInstance;
    }

    private static OkHttpClient.Builder getOkHttpClientBuilderInstance(Context context) {
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();

        okHttpClientBuilder.connectTimeout(HTTP_CONNECT_TIMEOUT, TimeUnit.MINUTES);
        okHttpClientBuilder.writeTimeout(HTTP_WRITE_TIMEOUT, TimeUnit.SECONDS);
        okHttpClientBuilder.readTimeout(HTTP_READ_TIMEOUT, TimeUnit.SECONDS);

        return okHttpClientBuilder;
    }
}
