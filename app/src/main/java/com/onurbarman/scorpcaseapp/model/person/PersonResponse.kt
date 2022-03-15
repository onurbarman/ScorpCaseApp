package com.onurbarman.scorpcaseapp.model.person

data class FetchResponse(
    val people: List<Person>,
    val next: String?
    )

data class FetchError(
    val errorDescription: String
    )
