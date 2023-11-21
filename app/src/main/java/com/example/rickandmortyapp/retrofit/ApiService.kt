package com.example.rickandmortyapp.retrofit

import com.example.rickandmortyapp.pojo.character.Result
import com.example.rickandmortyapp.pojo.location.Location
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("location")
    fun getLocations(@Query("page") page: Int): Call<Location>

    @GET("character/{ids}")
    fun getCharactersByLocId(@Path("ids") ids: ArrayList<String>): Call<List<Result>>

}