package com.example.kotlinarhitecturecomponents.dagger.module

import android.util.Log
import com.example.kotlinarhitecturecomponents.BuildConfig
import com.example.kotlinarhitecturecomponents.data.repository.LanguageRemote
import com.example.kotlinarhitecturecomponents.remote.LanguageRemoteImpl
import com.example.kotlinarhitecturecomponents.remote.api.RetrofitApiInterfaceNetwork
import com.example.kotlinarhitecturecomponents.remote.mapper.RemoteEntityMapper
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class NetworkModule {

    private val URL = "http://10.0.2.2:8000/api/"

    @Provides
    fun getHeaders(): HashMap<String, String> {
        val params = HashMap<String, String>()
        params.put("Content-Type", "application/json")
        return params
    }


    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        return interceptor
    }


    @Provides
    fun provideRetrofit(gson: Gson, okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .client(okHttpClient)
            .baseUrl(URL).build()
    }


    @Provides
    fun providesGson(): Gson {
        return GsonBuilder().create()
    }

    @Provides
    fun provideNetworkService(retrofit: Retrofit): RetrofitApiInterfaceNetwork {
        return retrofit.create(RetrofitApiInterfaceNetwork::class.java)
    }

    @Provides
    fun getTimeOut(): Int {
        return 30
    }


    @Provides
    fun provideOkHttpClientDefault(interceptor: HttpLoggingInterceptor, headers: HashMap<String, String>): OkHttpClient {
        val okBuilder = OkHttpClient.Builder()
        okBuilder.addInterceptor(interceptor)
        okBuilder.addInterceptor { chain ->
            val request = chain.request()
            val builder = request.newBuilder()

            if (headers.size > 0) {
                for ((key, value) in headers) {
                    builder.addHeader(key, value)
                    Log.e(key, value)
                }
            }
            chain.proceed(builder.build())
        }

        val timeout = getTimeOut()
        okBuilder.connectTimeout(timeout.toLong(), TimeUnit.SECONDS)
        okBuilder.readTimeout(timeout.toLong(), TimeUnit.SECONDS)
        okBuilder.writeTimeout(timeout.toLong(), TimeUnit.SECONDS)

        return okBuilder.build()
    }


    @Provides
    @Singleton
    fun provideRemoteModelConverter(): RemoteEntityMapper {
        return RemoteEntityMapper()
    }

    @Provides
    @Singleton
    fun provideRemoteRepository(retrofitApiInterfaceNetwork: RetrofitApiInterfaceNetwork, remoteEntityMapper: RemoteEntityMapper): LanguageRemote {
        return LanguageRemoteImpl(retrofitApiInterfaceNetwork, remoteEntityMapper)
    }

}