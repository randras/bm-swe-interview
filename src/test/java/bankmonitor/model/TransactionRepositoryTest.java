package bankmonitor.model;

import bankmonitor.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class TransactionRepositoryTest {

    @Autowired
    private TransactionRepository repository;

    @Test
    public void testSave(){
        Transaction transaction = new Transaction("""
            { "reference": "foo", "amount": 1000.00}
        """);
        transaction.setId(1L);

        Transaction savedTransaction = repository.save(transaction);

        Optional<Transaction> optRetrievedTransaction = repository.findById(savedTransaction.getId());

        assertTrue(optRetrievedTransaction.isPresent());
        assertEquals(optRetrievedTransaction.get().getId(), transaction.getId());
        assertEquals(optRetrievedTransaction.get().getAmount(), transaction.getAmount());
        assertEquals(optRetrievedTransaction.get().getReference(), transaction.getReference());
    }

    @Test
    public void testFindById() {
        Transaction transaction = new Transaction("""
            { "reference1": "foo", "amount": 100.00}
        """);

        transaction.setId(1);

        // Call to the save method and force an immediate check with the flush method
        repository.save(transaction);
        repository.flush();

        Optional<Transaction> maybeTransaction = repository.findById(transaction.getId());
        assertTrue(maybeTransaction.isPresent());
        assertEquals(transaction.getReference(), maybeTransaction.get().getReference());
        assertEquals(transaction.getAmount(), maybeTransaction.get().getAmount());
    }

}