package bankmonitor.model;

import bankmonitor.dto.TransactionDTO;
import bankmonitor.model.Transaction;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TransactionMapper {

    public static TransactionDTO convertToDTO(Transaction transaction) {
        return new TransactionDTO(transaction.getId(), transaction.getAmount(), transaction.getReference(), transaction.getData());
    }

    public static TransactionDTO parseDTO(String transactionJsonData)  {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        TransactionDTO transactionDTO;
        try {
            transactionDTO = objectMapper.readValue(transactionJsonData, TransactionDTO.class);
        } catch (JsonProcessingException e) {
            log.warn("Can not process json", e);
            transactionDTO = new TransactionDTO(null, null, null, transactionJsonData);
        }
        transactionDTO.setData(transactionJsonData);

        return transactionDTO;

    }

    public static Transaction convertFromDTO(TransactionDTO transactionDTO) {
        return Transaction.builder()
                .reference(transactionDTO.getReference())
                .amount(transactionDTO.getAmount())
                .id(transactionDTO.getId())
                .data(transactionDTO.getData())
                .build();
    }
}
