package no.insurance.service

import no.insurance.api.dto.PurchaseRequest
import no.insurance.integration.letter.LetterClient
import no.insurance.integration.policy.PolicyClient
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
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
            firstName = "Ola",
            lastName = "Nordmann",
            personalNumber = "12345678901",
            email = "ola@example.com",
            registrationNumber = "AB12345",
            bonus = 50
        )

        `when`(policyClient.createCustomer("Ola", "Nordmann", "12345678901", "ola@example.com")).thenReturn("cust-123")
        `when`(policyClient.createPolicy("cust-123", "AB12345", 50)).thenReturn("pol-1")

        purchaseService.purchaseInsurance(request)

        verify(policyClient).createCustomer("Ola", "Nordmann", "12345678901", "ola@example.com")
        verify(policyClient).createPolicy("cust-123", "AB12345", 50)
        verify(letterClient).sendConfirmationLetter("pol-1", "cust-123")
    }
}
