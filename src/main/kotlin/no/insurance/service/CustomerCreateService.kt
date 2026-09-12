package no.insurance.service

import no.insurance.api.dto.CreateCustomerRequest
import no.insurance.api.dto.CreateCustomerResponse
import no.insurance.integration.policy.PolicyClient
import org.springframework.stereotype.Service

@Service
class CustomerCreateService(private val policyClient: PolicyClient) {

    fun createCustomer(request: CreateCustomerRequest): CreateCustomerResponse {
        val customerId = policyClient.createCustomer(request)
        return CreateCustomerResponse(
            customerId = customerId,
            message = "Kunde opprettet med ID $customerId"
        )
    }
}
