package com.example.projet_kotlin.model

data class Country(
    val name: String,
    val fullName: String,
    val code: String,
    val codes: List<String>,
    val currency: String,
    val demonym: String,
    val languages: List<Language>,
    val capital: String,
    val callingCode: String,
    val region: String,
    val subregions: List<String>,
    val translation: Map<String, String>,
    val flag: String
)

data class Language(
    val name: String,
    val code: String
)
