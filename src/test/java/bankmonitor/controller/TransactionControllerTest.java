
package bankmonitor.controller;

import bankmonitor.controller.TransactionController;
import bankmonitor.model.Transaction;
import bankmonitor.repository.TransactionRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = TransactionController.class)
@AutoConfigureMockMvc
public class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionRepository transactionRepository;

    @Test
    public void testGetAllTransactions() throws Exception {
        when(transactionRepository.findAll()).thenReturn(Arrays.asList(
                new Transaction("""
                            { "reference": "foo", "amount": 100}
                            """
                ),
                new Transaction("""
                            { "reference": "foo", "amount": 100}
                            """
                )
        ));

        mockMvc.perform(get("/transactions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(2)));

        verify(transactionRepository, times(1)).findAll();
    }

    @Test
    public void testCreateTransaction() throws Exception {
        String jsonData = """
                        { "reference": "foo", "amount": 100}
                        """;

        Transaction transaction = new Transaction(jsonData);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

        mockMvc.perform(post("/transactions")
                        .contentType("application/json")
                        .content(jsonData))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reference",Matchers.equalTo("foo")))
                .andExpect(jsonPath("$.amount",Matchers.equalTo(100)))
                .andExpect(jsonPath("$.data",Matchers.equalTo(jsonData.replaceAll("\\s+",""))));
        ;

        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    public void testUpdateTransaction() throws Exception {
        Long id = 1L;
        String jsonData = """
                        { "reference": "foo0", "amount": 1000}
                        """;
        Transaction transaction = new Transaction(jsonData);
        when(transactionRepository.findById(id)).thenReturn(Optional.of(transaction));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

        mockMvc.perform(put("/transactions/{id}", id)
                        .contentType("application/json")
                        .content("{}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reference",Matchers.equalTo("foo0")))
                .andExpect(jsonPath("$.amount",Matchers.equalTo(1000)))
                .andExpect(jsonPath("$.data",Matchers.equalTo(jsonData.replaceAll("\\s+",""))));

        verify(transactionRepository, times(1)).findById(id);
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }
}