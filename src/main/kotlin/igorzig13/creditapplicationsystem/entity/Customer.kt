package igorzig13.creditapplicationsystem.entity

import jakarta.persistence.*
import java.math.BigDecimal

@Entity
//@Table(name = "customer")
data class Customer(
    @Column(nullable = false)
    var firstName: String = "",

    @Column(nullable = false)
    var lastName: String = "",

    @Column(nullable = false, unique = true)
    val cpf: String,

    @Column(nullable = false)
    var income: BigDecimal = BigDecimal.ZERO,

    @Column(nullable = false, unique = true)
    var email: String,

    @Column(nullable = false)
    var password: String = "",

    @Column(nullable = false) @Embedded
    var address: Address = Address(),

    @Column(nullable = false) @OneToMany(
        fetch = FetchType.LAZY,
        cascade = [(CascadeType.REMOVE)],
        mappedBy = "customer")
    val credits: List<Credit> = mutableListOf(),

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
)