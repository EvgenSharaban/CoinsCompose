package com.example.coinscomp.presentation.coins.models.coins

data class ModelCoinsCustomView(
    val id: String,
    val rank: Int,
    val name: String,
    val description: String,
    val creationDate: String,
    val logo: String,
    val shortName: String,
    val type: String,
    val isActive: Boolean,
    val price: Double
)
