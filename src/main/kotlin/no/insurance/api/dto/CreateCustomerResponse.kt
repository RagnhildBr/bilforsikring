package no.insurance.api.dto

data class CreateCustomerResponse(
    val customerId: String,
    val message: String? = null
)
