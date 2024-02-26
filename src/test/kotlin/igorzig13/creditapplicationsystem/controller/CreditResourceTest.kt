package igorzig13.creditapplicationsystem.controller

import com.fasterxml.jackson.databind.ObjectMapper
import igorzig13.creditapplicationsystem.dto.CreditDto
import igorzig13.creditapplicationsystem.entity.Customer
import igorzig13.creditapplicationsystem.repository.CreditRepository
import igorzig13.creditapplicationsystem.repository.CustomerRepository
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.math.BigDecimal
import java.time.LocalDate

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration
class CreditResourceTest {
    @Autowired
    private lateinit var creditRepository: CreditRepository

    @Autowired
    private lateinit var customerRepository: CustomerRepository

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    companion object {
        const val URL: String = "/api/credits"
    }

    @BeforeEach
    fun setup() = creditRepository.deleteAll()

    @AfterEach
    fun tearDown() = creditRepository.deleteAll()

    @Test
    fun `should save credit and return 201 status`() {
        //given
        customerRepository.save(
            Customer(id = 1, firstName = "Test", email = "test@email.com", cpf = "121.133.094-08")
        )
        val creditDto: CreditDto = buildCreditDto()
        val valueAsString = objectMapper.writeValueAsString(creditDto)
        //when
        //then
        mockMvc.perform(
            MockMvcRequestBuilders.post(URL).contentType(MediaType.APPLICATION_JSON)
                .content(valueAsString)
        )
            .andExpect(MockMvcResultMatchers.status().isCreated)
            .andExpect(MockMvcResultMatchers.jsonPath("$.creditCode").isNotEmpty)
            .andExpect(MockMvcResultMatchers.jsonPath("$.creditValue").value("1000.0"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.numberOfInstallments").value("3"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.emailCustomer").value("test@email.com"))
            .andDo(MockMvcResultHandlers.print())
    }

    private fun buildCreditDto(
        creditValue: BigDecimal = BigDecimal.valueOf(1000.0),
        dayFirstInstallment: LocalDate = LocalDate.now().plusMonths(1),
        numberOfInstallments: Int = 3,
        customerId: Long = 1
    ) = CreditDto(
        creditValue = creditValue,
        dayFirstInstallment = dayFirstInstallment,
        numberOfInstallments = numberOfInstallments,
        customerId = customerId
    )
}