package com.example.retrofitvsvolley

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.GsonBuilder
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

class MainActivity : AppCompatActivity() {

    private lateinit var tvResponseFrom: TextView
    private lateinit var tvUserId: TextView
    private lateinit var tvId: TextView
    private lateinit var tvTitle: TextView
    private lateinit var tvBody: TextView
    private val key = "6c8b10d1db354c2894e105541222608"
    private val baseUrl = "https://api.weatherapi.com/v1/forecast.json?key=6c8b10d1db354c2894e105541222608&q=lviv&days=3&aqi=no&alerts=no"
    private val url = "https://api.weatherapi.com/"
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //getVolleyResponse()
        getRetrofitResponse()

        tvResponseFrom = findViewById(R.id.tvResponseFrom)
        tvUserId = findViewById(R.id.tvUserId)
        tvId = findViewById(R.id.tvId)
        tvTitle = findViewById(R.id.tvTitle)
        tvBody = findViewById(R.id.tvBody)




    }
    private fun setTextViews(
        responseFrom: String,
        userId: String,
        id: String,
        title: String,
        body: String
    ) {
        tvResponseFrom.text = "Response via =  $responseFrom"
        tvUserId.text = "UserId =  $userId"
        tvId.text = "Id =  $id"
        tvTitle.text = "Title=  $title"
        tvBody.text = "Body =  $body"
    }








    private fun getVolleyResponse() {
        val queue = Volley.newRequestQueue(this)  //...1

        val stringRequest = StringRequest(
            Request.Method.GET,
            baseUrl,
            { result ->
                Log.d("Response", result)
                tvResponseFrom.text = "Response From Volley"
                try {


                    val mainObject = JSONObject(result)


                    val item = WeatherModel   (city = mainObject.getJSONObject("location").getString("name"),
                        time = mainObject.getJSONObject("current").getString("last_updated"),
                        condition = mainObject.getJSONObject("current")
                            .getJSONObject("condition").getString("text"),
                        currentTemp = mainObject.getJSONObject("current").getString("temp_c"),
                        maxTemp = "weatherItem.maxTemp",
                        minTemp = "weatherItem.minTemp",
                        imageUrl = mainObject.getJSONObject("current")
                            .getJSONObject("condition").getString("icon"),
                        hours = "weatherItem.hours"
                    )

                    setTextViews(
                        item.city,
                        item.condition,
                        item.minTemp,
                        item.currentTemp,
                        item.time
                    )

                } catch (e: JSONException) {
                    Log.d("Parsing Issue:", e.localizedMessage)
                    tvResponseFrom.text = "Parsing Issue"
                }
            },
            {
                tvResponseFrom.text = "Response fail"

            })  //...2

        queue.add(stringRequest) //...3
    }



    private fun getRetrofitResponse() {


        val retrofit = Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()  //...1

        val jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi::class.java)  //...2

        val postApi = jsonPlaceHolderApi.getPosts()  //...3

        postApi.enqueue(object : Callback<WeatherModel> {
            override fun onResponse(call: Call<WeatherModel>, response: retrofit2.Response<WeatherModel>) {
                if (!response.isSuccessful) {
                    Log.d("Response", response.headers().toString())
                    tvResponseFrom.text = "Code " + response.headers().toString()
                    return
                }
                val post = response.body()
                if (post != null) {
                    Log.d("Response", response.body().toString())
                    setTextViews(
                        "Retrofit",
                        post.city,
                        post.condition,
                        post.time,
                        post.condition
                    )
                }

            }

            override fun onFailure(call: Call<WeatherModel>, t: Throwable) {
                tvResponseFrom.text = "Response fail"
            }
        }) //...4

    }



}
