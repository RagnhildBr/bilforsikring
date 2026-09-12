package no.insurance.domain

data class Policy(
    val id: String,
    val customerId: String,
    var registrationNumber: String,
    var bonus: Int,
    var status: String = "DRAFT"
)
