package no.insurance.api.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern

data class CreateCustomerRequest(
    @field:NotBlank
    @field:Pattern(regexp = "^[a-zA-ZæøåÆØÅ\\s-]+$")
    val firstName: String,

    @field:NotBlank
    @field:Pattern(regexp = "^[a-zA-ZæøåÆØÅ\\s-]+$")
    val lastName: String,

    @field:NotBlank
    @field:Pattern(regexp = "^\\d{11}$")
    val personalNumber: String,

    @field:NotBlank
    @field:Email
    val email: String,
)
