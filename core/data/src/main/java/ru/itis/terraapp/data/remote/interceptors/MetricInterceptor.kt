package ru.itis.terraapp.data.remote.interceptors

import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class MetricInterceptor @Inject constructor(): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val url = chain.request().url.newBuilder()
            .addQueryParameter("units", "metric")
        val request = chain.request().newBuilder().url(url.build())

        return chain.proceed(request.build())
    }
}