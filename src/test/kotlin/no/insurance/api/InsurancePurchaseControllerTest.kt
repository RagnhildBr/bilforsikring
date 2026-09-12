package no.insurance.api

import no.insurance.api.dto.PurchaseResponse
import no.insurance.service.InsurancePurchaseService
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.mockito.ArgumentMatchers.any
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.test.context.bean.override.mockito.MockitoBean

@WebMvcTest(InsurancePurchaseController::class)
class InsurancePurchaseControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockitoBean
    private lateinit var purchaseService: InsurancePurchaseService

    @Test
    fun shouldReturnCreatedForValidRequest() {
        val validJson = """
            {
                "firstName": "Ola",
                "lastName": "Nordmann",
                "personalNumber": "12345678901",
                "email": "ola@nordmann.no",
                "registrationNumber": "AB12345",
                "bonus": "50%"
            }
        """.trimIndent()

        given(purchaseService.purchaseInsurance(any())).willReturn(
            PurchaseResponse("policy-123", "SUCCESS", "OK")
        )

        mockMvc.perform(post("/api/insurance-purchases")
                .contentType(MediaType.APPLICATION_JSON)
                .content(validJson))
                .andExpect(status().isCreated)
    }

    @Test
    fun shouldReturnBadRequestForInvalidEmail() {
        val invalidJson = """
            {
                "firstName": "Ola",
                "lastName": "Nordmann",
                "personalNumber": "12345678901",
                "email": "invalid-email",
                "registrationNumber": "AB12345"
            }
        """.trimIndent()

        mockMvc.perform(post("/api/insurance-purchases")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJson))
                .andExpect(status().isBadRequest)
                .andExpect(jsonPath("$.email").exists())
    }

    @Test
    fun shouldReturnBadRequestForInvalidName() {
        val invalidJson = """
            {
                "firstName": "Ola123",
                "lastName": "Nordmann",
                "personalNumber": "12345678901",
                "email": "ola@nordmann.no",
                "registrationNumber": "AB12345"
            }
        """.trimIndent()

        mockMvc.perform(post("/api/insurance-purchases")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJson))
                .andExpect(status().isBadRequest)
                .andExpect(jsonPath("$.firstName").exists())
    }

    @Test
    fun shouldReturnBadRequestForInvalidPersonalNumber() {
        val invalidJson = """
            {
                "firstName": "Ola",
                "lastName": "Nordmann",
                "personalNumber": "12345",
                "email": "ola@nordmann.no",
                "registrationNumber": "AB12345"
            }
        """.trimIndent()

        mockMvc.perform(post("/api/insurance-purchases")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJson))
                .andExpect(status().isBadRequest)
                .andExpect(jsonPath("$.personalNumber").exists())
    }

    @Test
    fun shouldReturnBadRequestForInvalidRegNumber() {
        val invalidJson = """
            {
                "firstName": "Ola",
                "lastName": "Nordmann",
                "personalNumber": "12345678901",
                "email": "ola@nordmann.no",
                "registrationNumber": "ABC123456"
            }
        """.trimIndent()

        mockMvc.perform(post("/api/insurance-purchases")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJson))
                .andExpect(status().isBadRequest)
                .andExpect(jsonPath("$.registrationNumber").exists())
    }
}
