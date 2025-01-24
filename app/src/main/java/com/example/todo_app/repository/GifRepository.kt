package com.example.todo_app.repository

import android.util.Log
import com.example.todo_app.BuildConfig.GIPHY_API_KEY
import com.example.todo_app.model.GiphyResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

public interface GifApi {
    @Headers(
        "Accept: application/json"
    )

    @GET("random")
    abstract fun getRandomGif(
        @Query("api_key") apiKey: String,
        @Query("tag") tags: String
    ): Call<GiphyResponse?>?
}

interface GifRepository {
    abstract fun getRandomCongratulationGif(callback: (Response<GiphyResponse?>) -> Unit)
}

class GifRepositoryImpl : GifRepository {
    override fun getRandomCongratulationGif(callback: (Response<GiphyResponse?>) -> Unit) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.giphy.com/v1/gifs/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(GifApi::class.java)

        val call: Call<GiphyResponse?>? = api.getRandomGif(
            apiKey = GIPHY_API_KEY,
            tags = "congratulation congrats goodjob"
        )

        call!!.enqueue(object : Callback<GiphyResponse?> {
            override fun onResponse(
                call: Call<GiphyResponse?>,
                response: Response<GiphyResponse?>
            ) {
                if (response.isSuccessful) {
                    // callback(response.body()?.data?.images?.original?.url ?: "")
                    callback(response)
                }
            }

            override fun onFailure(call: Call<GiphyResponse?>, t: Throwable) {
                Log.e("GIF_API", "Error: " + t.message.toString())
            }
        })
    }
}