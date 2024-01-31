package com.zfml.bookworm.domain.model


data class Book(
    val id: String = "",
    val name: String = "Fuck You",
    val author: String = "",
    val hasRead: Boolean = false,
    val dateBought: Long = 0,
    val bookCover: String? = null
)
