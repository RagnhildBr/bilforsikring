package no.insurance.service

import no.insurance.api.dto.PurchaseRequest
import no.insurance.api.dto.PurchaseResponse
import no.insurance.domain.AgreementStatus
import no.insurance.domain.Policy
import no.insurance.integration.letter.LetterClient
import no.insurance.integration.policy.PolicyClient
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.inOrder
import org.mockito.Mockito.`when`
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

        val policy = Policy(
            id = "pol-1",
            customerId = "cust-123",
            status = AgreementStatus.ACTIVE,
            registrationNumber = "AB12345",
            bonus = 50
        )

        `when`(
            policyClient.createCustomer(
                "Ola",
                "Nordmann",
                "12345678901",
                "ola@example.com"
            )
        ).thenReturn("cust-123")

        `when`(
            policyClient.createPolicy(
                "cust-123",
                "AB12345",
                50,
                AgreementStatus.ACTIVE
            )
        ).thenReturn(policy)

        val response = purchaseService.purchaseInsurance(request)

        assertEquals(
            PurchaseResponse("pol-1", "SUCCESS", "Kjøp fullført"),
            response
        )

        val order = inOrder(policyClient, letterClient)

        order.verify(policyClient).createCustomer(
            "Ola",
            "Nordmann",
            "12345678901",
            "ola@example.com"
        )
        order.verify(policyClient).createPolicy(
            "cust-123",
            "AB12345",
            50,
            AgreementStatus.ACTIVE
        )
        order.verify(letterClient).sendConfirmationLetter(
            policy,
            "cust-123"
        )
    }
}