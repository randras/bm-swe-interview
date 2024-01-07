package bankmonitor.model;


import bankmonitor.dto.TransactionDTO;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TransactionTest {


    @Test
    void test_convertTransactionToDTO() {

        Transaction tr = Transaction.builder().amount(BigDecimal.ONE).reference("REF_01").data("""
                {"reference": "REF_01", "amount": 1}
                """).build();

        TransactionDTO dto = TransactionMapper.convertToDTO(tr);

        assertEquals(dto.getReference(), tr.getReference());
        assertEquals(dto.getAmount(), tr.getAmount());
        assertEquals(dto.getData(), tr.getData());

    }

    @Test
    void test_convertDTOToTransaction() {

        String data = """
            { "reference": "foo", "amount": 100}
        """;
        TransactionDTO transactionDTO = TransactionMapper.parseDTO(data);

        Transaction transaction = TransactionMapper.convertFromDTO(transactionDTO);

        assertEquals(transaction.getReference(), "foo");
        assertEquals(0, BigDecimal.valueOf(100).compareTo(transaction.getAmount()));
        assertEquals(transaction.getData(), data);

    }

    @Test
    void test_parseDTOFromString() {
        String data = """
            { "reference": "foo", "amount": 100}
        """;
        TransactionDTO transactionDTO = TransactionMapper.parseDTO(data);

        assertEquals(transactionDTO.getReference(), "foo");
        assertEquals(transactionDTO.getData(), data);
        assertEquals(0, BigDecimal.valueOf(100).compareTo(transactionDTO.getAmount()));


        data = """
                { "amount": -100, "reference": "BM_2023_101_BACK", "reason": "duplicate" }
                """;

        transactionDTO = TransactionMapper.parseDTO(data);

        assertEquals(transactionDTO.getReference(), "BM_2023_101_BACK");
        assertEquals(transactionDTO.getData(), data);
        assertEquals(0, BigDecimal.valueOf(-100).compareTo(transactionDTO.getAmount()));


        data = """
                { "amount": 54321, "sender": "Bankmonitor", "recipient": "John Doe" }
                """;

        transactionDTO = TransactionMapper.parseDTO(data);
        assertEquals(transactionDTO.getReference(), "");
        assertEquals(transactionDTO.getData(), data);
        assertEquals(0, BigDecimal.valueOf(54321).compareTo(transactionDTO.getAmount()));

        data = """
                { "sender": "Bankmonitor", "recipient": "John Doe" }
                """;

        transactionDTO = TransactionMapper.parseDTO(data);
        assertEquals(transactionDTO.getReference(), "");
        assertEquals(transactionDTO.getData(), data);
        assertEquals(0, BigDecimal.valueOf(-1).compareTo(transactionDTO.getAmount()));

    }
}