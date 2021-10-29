package com.yannick.loic.home.domain.entities

/**
 * Entite du comique
 */
data class ComicsEntity(
    val id: String,
    val title: String,
    val description: String,
    val thumbNail: String,
    val imageUrls: List<String>,
    val flagged: Boolean
)