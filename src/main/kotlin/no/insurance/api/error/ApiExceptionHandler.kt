package no.insurance.api.error

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
}
