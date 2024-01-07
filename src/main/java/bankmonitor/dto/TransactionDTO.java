package bankmonitor.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.math.BigDecimal;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionDTO  {
    private Long id;
    private BigDecimal amount;
    private String reference;
    private String data;

    public BigDecimal getAmount() {
        if (amount == null) {
            return BigDecimal.valueOf(-1);
        }
        return amount;
    }

    public String getReference() {
       if (reference == null) {
           return "";
       }
       return reference;
    }
}