package igorzig13.creditapplicationsystem.service

import igorzig13.creditapplicationsystem.entity.Credit
import java.util.UUID

interface ICreditService {

    fun save(credit: Credit): Credit

    fun findAllByCustomer(customerId: Long): List<Credit>

    fun findByCreditCode(creditCode: UUID, customerId: Long): Credit
}