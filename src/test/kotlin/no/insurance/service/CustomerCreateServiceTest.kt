package no.insurance.service

import no.insurance.api.dto.CreateCustomerRequest
import no.insurance.integration.policy.PolicyClient
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.any
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class CustomerCreateServiceTest {

    @Mock
    private lateinit var policyClient: PolicyClient

    @InjectMocks
    private lateinit var customerCreateService: CustomerCreateService

    @Test
    fun shouldCreateCustomerSuccessfully() {
        // Given
        val request = CreateCustomerRequest(
            firstName = "Ola",
            lastName = "Nordmann",
            personalNumber = "12345678901",
            email = "ola@nordmann.no",
            phone = "99887766"
        )
        val expectedCustomerId = "cust-123"
        `when`(policyClient.createCustomer(any())).thenReturn(expectedCustomerId)

        // When
        val response = customerCreateService.createCustomer(request)

        // Then
        assertEquals(expectedCustomerId, response.customerId)
        assertEquals("Kunde opprettet med ID $expectedCustomerId", response.message)
        verify(policyClient).createCustomer(request)
    }
}
