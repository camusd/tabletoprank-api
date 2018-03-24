package com.dylancamus.tabletoprank.util

import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import javax.persistence.EntityNotFoundException

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
class RestExceptionHandler : ResponseEntityExceptionHandler() {

    @ExceptionHandler(RestException::class)
    protected fun handleRestException(ex: RestException): ResponseEntity<Any> {
        return ResponseEntity(ApiError(status = HttpStatus.BAD_REQUEST,
                message = ex.localizedMessage, subErrors = ex.args.map {
            ApiValidationError("email", it.toString())
        }), HttpStatus.BAD_REQUEST)
    }

    override fun handleHttpMessageNotReadable(ex: HttpMessageNotReadableException,
                                              headers: HttpHeaders, status: HttpStatus,
                                              request: WebRequest): ResponseEntity<Any> {
        val error = "Malformed JSON request"
        return ResponseEntity(ApiError(status = HttpStatus.BAD_REQUEST,
                message = error, debugMessage = ex.localizedMessage), HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(EntityNotFoundException::class)
    protected fun handleEntityNotFound(ex: EntityNotFoundException): ResponseEntity<Any> {
        return ResponseEntity(ApiError(status = HttpStatus.NOT_FOUND,
                message = ex.localizedMessage), HttpStatus.NOT_FOUND)
    }

}
