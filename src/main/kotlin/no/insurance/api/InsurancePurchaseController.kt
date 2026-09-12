package no.insurance.api

import jakarta.validation.Valid
import no.insurance.api.dto.PurchaseRequest
import no.insurance.api.dto.PurchaseResponse
import no.insurance.service.InsurancePurchaseService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/insurance-purchases")
class InsurancePurchaseController(private val purchaseService: InsurancePurchaseService) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun purchaseInsurance(@Valid @RequestBody request: PurchaseRequest): PurchaseResponse {
        return purchaseService.purchaseInsurance(request)
    }
}
