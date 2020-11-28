package com.example.mwc.model.http

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDateTime

data class ErrorReponse(
        @field:JsonProperty("result_code")
        var resultCode: String? = null,

        @field:JsonProperty("http_status")
        var httpStatus: String? = null,

        var message: String? = null,
        var path: String? = null,
        var timestamp: LocalDateTime? = null,
        var errors: MutableList<Error>? = mutableListOf()
)

data class Error(
        var field: String? = null,
        var message: String? = null
)