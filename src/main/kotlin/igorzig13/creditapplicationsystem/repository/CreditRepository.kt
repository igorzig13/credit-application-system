package igorzig13.creditapplicationsystem.repository

import igorzig13.creditapplicationsystem.entity.Credit
import org.springframework.data.jpa.repository.JpaRepository

interface CreditRepository: JpaRepository<Credit, Long>