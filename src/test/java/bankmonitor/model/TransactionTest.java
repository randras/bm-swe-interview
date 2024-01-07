package bankmonitor.model;

import bankmonitor.model.Transaction;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TransactionTest {

    @Test
    void test_getData() {
        Transaction tr = new Transaction("""
            { "reference": "foo", "amount": 100}
        """);

        assertEquals(tr.getReference(), "foo");
        assertEquals(tr.getAmount(), 100);
    }
}