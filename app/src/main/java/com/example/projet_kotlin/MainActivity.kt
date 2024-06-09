package com.example.projet_kotlin

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projet_kotlin.api.RetrofitInstance
import com.example.projet_kotlin.model.Country
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.projet_kotlin.JsonFiles.readJsonFromFile
import com.example.projet_kotlin.JsonFiles.writeJsonToFile
import com.google.gson.reflect.TypeToken


class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CountryAdapter
    private lateinit var countries: List<Country>
    private lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        fetchCountries()
        searchView = findViewById(R.id.searchView)
        setupSearchView()

    }

    private fun setupSearchView() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {

                if (!query.isNullOrEmpty()) {
                    adapter.filter(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (!newText.isNullOrEmpty()) {
                    adapter.filter(newText)
                } else {
                    adapter.filter("")
                }
                return true
            }
        })
    }


    private fun fetchCountries() {
        val jsonFileName = "countries.json"
        val jsonString = readJsonFromFile(this, jsonFileName)
        if (jsonString != null) {

            val gson = Gson()
            val type = object : TypeToken<List<Country>>() {}.type
            countries = gson.fromJson(jsonString, type)
            adapter = CountryAdapter(countries)
            recyclerView.adapter = adapter
        } else {
            RetrofitInstance.api.getAllCountries().enqueue(object : Callback<List<Country>> {
                override fun onResponse(call: Call<List<Country>>, response: Response<List<Country>>) {
                    if (response.isSuccessful && response.body() != null) {
                        countries = response.body()!!

                        val gson = Gson()
                        val json = gson.toJson(countries)
                        writeJsonToFile(this@MainActivity, json, jsonFileName)

                        adapter = CountryAdapter(countries)
                        recyclerView.adapter = adapter
                    } else {
                        Toast.makeText(this@MainActivity, "Failed to retrieve data", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<List<Country>>, t: Throwable) {
                    Toast.makeText(this@MainActivity, "Error: " + t.message, Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}
