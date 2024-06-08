package com.example.projet_kotlin

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projet_kotlin.api.RetrofitInstance
import com.example.projet_kotlin.model.Country
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


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
                // Vérifie si la chaîne de requête n'est pas nulle
                if (!query.isNullOrEmpty()) {
                    adapter.filter(query)
                }
                return true // Retourne vrai pour indiquer que l'événement a été traité
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Vérifie si la nouvelle chaîne de texte n'est pas nulle
                if (!newText.isNullOrEmpty()) {
                    adapter.filter(newText)
                } else {
                    // Si la nouvelle chaîne de texte est nulle, efface le filtre
                    adapter.filter("")
                }
                return true // Retourne vrai pour indiquer que l'événement a été traité
            }
        })
    }


    private fun fetchCountries() {
        RetrofitInstance.api.getAllCountries().enqueue(object : Callback<List<Country>> {

            override fun onResponse(call: Call<List<Country>>, response: Response<List<Country>>) {
                if (response.isSuccessful && response.body() != null) {

                    Log.d("MainActivity", "API response: $response")
                    countries = response.body()!!

                    Log.d("MainActivity", "Here are the countries: $countries")
                    adapter = CountryAdapter(countries)
                    recyclerView.adapter = adapter

                } else {
                    Toast.makeText(this@MainActivity, "Failed to retrieve data", Toast.LENGTH_SHORT).show()
                    Log.e("MainActivity", "Failed to retrieve data: ${response.errorBody()?.string()}")
                }
            }
            override fun onFailure(call: Call<List<Country>>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Error: " + t.message, Toast.LENGTH_SHORT).show()
                Log.e("MainActivity", "Retrofit onFailure", t)
            }
        })
    }

}