package ng.safebanking.data.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.math.BigDecimal;
import java.time.LocalDateTime;


@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "account")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false)
    private String accountName;
    @Column(nullable = false, unique = true, length = 10)
    private String accountNumber;
    @Column(nullable = false)
    private BigDecimal balance = BigDecimal.ZERO;
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();


}
