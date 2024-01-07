
package bankmonitor.controller;

import bankmonitor.controller.TransactionController;
import bankmonitor.dto.TransactionDTO;
import bankmonitor.model.Transaction;
import bankmonitor.model.TransactionMapper;
import bankmonitor.repository.TransactionRepository;
import bankmonitor.service.TransactionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
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
    private TransactionService transactionService;

    @Test
    public void testGetAllTransactions() throws Exception {
        when(transactionService.findAllTransactions()).thenReturn(Arrays.asList(
                TransactionDTO.builder().reference("ref_1").amount(BigDecimal.ONE).build(),
                TransactionDTO.builder().reference("ref_2").amount(BigDecimal.ZERO).build()
        ));

        mockMvc.perform(get("/transactions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(2)));

        verify(transactionService, times(1)).findAllTransactions();
    }

    @Test
    public void testCreateTransaction() throws Exception {
        String jsonData = """
                        { "reference": "foo", "amount": 100}
                        """;

        TransactionDTO transactionDTO = TransactionMapper.parseDTO(jsonData);
        when(transactionService.createNewTransaction(any(String.class))).thenReturn(transactionDTO);

        mockMvc.perform(post("/transactions")
                        .contentType("application/json")
                        .content(jsonData))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reference",Matchers.equalTo("foo")))
                .andExpect(jsonPath("$.amount",Matchers.equalTo(100)))
                .andExpect(jsonPath("$.data",Matchers.equalTo(jsonData)));
        ;

        verify(transactionService, times(1)).createNewTransaction(any(String.class));
    }

    @Test
    public void testUpdateTransaction() throws Exception {
        Long id = 1L;
        String jsonData = """
                        { "reference": "foo0", "amount": 1000}
                        """;
        TransactionDTO transactionDTO = TransactionMapper.parseDTO(jsonData);
        when(transactionService.updateTransaction(id, jsonData)).thenReturn(Optional.of(transactionDTO));

        mockMvc.perform(put("/transactions/{id}", id)
                        .contentType("application/json")
                        .content(jsonData))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reference",Matchers.equalTo("foo0")))
                .andExpect(jsonPath("$.amount",Matchers.equalTo(1000)))
                .andExpect(jsonPath("$.data",Matchers.equalTo(jsonData)));

        verify(transactionService, times(1)).updateTransaction(id, jsonData);
    }
}