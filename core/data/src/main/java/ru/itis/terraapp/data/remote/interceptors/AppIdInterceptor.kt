package ru.itis.terraapp.data.remote.interceptors

import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AppIdInterceptor @Inject constructor(): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val url = chain.request().url.newBuilder()
            .addQueryParameter("appid", "d9990220ce61da50cdc4fa13b0ae4a84")
        val request = chain.request().newBuilder().url(url.build())

        return chain.proceed(request.build())
    }
}