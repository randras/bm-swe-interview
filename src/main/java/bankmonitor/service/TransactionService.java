package bankmonitor.service;

import bankmonitor.TransactionConstants;
import bankmonitor.model.TransactionMapper;
import bankmonitor.dto.TransactionDTO;
import bankmonitor.model.Transaction;
import bankmonitor.repository.TransactionRepository;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static bankmonitor.model.TransactionMapper.*;

@Service
@Slf4j
public class TransactionService {

    private TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public List<TransactionDTO> findAllTransactions() {
        return transactionRepository.findAll().stream().map(TransactionMapper::convertToDTO).collect(Collectors.toList());
    }

    @Transactional
    public TransactionDTO createNewTransaction(String transactionJsonData) {

        TransactionDTO transactionDTO = parseDTO(transactionJsonData);

        Transaction transaction = convertFromDTO(transactionDTO);

        transaction = transactionRepository.save(transaction);

        return TransactionMapper.convertToDTO(transaction);
    }

    @Transactional
    public Optional<TransactionDTO> updateTransaction(Long id, String transactionJsonData) {
        TransactionDTO transactionDTO = TransactionMapper.parseDTO(transactionJsonData);
        Optional<Transaction> optionalTransaction = transactionRepository.findById(id);

        if (!optionalTransaction.isPresent()) {
            return Optional.empty();
        }

        Transaction transaction = optionalTransaction.get();
        transaction.setAmount(transactionDTO.getAmount());
        transaction.setReference(transactionDTO.getReference());
        transaction.setData(mergeJsonData(transaction.getData(),transactionDTO.getData()));

        Transaction updatedTransaction = transactionRepository.save(transaction);

        return Optional.of(TransactionMapper.convertToDTO(updatedTransaction));
    }
}

