package igorzig13.creditapplicationsystem.repository

import igorzig13.creditapplicationsystem.entity.Credit
import igorzig13.creditapplicationsystem.entity.Customer
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.test.context.ActiveProfiles
import java.time.LocalDate
import java.util.UUID

@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE )
class CreditRepositoryTest {
    @Autowired lateinit var creditRepository: CreditRepository
    @Autowired lateinit var testEntityManager: TestEntityManager

    private lateinit var customer: Customer
    private lateinit var credit1: Credit
    private lateinit var credit2: Credit

    @BeforeEach fun setup() {
        customer = testEntityManager.persist(buildCustomer())
        val creditCode = UUID.fromString("c4777ca6-b001-4fce-8ba2-e932b02564b4")
        credit1 = testEntityManager.persist(buildCredit(customer, creditCode))
        credit2 = testEntityManager.persist(buildCredit(customer))
    }

    @Test
    fun `should find credit by Credit Code`(){
        //given
        val creditCode = UUID.fromString("c4777ca6-b001-4fce-8ba2-e932b02564b4")
        //when
        val actual: Credit = creditRepository.findByCreditCode(creditCode)!!
        //then
        Assertions.assertThat(actual).isNotNull
        Assertions.assertThat(actual).isSameAs(credit1)

    }

    @Test
    fun `should find all credits by customer id`(){
        //given
        val customerId: Long = 1L
        //when
        val creditList: List<Credit> = creditRepository.findAllByCustomer(customerId)
        //then
        Assertions.assertThat(creditList).isNotEmpty
        Assertions.assertThat(creditList).size().isEqualTo(2)
        Assertions.assertThat(creditList).contains(credit1, credit2)
    }

    // Important: Minified building functions, passing only required properties not already filled
    // Remember to refactor with full ones if needed (probably not).

    private fun buildCustomer(
        cpf: String = "121.133.094-08",
        email: String = "test@email.com"
    ): Customer = Customer(
        cpf = cpf,
        email = email
    )

    private fun buildCredit(customer: Customer, creditCode: UUID = UUID.randomUUID()
    ): Credit = Credit(
        dayFirstInstallment = LocalDate.now().plusMonths(1),
        customer = customer,
        creditCode = creditCode
    )

}