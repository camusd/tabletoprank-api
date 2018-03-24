package com.dylancamus.tabletoprank.util

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonInclude
import org.springframework.http.HttpStatus
import java.time.LocalDateTime

@JsonInclude(JsonInclude.Include.NON_NULL)
class ApiError(
        val status: HttpStatus = HttpStatus.INTERNAL_SERVER_ERROR,
        val message: String = "Unexpected Error",
        val debugMessage: String? = null,
        val subErrors: List<ApiSubError>? = null) {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    val timestamp = LocalDateTime.now()
}
