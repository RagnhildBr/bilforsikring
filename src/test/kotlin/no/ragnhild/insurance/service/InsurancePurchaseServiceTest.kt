package no.ragnhild.insurance.service

import no.ragnhild.insurance.api.dto.PurchaseRequest
import no.ragnhild.insurance.integration.letter.LetterClient
import no.ragnhild.insurance.integration.policy.PolicyClient
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.anyString
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class InsurancePurchaseServiceTest {

    @Mock
    private lateinit var policyClient: PolicyClient

    @Mock
    private lateinit var letterClient: LetterClient

    @InjectMocks
    private lateinit var purchaseService: InsurancePurchaseService

    @Test
    fun testPurchaseFlow() {
        val request = PurchaseRequest(
            customerId = "123",
            registrationNumber = "AB12345",
            bonus = "50%"
        )

        `when`(policyClient.createDraft(anyString())).thenReturn("draft-1")
        `when`(policyClient.activatePolicy(anyString())).thenReturn("policy-1")

        purchaseService.purchaseInsurance(request)

        verify(policyClient).createDraft("123")
        verify(policyClient).updateDraft("draft-1", "AB12345", "50%")
        verify(policyClient).activatePolicy("draft-1")
        verify(letterClient).sendConfirmationLetter("policy-1", "123")
    }
}
