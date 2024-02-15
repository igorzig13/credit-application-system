package igorzig13.creditapplicationsystem.exception

import org.springframework.dao.DataAccessException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.validation.ObjectError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.time.LocalDateTime

@RestControllerAdvice
class RestExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValidException(ex: MethodArgumentNotValidException
    ): ResponseEntity<ExceptionDetails>{
        val errors: MutableMap<String, String?> = HashMap()
        ex.bindingResult.allErrors.stream().forEach {
            error: ObjectError ->
            val fieldName: String = (error as FieldError).field
            val errorMessage: String? = error.defaultMessage
            errors[fieldName] = errorMessage
        }

        return ResponseEntity(ExceptionDetails(
            title = "Bad Request! Consult the documentation!",
            timestamp = LocalDateTime.now(),
            status = HttpStatus.BAD_REQUEST.value(),
            exception = ex.javaClass.toString(),
            details = errors
        ), HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(DataAccessException::class)
    fun handleDataAccessException(ex: DataAccessException
    ): ResponseEntity<ExceptionDetails>{

        return ResponseEntity(ExceptionDetails(
            title = "Bad Request! Consult the documentation!",
            timestamp = LocalDateTime.now(),
            status = HttpStatus.CONFLICT.value(),
            exception = ex.javaClass.toString(),
            details = mutableMapOf(ex.cause.toString() to ex.message)
        ), HttpStatus.CONFLICT)
    }

    @ExceptionHandler(BusinessException::class)
    fun handleBusinessException(ex: BusinessException
    ): ResponseEntity<ExceptionDetails>{

        return ResponseEntity(ExceptionDetails(
            title = "Bad Request! Consult the documentation!",
            timestamp = LocalDateTime.now(),
            status = HttpStatus.BAD_REQUEST.value(),
            exception = ex.javaClass.toString(),
            details = mutableMapOf(ex.cause.toString() to ex.message)
        ), HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(ex: IllegalArgumentException
    ): ResponseEntity<ExceptionDetails>{

        return ResponseEntity(ExceptionDetails(
            title = "Bad Request! Consult the documentation!",
            timestamp = LocalDateTime.now(),
            status = HttpStatus.BAD_REQUEST.value(),
            exception = ex.javaClass.toString(),
            details = mutableMapOf(ex.cause.toString() to ex.message)
        ), HttpStatus.BAD_REQUEST)
    }

}