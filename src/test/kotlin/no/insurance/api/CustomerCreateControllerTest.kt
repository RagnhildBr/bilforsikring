package no.insurance.api

import no.insurance.api.dto.CreateCustomerResponse
import no.insurance.service.CustomerCreateService
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(CustomerCreateController::class)
class CustomerCreateControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockitoBean
    private lateinit var customerCreateService: CustomerCreateService

    @Test
    fun shouldReturnCreatedForValidCustomerRequest() {
        val validJson = "{\"firstName\":\"Ola\",\"lastName\":\"Nordmann\",\"personalNumber\":\"12345678901\",\"email\":\"ola@nordmann.no\"}"

        val expectedResponse = CreateCustomerResponse("cust-123", "Kunde opprettet")
        given(customerCreateService.createCustomer(no.insurance.api.dto.CreateCustomerRequest("Ola", "Nordmann", "12345678901", "ola@nordmann.no"))).willReturn(expectedResponse)

        mockMvc.perform(post("/api/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(validJson))
                .andExpect(status().isCreated)
                .andExpect(jsonPath("$.customerId").value("cust-123"))
    }

    @Test
    fun shouldReturnBadRequestForInvalidCustomerEmail() {
        val invalidJson = "{\"firstName\":\"Ola\",\"lastName\":\"Nordmann\",\"personalNumber\":\"12345678901\",\"email\":\"invalid-email\"}"

        mockMvc.perform(post("/api/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJson))
                .andExpect(status().isBadRequest)
    }
}
