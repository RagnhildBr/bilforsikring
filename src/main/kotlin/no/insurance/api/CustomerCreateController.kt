package no.insurance.api

import jakarta.validation.Valid
import no.insurance.api.dto.CreateCustomerRequest
import no.insurance.api.dto.CreateCustomerResponse
import no.insurance.service.CustomerCreateService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/customers")
class CustomerCreateController(private val customerCreateService: CustomerCreateService) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createCustomer(@Valid @RequestBody request: CreateCustomerRequest): CreateCustomerResponse {
        return customerCreateService.createCustomer(request)
    }
}
