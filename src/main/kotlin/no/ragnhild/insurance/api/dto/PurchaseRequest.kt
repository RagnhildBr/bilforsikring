package no.ragnhild.insurance.api.dto

import jakarta.validation.constraints.NotBlank

data class PurchaseRequest(
    @field:NotBlank
    val customerId: String,
    @field:NotBlank
    val registrationNumber: String,
    val bonus: String? = null
)
