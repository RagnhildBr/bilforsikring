package no.insurance.integration.policy

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Test

class PolicyClientTest {

    private val policyClient = PolicyClient()

    @Test
    fun shouldReturnSameCustomerIdForSamePersonalNumber() {
        val personalNumber = "12345678901"

        val id1 = policyClient.createCustomer("Ola", "Nordmann", personalNumber, "ola@example.com")
        val id2 = policyClient.createCustomer("Ola", "Nordmann", personalNumber, "ola@example.com")

        assertEquals(id1, id2, "Should return the same ID for the same personal number")
        assertEquals(1, policyClient.getCustomerCount(), "Should only have one customer in storage")
    }

    @Test
    fun shouldReturnSameCustomerIdForSamePersonalNumberWithDifferentName() {
        val personalNumber = "12345678901"

        // TODO: This should be handled manually
        val id1 = policyClient.createCustomer("Ola", "Nordmann", personalNumber, "ola@example.com")
        val id2 = policyClient.createCustomer("Kari", "Nordmann", personalNumber, "kari@example.com")
        
        assertEquals(id1, id2, "Should return the same ID for the same personal number even if other fields differ")
        assertEquals(1, policyClient.getCustomerCount(), "Should only have one customer in storage even with different names for same PN")
    }

    @Test
    fun shouldGenerateUniqueIdsForDifferentCustomers() {
        val id1 = policyClient.createCustomer("Ola", "Nordmann", "12345678901", "ola@example.com")
        val id2 = policyClient.createCustomer("Kari", "Nordmann", "12345678902", "kari@example.com")
        
        assertNotEquals(id1, id2, "Should return different IDs for different personal numbers")
        assertEquals(2, policyClient.getCustomerCount(), "Should have two customers in storage")
    }

    @Test
    fun shouldGenerateUniquePolicyIds() {
        val customerId = "cust-123"
        val id1 = policyClient.createPolicy(customerId, "AB12345", 50)
        val id2 = policyClient.createPolicy(customerId, "XY67890", 70)
        
        assertNotEquals(id1, id2, "Should return different IDs for different policies")
    }
}
