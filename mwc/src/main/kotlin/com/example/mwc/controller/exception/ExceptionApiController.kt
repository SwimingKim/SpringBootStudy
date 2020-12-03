package com.example.mwc.controller.exception

import com.example.mwc.model.http.Error
import com.example.mwc.model.http.ErrorReponse
import com.example.mwc.model.http.UserRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime
import javax.servlet.http.HttpServletRequest
import javax.validation.ConstraintViolationException
import javax.validation.Valid
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

@RestController
@RequestMapping("/api/exception")
@Validated
class ExceptionApiController {


    @GetMapping("/hello")
    fun hello() {
//        if (true) {
//            throw RuntimeException("강제 exception 발생")
//        }

        val list = mutableListOf<String>()
        val temp = list[0]
    }

    @GetMapping("")
    fun get(
            @NotBlank
            @Size(min = 2, max = 6)
            @RequestParam name: String,

            @Min(10)
            @RequestParam age: Int
    ): String {
        println(name)
        println(age)
        return name + " " + age
    }

    @PostMapping("")
    fun post(@Valid @RequestBody userRequest: UserRequest): UserRequest {
        println(userRequest)
        return userRequest
    }

    @ExceptionHandler(value = [MethodArgumentNotValidException::class])
    fun methodArgumentNotValidException(e: MethodArgumentNotValidException, request: HttpServletRequest): ResponseEntity<ErrorReponse> {
        val errors = mutableListOf<Error>()

        e.bindingResult.allErrors.forEach { errorObject ->
            val error = Error().apply {
                this.field = (errorObject as FieldError).field
                this.message = errorObject.defaultMessage
                this.value = errorObject.rejectedValue
            }
            errors.add(error)
        }

        // 2. Error Response
        val errorReponse = ErrorReponse().apply {
            this.resultCode = "FAIL"
            this.httpStatus = HttpStatus.BAD_REQUEST.value().toString()
            this.httpMethod = request.method
            this.message = "요청에 에러가 발생하였습니다."
            this.path = request.requestURI.toString()
            this.timestamp = LocalDateTime.now()
            this.errors = errors
        }

        // 3. ResponseEntity
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorReponse)
    }

    @ExceptionHandler(value = [ConstraintViolationException::class])
    fun constraintViolationException(e: ConstraintViolationException, request: HttpServletRequest): ResponseEntity<ErrorReponse> {
        // 1. 에러 분석
        val errors = mutableListOf<Error>()

        e.constraintViolations.forEach {
            val error = Error().apply {
                this.field =  it.propertyPath.last().name
                this.message = it.message
                this.value = it.invalidValue
            }
            errors.add(error)
        }

        // 2. Error Response
        val errorReponse = ErrorReponse().apply {
            this.resultCode = "FAIL"
            this.httpStatus = HttpStatus.BAD_REQUEST.value().toString()
            this.httpMethod = request.method
            this.message = "요청에 에러가 발생하였습니다."
            this.path = request.requestURI.toString()
            this.timestamp = LocalDateTime.now()
            this.errors = errors
        }

        // 3. ResponseEntity
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorReponse)
    }

    @ExceptionHandler(value = [IndexOutOfBoundsException::class])
    fun indexOutOfBoundsException(): ResponseEntity<String> { // 200 ok
        println("controller exception handler")
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Index Error")
    }

}