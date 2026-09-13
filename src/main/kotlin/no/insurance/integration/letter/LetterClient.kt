package no.insurance.integration.letter

import no.insurance.domain.AgreementStatus
import no.insurance.domain.Policy
import org.springframework.stereotype.Component

@Component
class LetterClient {

    fun sendConfirmationLetter(policy: Policy, customerId: String) {
        policy.status = AgreementStatus.AGREEMENT_SENT
        System.out.println("[MOCK] Sending confirmation letter for policy " + policy.id + " to customer " + customerId)
    }
}
