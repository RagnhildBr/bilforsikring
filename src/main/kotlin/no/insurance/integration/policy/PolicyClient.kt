package no.insurance.integration.policy

import no.insurance.domain.AgreementStatus
import no.insurance.domain.Customer
import no.insurance.domain.Policy
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.util.*
import java.util.concurrent.ConcurrentHashMap

@Component
class PolicyClient {

    private val customers = ConcurrentHashMap<String, Customer>()
    private val policies = ConcurrentHashMap<String, Policy>()

    fun createCustomer(firstName: String, lastName: String, personalNumber: String, email: String): String {
        val hashedPn = hashPersonalNumber(personalNumber)
        val existingCustomer = customers.values.find { it.personalNumber == hashedPn }
        
        if (existingCustomer != null) {
            return existingCustomer.id
        }

        var id: String
        do {
            val uuid = UUID.randomUUID().toString()
            id = "cust-" + uuid.take(8)
        } while (customers.containsKey(id))

        val customer = Customer(
            id = id,
            firstName = firstName,
            lastName = lastName,
            personalNumber = hashedPn,
            email = email
        )
        customers[id] = customer
        printStorageState()
        return id
    }

    fun getCustomerCount(): Int = customers.size

    private fun printStorageState() {
        System.out.println("--- CURRENT STORAGE STATE ---")
        System.out.println("Customers (${customers.size}):")
        customers.values.forEach { System.out.println("  $it") }
        System.out.println("Policies (${policies.size}):")
        policies.values.forEach { System.out.println("  $it") }
        System.out.println("-----------------------------")
    }

    private fun hashPersonalNumber(personalNumber: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val bytes = personalNumber.toByteArray(StandardCharsets.UTF_8)
        val hashBytes = digest.digest(bytes)
        val sb = StringBuilder()
        for (b in hashBytes) {
            sb.append(String.format("%02x", b))
        }
        return sb.toString()
    }

    fun createPolicy(customerId: String, registrationNumber: String, bonus: Int?, status: AgreementStatus = AgreementStatus.ACTIVE): Policy {
        var id: String
        do {
            val uuid = UUID.randomUUID().toString()
            id = "pol-" + uuid.take(8)
        } while (policies.containsKey(id))

        val policy = Policy(
            id = id,
            customerId = customerId,
            status = status,
            registrationNumber = registrationNumber,
            bonus = bonus ?: 0
        )
        policies.put(id, policy)
        printStorageState()
        return policy
    }
}
