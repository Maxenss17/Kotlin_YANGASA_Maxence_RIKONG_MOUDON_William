package com.example.projet_kotlin

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.projet_kotlin.model.Country

class CountryDetailActivity : AppCompatActivity() {

    private lateinit var flagImageView: ImageView
    private lateinit var countryNameTextView: TextView
    private lateinit var fullNameTextView: TextView
    private lateinit var capitalTextView: TextView
    private lateinit var regionTextView: TextView
    private lateinit var subregionTextView: TextView
    private lateinit var currencyTextView: TextView
    private lateinit var demonymTextView: TextView
    private lateinit var languagesTextView: TextView

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_country_detail)
//
//        flagImageView = findViewById(R.id.flagImageView)
//        countryNameTextView = findViewById(R.id.countryNameTextView)
//        fullNameTextView = findViewById(R.id.fullNameTextView)
//        capitalTextView = findViewById(R.id.capitalTextView)
//        regionTextView = findViewById(R.id.regionTextView)
//        subregionTextView = findViewById(R.id.subregionTextView)
//        currencyTextView = findViewById(R.id.currencyTextView)
//        demonymTextView = findViewById(R.id.demonymTextView)
//        languagesTextView = findViewById(R.id.languagesTextView)
//
//        val country = intent.getSerializableExtra("country") as? Country
//        country?.let { displayCountryDetails(it) }
//    }

    private fun displayCountryDetails(country: Country) {
        countryNameTextView.text = country.name
        fullNameTextView.text = getString(R.string.full_name, country.fullName)
        capitalTextView.text = getString(R.string.capital, country.capital)
        regionTextView.text = getString(R.string.region, country.region)
        subregionTextView.text = getString(R.string.subregions, country.subregions.joinToString(", "))
        currencyTextView.text = getString(R.string.currency, country.currency)
        demonymTextView.text = getString(R.string.demonym, country.demonym)

        val languages = country.languages.joinToString { it.name }
        languagesTextView.text = getString(R.string.languages, languages)

        Glide.with(this).load(country.flag).into(flagImageView)
    }
}