package no.insurance.api.dto

data class PurchaseResponse(
    val policyId: String? = null,
    val status: String,
    val message: String
)
