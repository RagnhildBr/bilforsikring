package no.insurance.domain

data class Policy(
    val id: String,
    val customerId: String,
    var registrationNumber: String? = null,
    var bonus: String? = null,
    var status: String = "DRAFT"
)
