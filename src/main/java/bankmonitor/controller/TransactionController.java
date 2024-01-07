package bankmonitor.controller;

import bankmonitor.dto.TransactionDTO;
import bankmonitor.model.Transaction;
import bankmonitor.repository.TransactionRepository;
import bankmonitor.service.TransactionService;
import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController("/")
@Log
public class TransactionController {

    private TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }


    @GetMapping("/transactions")
    public List<TransactionDTO> getAllTransactions() {
        return transactionService.findAllTransactions();
    }

    @PostMapping("/transactions")
    public TransactionDTO createTransaction(@RequestBody String transactionJsonData) {
        return transactionService.createNewTransaction(transactionJsonData);
    }

    @PutMapping("/transactions/{id}")
    public ResponseEntity<TransactionDTO> updateTransaction(@PathVariable Long id, @RequestBody String update) {
        Optional<TransactionDTO> updatedTransaction = transactionService.updateTransaction(id, update);

        if (!updatedTransaction.isPresent()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(updatedTransaction.get());
    }
}