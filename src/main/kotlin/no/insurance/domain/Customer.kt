package no.insurance.domain

data class Customer(
    val id: String,
    val firstName: String,
    val lastName: String,
    val personalNumber: String,
    val email: String
)
