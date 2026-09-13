package no.insurance.domain

data class Policy(
    val id: String,
    val customerId: String,
    var registrationNumber: String,
    var bonus: Int,
    var status: AgreementStatus = AgreementStatus.DRAFT
)

enum class AgreementStatus {
    DRAFT,
    ACTIVE,
    AGREEMENT_SENT
}
