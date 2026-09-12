package no.ragnhild.insurance.service

import no.ragnhild.insurance.api.dto.PurchaseRequest
import no.ragnhild.insurance.api.dto.PurchaseResponse
import no.ragnhild.insurance.integration.letter.LetterClient
import no.ragnhild.insurance.integration.policy.PolicyClient
import org.springframework.stereotype.Service

@Service
class InsurancePurchaseService(
    private val policyClient: PolicyClient,
    private val letterClient: LetterClient
) {

    fun purchaseInsurance(request: PurchaseRequest): PurchaseResponse {
        // Implement flow based on kjopsflyt.puml
        val draftId = policyClient.createDraft(request.customerId)
        policyClient.updateDraft(draftId, request.registrationNumber, request.bonus)
        val policyId = policyClient.activatePolicy(draftId)
        letterClient.sendConfirmationLetter(policyId, request.customerId)

        return PurchaseResponse(policyId, "SUCCESS", "Kjøp fullført")
    }
}
