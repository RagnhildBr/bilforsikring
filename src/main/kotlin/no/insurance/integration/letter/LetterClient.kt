package no.insurance.integration.letter

import org.springframework.stereotype.Component

@Component
class LetterClient {

    fun sendConfirmationLetter(policyId: String, customerId: String) {
        System.out.println("[MOCK] Sending confirmation letter for policy " + policyId + " to customer " + customerId)
    }
}
