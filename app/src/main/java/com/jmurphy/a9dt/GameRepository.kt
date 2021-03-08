package com.jmurphy.a9dt

import android.content.Context
import androidx.lifecycle.MutableLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class GameRepository(context: Context) {

    val baseUrl = context.getString(R.string.base_url)

    val httpClient = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
  //      .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
//         .client(movieApiClient)
        .build()


    val apiClient = httpClient.create(GameEndpoints::class.java)

    val movesListLiveData = MutableLiveData<List<Int>>()

    fun sendPlayerMove(moves: List<Int>){
        val param = moves.toString()
        apiClient.getMoves(param).enqueue(object: Callback<List<Int>>{
            override fun onResponse(call: Call<List<Int>>, response: Response<List<Int>>) {
                movesListLiveData.postValue(response.body())
            }

            override fun onFailure(call: Call<List<Int>>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })

    }

}