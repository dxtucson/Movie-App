package com.d.x.movie.app.network

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit


object ServiceApi {

    const val BASE_URL = "https://api.themoviedb.org/3/"
    const val API_KEY = "493512e575038781f5392ed83086b101"
    private var _api: MovieDbService? = null
    val api: MovieDbService
        get() {
            if (_api == null) {
                val interceptor = object : Interceptor {
                    @Throws(IOException::class)
                    override fun intercept(chain: Interceptor.Chain): Response {
                        val original = chain.request()
                        val originalHttpUrl = original.url
                        val url = originalHttpUrl.newBuilder()
                            .addQueryParameter("api_key", API_KEY)
                            .build()
                        val requestBuilder = original.newBuilder()
                            .url(url)
                        val request = requestBuilder.build()
                        return chain.proceed(request)
                    }
                }
                val httpLoggingInterceptor = HttpLoggingInterceptor()
                httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
                val client = OkHttpClient
                    .Builder()
                    .addInterceptor(httpLoggingInterceptor)
                    .addInterceptor(interceptor)
                    .callTimeout(10, TimeUnit.SECONDS)
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .build()
                val retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                _api = retrofit.create(MovieDbService::class.java)
            }
            return _api!!
        }
}
