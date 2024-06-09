package com.example.projet_kotlin

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.projet_kotlin.JsonFiles
import com.example.projet_kotlin.JsonFiles.readJsonFromFile
import com.example.projet_kotlin.JsonFiles.writeJsonToFile
import com.example.projet_kotlin.api.RetrofitInstance
import com.example.projet_kotlin.model.Country
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CountryDetailsActivity : AppCompatActivity() {

    private lateinit var flagImageView: ImageView
    private lateinit var countryNameTextView: TextView
    private lateinit var capitalNameTextView: TextView
    private lateinit var populationTextView: TextView
    private lateinit var regionTextView: TextView
    private lateinit var subregionTextView: TextView
    private lateinit var areaTextView: TextView
    private lateinit var languageTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.country_details)

        flagImageView = findViewById(R.id.flagImageView)
        countryNameTextView = findViewById(R.id.countryNameTextView)
        capitalNameTextView = findViewById(R.id.capitalNameTextView)
        populationTextView = findViewById(R.id.populationTextView)
        regionTextView = findViewById(R.id.regionTextView)
        subregionTextView = findViewById(R.id.subregionTextView)
        areaTextView = findViewById(R.id.areaTextView)
        languageTextView = findViewById(R.id.languageTextView)

        val countryName = intent.getStringExtra("country_name")
        if (countryName != null) {
            fetchCountryDetails(countryName)
        } else {
            Toast.makeText(this, "Country name is missing", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchCountryDetails(name: String) {
        val jsonFileName = "country_details_$name.json"
        val jsonString = readJsonFromFile(this, jsonFileName)
        if (jsonString != null) {

            val gson = Gson()
            val type = object : TypeToken<List<Country>>() {}.type
            val country = gson.fromJson<List<Country>>(jsonString, type).firstOrNull()
            if (country != null) {
                displayCountryDetails(country)
            } else {
                Toast.makeText(this, "Country not found", Toast.LENGTH_SHORT).show()
            }
        } else {
            RetrofitInstance.api.getCountryByName(name).enqueue(object : Callback<List<Country>> {
                override fun onResponse(
                    call: Call<List<Country>>,
                    response: Response<List<Country>>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        val country = response.body()!!.firstOrNull()
                        if (country != null) {

                            val gson = Gson()
                            val json = gson.toJson(response.body())
                            writeJsonToFile(this@CountryDetailsActivity, json, jsonFileName)

                            displayCountryDetails(country)
                        } else {
                            Toast.makeText(
                                this@CountryDetailsActivity,
                                "Country not found",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        Toast.makeText(
                            this@CountryDetailsActivity,
                            "Failed to retrieve data",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<List<Country>>, t: Throwable) {
                    Toast.makeText(
                        this@CountryDetailsActivity,
                        "Error: " + t.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }

    }

    private fun displayCountryDetails(country: Country) {
        Glide.with(this).load(country.flags.png).into(flagImageView)
        countryNameTextView.text = country.name.common
        capitalNameTextView.text = country.capital?.firstOrNull() ?: "Aucune Capitale"
        populationTextView.text = "Population: ${country.population}"
        regionTextView.text = "Region: ${country.region}"
        subregionTextView.text = "Subregion: ${country.subregion}"
        areaTextView.text = "Area: ${country.area} km²"
        languageTextView.text = "Languages: ${country.languages.values.joinToString(", ")}"
    }
}
