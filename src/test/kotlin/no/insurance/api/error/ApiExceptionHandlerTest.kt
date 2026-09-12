package no.insurance.api.error

import no.insurance.api.error.exception.InsuranceBusinessException
import no.insurance.api.error.exception.ResourceNotFoundException
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class TestErrorController {
    @PostMapping("/test-not-found")
    fun throwNotFound() {
        throw ResourceNotFoundException("Test not found")
    }

    @PostMapping("/test-business-error")
    fun throwBusinessError() {
        throw InsuranceBusinessException("Test business error")
    }

    @PostMapping("/test-general-error")
    fun throwGeneralError() {
        throw RuntimeException("Test general error")
    }
}

class ApiExceptionHandlerTest {

    private lateinit var mockMvc: MockMvc

    @BeforeEach
    fun setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(TestErrorController())
            .setControllerAdvice(ApiExceptionHandler())
            .build()
    }

    @Test
    fun shouldHandleResourceNotFound() {
        mockMvc.perform(post("/test-not-found"))
            .andExpect(status().isNotFound)
            .andExpect(jsonPath("$.error").value("Resource Not Found"))
            .andExpect(jsonPath("$.message").value("Test not found"))
    }

    @Test
    fun shouldHandleBusinessException() {
        mockMvc.perform(post("/test-business-error"))
            .andExpect(status().isUnprocessableEntity)
            .andExpect(jsonPath("$.error").value("Business Error"))
            .andExpect(jsonPath("$.message").value("Test business error"))
    }

    @Test
    fun shouldHandleGeneralException() {
        mockMvc.perform(post("/test-general-error"))
            .andExpect(status().isInternalServerError)
            .andExpect(jsonPath("$.error").value("Internal Server Error"))
            .andExpect(jsonPath("$.message").value("An unexpected error occurred"))
    }
}
