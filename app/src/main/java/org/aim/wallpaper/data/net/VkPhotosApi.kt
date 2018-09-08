package org.aim.wallpaper.data.net

import io.reactivex.Single
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.aim.wallpaper.BuildConfig
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface VkPhotosApi {

    @GET("photos.getAlbums")
    fun getAlbums(@Query("owner_id") ownerId: Long,
                  @Query("album_ids") albumsId: String,
                  @Query("need_covers") needCovers: Int,
                  @Query("photo_sizes") photoSize: Int,
                  @Query("access_token") accessToken: String): Single<AlbumsResponse>

    @GET("photos.get")
    fun get(@Query("owner_id") ownerId: Long,
            @Query("album_id") albumId: Long,
            @Query("access_token") accessToken: String): Single<PhotosResponse>

    @GET("photos.getById")
    fun getById(@Query("photos") photos: String,
                @Query("access_token") accessToken: String): Single<PhotoResponse>

    companion object {

        const val ROOT = "https://api.vk.com/method/"
        const val TIME_OUT = 120L

        val INSTANCE: VkPhotosApi by lazy {
            val builder = OkHttpClient.Builder()
            if (BuildConfig.DEBUG) {
                val interceptor = HttpLoggingInterceptor()
                interceptor.level = HttpLoggingInterceptor.Level.BODY
                builder.addInterceptor(interceptor)
            }
            builder.connectTimeout(TIME_OUT, TimeUnit.SECONDS)
                    .writeTimeout(TIME_OUT, TimeUnit.SECONDS)
                    .readTimeout(TIME_OUT, TimeUnit.SECONDS)
            val client = builder.build()
            val retrofit = Retrofit.Builder()
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .baseUrl(ROOT)
                    .build()

            retrofit.create(VkPhotosApi::class.java)
        }
    }
}