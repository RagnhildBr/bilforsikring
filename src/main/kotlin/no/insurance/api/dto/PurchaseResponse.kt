package no.insurance.api.dto

data class PurchaseResponse(
    val policyId: String,
    val status: String,
    val message: String
)
