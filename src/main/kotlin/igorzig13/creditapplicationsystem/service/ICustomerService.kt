package igorzig13.creditapplicationsystem.service

import igorzig13.creditapplicationsystem.entity.Customer

interface ICustomerService {
    fun save(customer: Customer): Customer

    fun findById(id: Long): Customer

    fun delete(id: Long)
}