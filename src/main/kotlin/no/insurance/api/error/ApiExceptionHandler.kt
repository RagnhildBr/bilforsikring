package no.insurance.api.error

import no.insurance.api.error.exception.InsuranceBusinessException
import no.insurance.api.error.exception.ResourceNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.util.HashMap

@RestControllerAdvice
class ApiExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationExceptions(ex: MethodArgumentNotValidException): ResponseEntity<Map<String, String?>> {
        val errors = HashMap<String, String?>()
        for (fieldError in ex.bindingResult.fieldErrors) {
            errors.put(fieldError.field, fieldError.defaultMessage)
        }
        return ResponseEntity.badRequest().body(errors)
    }

    @ExceptionHandler(ResourceNotFoundException::class)
    fun handleResourceNotFound(ex: ResourceNotFoundException): ResponseEntity<Map<String, String>> {
        val body = HashMap<String, String>()
        body.put("error", "Resource Not Found")
        body.put("message", ex.message ?: "The requested resource was not found")
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body)
    }

    @ExceptionHandler(InsuranceBusinessException::class)
    fun handleBusinessException(ex: InsuranceBusinessException): ResponseEntity<Map<String, String>> {
        val body = HashMap<String, String>()
        body.put("error", "Business Error")
        body.put("message", ex.message ?: "An error occurred during processing")
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(body)
    }

    @ExceptionHandler(Exception::class)
    fun handleGeneralException(ex: Exception): ResponseEntity<Map<String, String>> {
        val body = HashMap<String, String>()
        body.put("error", "Internal Server Error")
        body.put("message", "An unexpected error occurred")
        // In a real app, we would log the stack trace here
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body)
    }
}
