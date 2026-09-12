package no.insurance.api

import no.insurance.api.dto.CreateCustomerResponse
import no.insurance.service.CustomerCreateService
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

@WebMvcTest(CustomerCreateController::class)
class CustomerCreateControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockitoBean
    private lateinit var customerCreateService: CustomerCreateService

    @Test
    fun shouldReturnCreatedForValidCustomerRequest() {
        val validJson = """
            {
                "firstName": "Ola",
                "lastName": "Nordmann",
                "personalNumber": "12345678901",
                "email": "ola@nordmann.no",
                "phone": "99887766"
            }
        """.trimIndent()

        given(customerCreateService.createCustomer(any())).willReturn(
            CreateCustomerResponse("cust-123", "Kunde opprettet")
        )

        mockMvc.perform(post("/api/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(validJson))
                .andExpect(status().isCreated)
                .andExpect(jsonPath("$.customerId").value("cust-123"))
    }

    @Test
    fun shouldReturnBadRequestForInvalidCustomerEmail() {
        val invalidJson = """
            {
                "firstName": "Ola",
                "lastName": "Nordmann",
                "personalNumber": "12345678901",
                "email": "invalid-email"
            }
        """.trimIndent()

        mockMvc.perform(post("/api/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJson))
                .andExpect(status().isBadRequest)
    }
}
