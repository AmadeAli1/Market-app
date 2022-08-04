package com.example.marketapp.extra

import com.example.marketapp.exception.Message
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import javax.validation.Validator

@Component
//@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
class ValidationRequest(
    private val validator: Validator,
) {
    fun <T> validateRequest(request: T): ResponseEntity<out Any>? {
        val validate = validator.validate(request)
        if (validate.isNotEmpty()) {
            val erros = validate.map {
                Message(field = it.propertyPath.toString(), message = it.message)
            }.toList()
            return ResponseEntity(erros, HttpStatus.BAD_REQUEST)
        }
        return null
    }

}