package no.insurance.integration.policy

import no.insurance.api.dto.CreateCustomerRequest
import org.springframework.stereotype.Component

@Component
class PolicyClient {

    fun createCustomer(request: CreateCustomerRequest): String {
        return "cust-123"
    }

    fun createDraft(customerId: String): String {
        return "draft-123"
    }

    fun updateDraft(draftId: String, registrationNumber: String, bonus: String?) {
        // Mock update
    }

    fun activatePolicy(draftId: String): String {
        return "policy-999"
    }
}
