package com.example.retrofitvsvolley

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Url

interface JsonPlaceHolderApi {
    @GET("https://api.weatherapi.com/v1/forecast.json?key=6c8b10d1db354c2894e105541222608&q=lviv&days=3&aqi=no&alerts=no")
    fun getPosts(  ): Call<WeatherModel>
}