package com.example.projet_kotlin

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.projet_kotlin.model.Country

class CountryViewHolder(view : View) : RecyclerView.ViewHolder(view)

class CountryAdapter(private var countries: List<Country>) : RecyclerView.Adapter<CountryViewHolder>() {

    private var countriesFiltered: List<Country> = countries

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.country_item, parent, false)
        return CountryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {

        val country = countriesFiltered[position]
        val view = holder.itemView
        val countryNameTextView: TextView = view.findViewById(R.id.countryNameTextView)
        countryNameTextView.text = country.name

//        val flagImageView: ImageView = view.findViewById(R.id.flagImageView)
//        Glide.with(holder.itemView.context).load(country.flag).into(holder.flagImageView)

        val CardView = view.findViewById<CardView>(R.id.country_view_cardview)
        CardView.setOnClickListener() {
            val intent = Intent(holder.itemView.context, CountryDetailActivity::class.java)
            intent.putExtra("name", country.name)
            holder.itemView.context.startActivity(intent)

        }
    }

    override fun getItemCount(): Int = countriesFiltered.size

    fun filter(query: String) {
        countriesFiltered = if (query.isEmpty()) {
            countries
        } else {
            countries.filter {
                it.name.lowercase().contains(query.lowercase()) ||
                        it.capital.lowercase().contains(query.lowercase())
            }
        }
        notifyDataSetChanged()
    }
}