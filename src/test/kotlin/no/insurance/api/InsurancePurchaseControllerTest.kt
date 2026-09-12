package no.insurance.api

import no.insurance.api.dto.PurchaseRequest
import no.insurance.api.dto.PurchaseResponse
import no.insurance.service.InsurancePurchaseService
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(InsurancePurchaseController::class)
class InsurancePurchaseControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockitoBean
    private lateinit var purchaseService: InsurancePurchaseService

    @Test
    fun shouldReturnCreatedForValidRequest() {
        val validJson = "{\"firstName\":\"Ola\",\"lastName\":\"Nordmann\",\"personalNumber\":\"12345678901\",\"email\":\"ola@nordmann.no\",\"registrationNumber\":\"AB12345\",\"bonus\":50}"

        val expectedResponse = PurchaseResponse("123", "SUCCESS", "OK")
        given(purchaseService.purchaseInsurance(PurchaseRequest("Ola", "Nordmann", "12345678901", "ola@nordmann.no", "AB12345", 50))).willReturn(expectedResponse)

        mockMvc.perform(post("/api/insurance-purchases")
                .contentType(MediaType.APPLICATION_JSON)
                .content(validJson))
                .andExpect(status().`is`(HttpStatus.CREATED.value()))
    }

    @Test
    fun shouldReturnBadRequestForInvalidEmail() {
        val invalidJson = "{\"firstName\":\"Ola\",\"lastName\":\"Nordmann\",\"personalNumber\":\"12345678901\",\"email\":\"invalid-email\",\"registrationNumber\":\"AB12345\"}"

        mockMvc.perform(post("/api/insurance-purchases")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJson))
                .andExpect(status().`is`(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.email").exists())
    }

    @Test
    fun shouldReturnBadRequestForInvalidName() {
        val invalidJson = "{\"firstName\":\"Ola123\",\"lastName\":\"Nordmann\",\"personalNumber\":\"12345678901\",\"email\":\"ola@nordmann.no\",\"registrationNumber\":\"AB12345\"}"

        mockMvc.perform(post("/api/insurance-purchases")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJson))
                .andExpect(status().`is`(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.firstName").exists())
    }

    @Test
    fun shouldReturnBadRequestForInvalidPersonalNumber() {
        val invalidJson = "{\"firstName\":\"Ola\",\"lastName\":\"Nordmann\",\"personalNumber\":\"12345\",\"email\":\"ola@nordmann.no\",\"registrationNumber\":\"AB12345\"}"

        mockMvc.perform(post("/api/insurance-purchases")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJson))
                .andExpect(status().`is`(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.personalNumber").exists())
    }

    @Test
    fun shouldReturnBadRequestForInvalidRegNumber() {
        val invalidJson = "{\"firstName\":\"Ola\",\"lastName\":\"Nordmann\",\"personalNumber\":\"12345678901\",\"email\":\"ola@nordmann.no\",\"registrationNumber\":\"ABC123456\"}"

        mockMvc.perform(post("/api/insurance-purchases")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJson))
                .andExpect(status().`is`(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.registrationNumber").exists())
    }
}
