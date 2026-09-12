package no.insurance.service

import no.insurance.api.dto.PurchaseRequest
import no.insurance.api.dto.PurchaseResponse
import no.insurance.integration.letter.LetterClient
import no.insurance.integration.policy.PolicyClient
import org.springframework.stereotype.Service

@Service
class InsurancePurchaseService(
    private val policyClient: PolicyClient,
    private val letterClient: LetterClient
) {

    fun purchaseInsurance(request: PurchaseRequest): PurchaseResponse {
        // 1. Create customer
        val customerId = policyClient.createCustomer(
            firstName = request.firstName,
            lastName = request.lastName,
            personalNumber = request.personalNumber,
            email = request.email
        )

        // 2. Create policy
        val policyId = policyClient.createPolicy(customerId, request.registrationNumber, request.bonus)

        // 3. Send confirmation letter
        letterClient.sendConfirmationLetter(policyId, customerId)

        return PurchaseResponse(policyId, "SUCCESS", "Kjøp fullført")
    }
}
