package no.insurance.service

import no.insurance.api.dto.CreateCustomerRequest
import no.insurance.api.dto.PurchaseRequest
import no.insurance.api.dto.PurchaseResponse
import no.insurance.integration.letter.LetterClient
import no.insurance.integration.policy.PolicyClient
import org.springframework.stereotype.Service

@Service
class InsurancePurchaseService(
    private val policyClient: PolicyClient,
    private val letterClient: LetterClient,
    private val customerCreateService: CustomerCreateService
) {

    fun purchaseInsurance(request: PurchaseRequest): PurchaseResponse {
        // 1. Create customer
        val customerRequest = CreateCustomerRequest(
            firstName = request.firstName,
            lastName = request.lastName,
            personalNumber = request.personalNumber,
            email = request.email,
            phone = request.phone
        )
        val customerResponse = customerCreateService.createCustomer(customerRequest)
        val customerId = customerResponse.customerId

        // 2. Create and update draft
        val draftId = policyClient.createDraft(customerId)
        policyClient.updateDraft(draftId, request.registrationNumber, request.bonus)

        // 3. Activate policy
        val policyId = policyClient.activatePolicy(draftId)

        // 4. Send confirmation letter
        letterClient.sendConfirmationLetter(policyId, customerId)

        return PurchaseResponse(policyId, "SUCCESS", "Kjøp fullført for kunde $customerId")
    }
}
