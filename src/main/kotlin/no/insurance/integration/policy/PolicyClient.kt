package no.insurance.integration.policy

import no.insurance.domain.Customer
import no.insurance.domain.Policy
import org.springframework.stereotype.Component
import java.nio.charset.Charset
import java.security.MessageDigest
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

@Component
class PolicyClient {

    private val customers = ConcurrentHashMap<String, Customer>()
    private val policies = ConcurrentHashMap<String, Policy>()

    fun createCustomer(firstName: String, lastName: String, personalNumber: String, email: String): String {
        var id: String
        do {
            val uuid = UUID.randomUUID().toString()
            id = "cust-" + (uuid as java.lang.String).substring(0, 8)
        } while (customers.containsKey(id))

        val customer = Customer(
            id = id,
            firstName = firstName,
            lastName = lastName,
            personalNumber = hashPersonalNumber(personalNumber),
            email = email
        )
        customers.put(id, customer)
        printStorageState()
        return id
    }

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
        val bytes = (personalNumber as java.lang.String).getBytes(Charset.forName("UTF-8"))
        val hashBytes = digest.digest(bytes)
        val sb = StringBuilder()
        for (b in hashBytes) {
            sb.append(java.lang.String.format("%02x", b))
        }
        return sb.toString()
    }

    fun createPolicy(customerId: String, registrationNumber: String, bonus: Int?, status: String = "ACTIVE"): String {
        var id: String
        do {
            val uuid = UUID.randomUUID().toString()
            id = "pol-" + (uuid as java.lang.String).substring(0, 8)
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
        return id
    }
}
