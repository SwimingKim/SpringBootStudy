package com.example.mwc.model.http

import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import javax.validation.constraints.*

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
data class UserRequest(

        @field:NotEmpty
        @field:Size(min = 2, max = 8)
        var name: String? = null,

        @field:PositiveOrZero // 0 < 숫자를 검증 0도 포함 (양수)
        var age: Int? = null,

        @field:Email // email 양식
        var email: String? = null,

        @field:NotBlank // 공백을 검증증
        var address: String? = null,

//        @JsonProperty("phone_number")
        @field:Pattern(regexp = "^\\d{2,3}-\\d{3,4}-\\f{4}\$")
        var phoneNumber: String? = null // phone_number

)