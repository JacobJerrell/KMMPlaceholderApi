package com.bocaj.kmmplaceholderapi.sharedPlaceholderApi.model

import kotlinx.serialization.Serializable

@Serializable
data class Post(
        val userId: Int,
        val id: Int,
        val title: String,
        val body: String
)