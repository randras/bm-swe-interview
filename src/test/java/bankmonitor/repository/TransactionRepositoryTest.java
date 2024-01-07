package bankmonitor.repository;

import bankmonitor.model.Transaction;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.core.env.Environment;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class TransactionRepositoryTest {

    @Autowired
    private TransactionRepository repository;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private Environment environment;

    @Test
    public void testSave(){

        Transaction transaction = Transaction.builder()
                .reference("foo")
                .amount(BigDecimal.valueOf(1000))
                .build();
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
        Transaction transaction = Transaction.builder()
                .reference("foo00")
                .amount(BigDecimal.valueOf(1000))
                .data("""
                        "{reference": "foo", "amount": 1000}
                        """)
                .id(1L)
                .build();


        repository.save(transaction);

        Optional<Transaction> maybeTransaction = repository.findById(transaction.getId());
        assertTrue(maybeTransaction.isPresent());
        assertEquals(transaction.getReference(), maybeTransaction.get().getReference());
        assertEquals(transaction.getAmount(), maybeTransaction.get().getAmount());
    }

}