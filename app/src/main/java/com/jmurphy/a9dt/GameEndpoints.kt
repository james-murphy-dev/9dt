package com.jmurphy.a9dt

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface GameEndpoints {

    @GET
    fun getMoves(@Query("moves") moves: List<Int>): Call<List<Int>>

}