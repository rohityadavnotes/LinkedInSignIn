package com.social.sign.in.data.remote;

import android.content.Context;
import com.social.sign.in.linkedin.LinkedInConstants;

public class ServiceGenerator {

    /**
     * Retrofit Service Generator class which initializes the calling RetrofitService
     *
     * @param context -  getApplicationContext.
     * @param serviceClass -  The Retrofit Service Interface class.
     */
    public static <S> S createService(Context context, Class<S> serviceClass) {
        return RetrofitInstance.getInstance(context, LinkedInConstants.LINKED_IN_API_HOST).create(serviceClass);
    }
}
