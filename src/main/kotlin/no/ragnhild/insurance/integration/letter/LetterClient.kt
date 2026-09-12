package no.ragnhild.insurance.integration.letter

import org.springframework.stereotype.Component

@Component
class LetterClient {

    fun sendConfirmationLetter(policyId: String, customerId: String) {
        // Mock send
    }
}
