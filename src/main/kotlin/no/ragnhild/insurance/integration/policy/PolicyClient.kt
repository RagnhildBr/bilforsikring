package no.ragnhild.insurance.integration.policy

import org.springframework.stereotype.Component

@Component
class PolicyClient {

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
