package igorzig13.creditapplicationsystem.service

import igorzig13.creditapplicationsystem.entity.Credit
import igorzig13.creditapplicationsystem.entity.Customer
import igorzig13.creditapplicationsystem.enums.Status
import igorzig13.creditapplicationsystem.exception.BusinessException
import igorzig13.creditapplicationsystem.repository.CreditRepository
import igorzig13.creditapplicationsystem.service.impl.CreditService
import igorzig13.creditapplicationsystem.service.impl.CustomerService
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.ActiveProfiles
import java.math.BigDecimal
import java.time.LocalDate
import java.util.Random
import java.util.UUID

@ActiveProfiles("test")
@ExtendWith(MockKExtension::class)
class CreditServiceTest {
    @MockK
    lateinit var creditRepository: CreditRepository
    @MockK
    lateinit var customerService: CustomerService
    @InjectMockKs
    lateinit var creditService: CreditService

    @Test
    fun `should create credit`() {
        //given
        val fakeCustomer = Customer(id = 1L, cpf = "121.133.094-08", email = "fake@email.com")
        val fakeCredit: Credit = buildCredit()
        every { customerService.findById(1) } returns fakeCustomer
        every { creditRepository.save(fakeCredit) } returns fakeCredit
        //when
        val actual: Credit = creditService.save(fakeCredit)
        //then
        Assertions.assertThat(actual).isNotNull
        Assertions.assertThat(actual).isSameAs(fakeCredit)
        verify(exactly = 1) { creditRepository.save(fakeCredit) }
        verify(exactly = 1) { customerService.findById(1) }
    }

    @Test
    fun `should return all credits by customer id`(){
        //given
        val fakeId: Long = Random().nextLong()
        val fakeCreditList: List<Credit> = listOf(buildCredit())
        every { creditRepository.findAllByCustomer(fakeId) } returns fakeCreditList
        //when
        val actual = creditService.findAllByCustomer(fakeId)
        //then
        Assertions.assertThat(actual).isNotNull
        Assertions.assertThat(actual).isSameAs(fakeCreditList)
        verify(exactly = 1) { creditRepository.findAllByCustomer(fakeId) }
    }

    @Test
    fun `should find credit by credit code`(){
        //given
        val fakeCreditCode: UUID = UUID.randomUUID()
        val fakeCredit = buildCredit()
        every { creditRepository.findByCreditCode(fakeCreditCode) } returns fakeCredit
        //when
        val actual = creditService.findByCreditCode(fakeCreditCode, fakeCredit.customer?.id!!)
        //then
        Assertions.assertThat(actual).isNotNull
        Assertions.assertThat(actual).isExactlyInstanceOf(Credit::class.java)
        Assertions.assertThat(actual).isSameAs(fakeCredit)
        verify(exactly = 1) { creditRepository.findByCreditCode(fakeCreditCode) }
    }

    @Test
    fun `should not find credit by credit code and throw BusinessException`(){
        //given
        val fakeCreditCode: UUID = UUID.randomUUID()
        every { creditRepository.findByCreditCode(fakeCreditCode) } returns null
        //when
        //then
        Assertions.assertThatExceptionOfType(BusinessException::class.java)
            .isThrownBy { creditService.findByCreditCode(fakeCreditCode, 1) }
            .withMessage("Credit code $fakeCreditCode not found")
        verify(exactly = 1) { creditRepository.findByCreditCode(fakeCreditCode) }
    }

    @Test
    fun `should find credit from different customer and throw IllegalArgumentException`(){
        //given
        val fakeCreditCode: UUID = UUID.randomUUID()
        val fakeCredit = buildCredit()
        every { creditRepository.findByCreditCode(fakeCreditCode) } returns fakeCredit
        //when
        //then
        Assertions.assertThatExceptionOfType(IllegalArgumentException::class.java)
            .isThrownBy { creditService.findByCreditCode(fakeCreditCode, -1) }
            .withMessage("Contact admin")
        verify(exactly = 1) { creditRepository.findByCreditCode(fakeCreditCode) }
    }

    private fun buildCredit(
        creditCode: UUID = UUID.fromString("40610f07-aaf7-493a-a400-8f37cdb4f293"),
        creditValue: BigDecimal = BigDecimal.ZERO,
        dayFirstInstallment: LocalDate = LocalDate.now().plusMonths(1),
        numberOfInstallments: Int = 1,
        status: Status = Status.IN_PROGRESS,
        customer: Customer = Customer(id = 1L, cpf = "121.133.094-08", email = "fake@email.com"),
        id: Long = 1L
    ) = Credit(
        creditCode = creditCode,
        creditValue = creditValue,
        dayFirstInstallment = dayFirstInstallment,
        numberOfInstallments = numberOfInstallments,
        status = status,
        customer = customer,
        id = id
    )
}