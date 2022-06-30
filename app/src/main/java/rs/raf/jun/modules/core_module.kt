package rs.raf.jun.modules

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.BuildConfig
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import rs.raf.jun.data.datasources.local.MyDataBase
import rs.raf.jun.data.datasources.remote.mock.news.MockNewsServiceImpl
import rs.raf.jun.data.datasources.remote.mock.stock.MockStockServiceImpl
import java.util.*
import java.util.concurrent.TimeUnit

val coreModule = module {
    single<SharedPreferences> {
        androidApplication().getSharedPreferences(androidApplication().packageName, Context.MODE_PRIVATE)
    }

    single { Room.databaseBuilder(androidContext(), MyDataBase::class.java, "RafTradingDb")
        .fallbackToDestructiveMigration()
        .build() }

    single { createRetrofit(moshi = get(), httpClient = get(), "https://b311eca0-15ae-40e9-9185-c6d4da7ae2c7.mock.pstmn.io/rma/getNews/") }

    single { createMoshi() }

    single { createOkHttpClient() }
}

fun createMoshi(): Moshi {
    return Moshi.Builder()
        .add(Date::class.java, Rfc3339DateJsonAdapter())
        .build()
}

fun createRetrofit(moshi: Moshi,
                   httpClient: OkHttpClient,
                   baseUrl: String
): Retrofit {
    return Retrofit.Builder()
        .baseUrl(baseUrl)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(MoshiConverterFactory.create(moshi).asLenient())
        .client(httpClient)
        .build()
}

fun createOkHttpClient(): OkHttpClient {
    val httpClient = OkHttpClient.Builder()
    httpClient.readTimeout(60, TimeUnit.SECONDS)
    httpClient.connectTimeout(60, TimeUnit.SECONDS)
    httpClient.writeTimeout(60, TimeUnit.SECONDS)

    if (BuildConfig.DEBUG) {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        httpClient.addInterceptor(logging)
    }

    return httpClient.build()
}

inline fun <reified T> create(retrofit: Retrofit): T  {
    return retrofit.create(T::class.java)
}

fun createMockNewsService(): MockNewsServiceImpl {
    return MockNewsServiceImpl()
}

fun createMockStockService(): MockStockServiceImpl {
    return MockStockServiceImpl()
}
