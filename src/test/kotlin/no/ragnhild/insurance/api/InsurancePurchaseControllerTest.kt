package no.ragnhild.insurance.api

import no.ragnhild.insurance.service.InsurancePurchaseService
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(InsurancePurchaseController::class)
class InsurancePurchaseControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var purchaseService: InsurancePurchaseService

    @Test
    fun shouldReturnCreated() {
        mockMvc.perform(post("/api/insurance-purchases")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"customerId\":\"123\", \"registrationNumber\":\"AB12345\", \"bonus\":\"50%\"}"))
                .andExpect(status().isCreated)
    }
}
