package igorzig13.creditapplicationsystem.dto

import igorzig13.creditapplicationsystem.entity.Credit
import igorzig13.creditapplicationsystem.entity.Customer
import jakarta.validation.constraints.Future
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.NotNull
import java.math.BigDecimal
import java.time.LocalDate

data class CreditDto(
    @field:NotNull(message = "Invalid input") val creditValue: BigDecimal,
    @field:Future(message = "Invalid date") val dayFirstInstallment: LocalDate,
    @field:Max(value = 48, message = "Invalid input") val numberOfInstallments: Int,
    @field:NotNull(message = "Invalid input") val customerId: Long
) {
    fun toEntity(): Credit = Credit(
        creditValue = this.creditValue,
        dayFirstInstallment = this.dayFirstInstallment,
        numberOfInstallments = this.numberOfInstallments,
        customer = Customer(id = this.customerId, email = "", cpf = "")
        // VERY UGLY IMPLEMENTATION ABOVE, CHECK CreditService.save() - Gambiarra ?
    )
}
