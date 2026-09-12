package no.insurance.service

import no.insurance.api.dto.CreateCustomerRequest
import no.insurance.api.dto.CreateCustomerResponse
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

    @Mock
    private lateinit var customerCreateService: CustomerCreateService

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
            bonus = "50%"
        )

        val customerRequest = CreateCustomerRequest(
            firstName = "Ola",
            lastName = "Nordmann",
            personalNumber = "12345678901",
            email = "ola@example.com"
        )
        `when`(customerCreateService.createCustomer(customerRequest)).thenReturn(CreateCustomerResponse("cust-123"))
        `when`(policyClient.createDraft("cust-123")).thenReturn("draft-1")
        `when`(policyClient.activatePolicy("draft-1")).thenReturn("policy-1")

        purchaseService.purchaseInsurance(request)

        verify(customerCreateService).createCustomer(customerRequest)
        verify(policyClient).createDraft("cust-123")
        verify(policyClient).updateDraft("draft-1", "AB12345", "50%")
        verify(policyClient).activatePolicy("draft-1")
        verify(letterClient).sendConfirmationLetter("policy-1", "cust-123")
    }
}
