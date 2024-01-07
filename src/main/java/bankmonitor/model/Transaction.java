package bankmonitor.model;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "transaction")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime timestamp;

    @Column(name = "data")
    private String data;


    // these attributes seem to relevant so maybe worth to refactor them into separate fields.
    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "reference")
    private String reference;


}