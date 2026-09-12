package no.insurance.api.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern

data class PurchaseRequest(
    @field:NotBlank(message = "Fornavn må fylles ut")
    @field:Pattern(regexp = "^[a-zA-ZæøåÆØÅ\\s-]+$", message = "Fornavn kan kun inneholde bokstaver")
    val firstName: String,

    @field:NotBlank(message = "Etternavn må fylles ut")
    @field:Pattern(regexp = "^[a-zA-ZæøåÆØÅ\\s-]+$", message = "Etternavn kan kun inneholde bokstaver")
    val lastName: String,

    @field:NotBlank(message = "Fødselsnummer må fylles ut")
    @field:Pattern(regexp = "^\\d{11}$", message = "Fødselsnummer må være 11 siffer")
    val personalNumber: String,

    @field:NotBlank(message = "E-post må fylles ut")
    @field:Email(message = "Ugyldig e-postformat")
    val email: String,

    val phone: String? = null,

    @field:NotBlank(message = "Registreringsnummer må fylles ut")
    @field:Pattern(regexp = "^[a-zA-Z]{2}\\d{5}$", message = "Ugyldig registreringsnummer (f.eks. AB12345)")
    val registrationNumber: String,

    val bonus: String? = null
)
