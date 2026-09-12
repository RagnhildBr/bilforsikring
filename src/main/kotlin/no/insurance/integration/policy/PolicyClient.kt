package no.insurance.integration.policy

import no.insurance.api.dto.CreateCustomerRequest
import no.insurance.api.error.exception.InsuranceBusinessException
import no.insurance.api.error.exception.ResourceNotFoundException
import no.insurance.domain.Customer
import no.insurance.domain.Policy
import org.springframework.stereotype.Component
import java.util.concurrent.ConcurrentHashMap

@Component
class PolicyClient {

    private val customers = ConcurrentHashMap<String, Customer>()
    private val policies = ConcurrentHashMap<String, Policy>()

    fun createCustomer(request: CreateCustomerRequest): String {
        val uuid = java.util.UUID.randomUUID().toString()
        val id = "cust-" + (uuid as java.lang.String).substring(0, 8)
        val customer = Customer(
            id = id,
            firstName = request.firstName,
            lastName = request.lastName,
            personalNumber = request.personalNumber,
            email = request.email
        )
        customers.put(id, customer)
        return id
    }

    fun createDraft(customerId: String): String {
        val uuid = java.util.UUID.randomUUID().toString()
        val id = "draft-" + (uuid as java.lang.String).substring(0, 8)
        val policy = Policy(id = id, customerId = customerId, status = "DRAFT")
        policies.put(id, policy)
        return id
    }

    fun updateDraft(draftId: String, registrationNumber: String, bonus: String?) {
        val policy = policies.get(draftId) ?: throw ResourceNotFoundException("Draft not found: " + draftId)
        policy.registrationNumber = registrationNumber
        policy.bonus = bonus
    }

    fun activatePolicy(draftId: String): String {
        val policy = policies.get(draftId) ?: throw ResourceNotFoundException("Draft not found: " + draftId)
        val uuid = java.util.UUID.randomUUID().toString()
        val policyId = (uuid as java.lang.String).substring(0, 8)
        
        val activePolicy = Policy(
            id = policyId,
            customerId = policy.customerId,
            registrationNumber = policy.registrationNumber ?: throw InsuranceBusinessException("Registration number missing in draft"),
            bonus = policy.bonus,
            status = "ACTIVE"
        )
        
        policies.put(policyId, activePolicy)
        policy.status = "ACTIVATED"
        
        return policyId
    }
}
