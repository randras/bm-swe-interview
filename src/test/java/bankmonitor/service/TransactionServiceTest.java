package bankmonitor.service;

import bankmonitor.dto.TransactionDTO;
import bankmonitor.model.Transaction;
import bankmonitor.repository.TransactionRepository;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
public class TransactionServiceTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TransactionRepository transactionRepository;

    private TransactionService transactionService;

    @BeforeEach
    public void setUp() {
        transactionService = new TransactionService(transactionRepository);
    }

    @Test
    public void testCreateNewTransaction() throws JSONException {
        // setup
        String transactionJsonData = """
            {"amount":100,"reference":"ref1"}
        """;

        // execute
        TransactionDTO createdTransactionDTO = transactionService.createNewTransaction(transactionJsonData);

        // assert
        Transaction transactionFromDb = entityManager.find(Transaction.class, createdTransactionDTO.getId());

        assertNotNull(transactionFromDb);
        assertEquals(new JSONObject(transactionJsonData).toString(), new JSONObject(transactionFromDb.getData()).toString());
        assertEquals(createdTransactionDTO.getAmount(), transactionFromDb.getAmount());
        assertEquals(createdTransactionDTO.getReference(), transactionFromDb.getReference());

        assertEquals("ref1", transactionFromDb.getReference());
        assertEquals(createdTransactionDTO.getReference(), transactionFromDb.getReference());
    }

    @Test
    public void testUpdateTransaction() throws JSONException {
        // setup
        String initialTransactionJsonData = """
            {"amount":100,"reference":"ref1", "additionData": "xyz"}
        """;
        String newTransactionJsonData = """
            {"amount":200,"reference":"ref2"}
        """;

        // we expcet the additionData entry will remain in the data column, since the original code only updated the amount and reference values.
        String expectedTransactionJsonData = """
            {"amount":200,"reference":"ref2", "additionData": "xyz"}
        """;

        TransactionDTO newTransactionDTO = transactionService.createNewTransaction(initialTransactionJsonData);

        assertEquals(BigDecimal.valueOf(100), newTransactionDTO.getAmount());
        assertEquals( "ref1", newTransactionDTO.getReference());

        // execute
        transactionService.updateTransaction(newTransactionDTO.getId(), newTransactionJsonData);

        // assert
        Transaction updatedTransactionFromDb = entityManager.find(Transaction.class, newTransactionDTO.getId());

        assertNotNull(updatedTransactionFromDb);
        assertEquals(new JSONObject(expectedTransactionJsonData).toString(), new JSONObject(updatedTransactionFromDb.getData()).toString());
    }

}