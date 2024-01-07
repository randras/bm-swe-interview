package bankmonitor.model;

import bankmonitor.dto.TransactionDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TransactionMapper {

    public static TransactionDTO convertToDTO(Transaction transaction) {
        return new TransactionDTO(transaction.getId(), transaction.getAmount(), transaction.getReference(), transaction.getData());
    }


    /**
     * Parses the given transaction JSON data into a TransactionDTO object.
     *
     * @param transactionJsonData The JSON data representing the transaction.
     * @return The parsed TransactionDTO object.
     */
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

    public static JsonNode parseNode(String data) {
        try {
            return new ObjectMapper().readTree(data);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Merges the JSON data from the source into the target JSON data and returns the merged JSON as a string.
     *
     * @param target The target JSON data.
     * @param source The source JSON data.
     * @return The merged JSON data as a string.
     * @throws RuntimeException If there is an error processing the JSON.
     */
    public static String mergeJsonData(String target, String source) {
        JsonNode sourceData = parseNode(source);
        JsonNode targetData = parseNode(target);

        ObjectNode merged = ((ObjectNode)targetData).setAll((ObjectNode)sourceData);

        try {
            return new ObjectMapper().writeValueAsString(merged);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
