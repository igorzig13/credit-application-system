package igorzig13.creditapplicationsystem.dto

import igorzig13.creditapplicationsystem.entity.Credit
import igorzig13.creditapplicationsystem.entity.Customer
import java.math.BigDecimal
import java.time.LocalDate

data class CreditDto(
    val creditValue: BigDecimal,
    val dayFirstInstallment: LocalDate,
    val numberOfInstallments: Int,
    val customerId: Long
) {
    fun toEntity(): Credit = Credit(
        creditValue = this.creditValue,
        dayFirstInstallment = this.dayFirstInstallment,
        numberOfInstallments = this.numberOfInstallments,
        customer = Customer(id = this.customerId, email = "", cpf = "")
        // VERY UGLY IMPLEMENTATION ABOVE, CHECK CreditService.save() - Gambiarra ?
    )
}
