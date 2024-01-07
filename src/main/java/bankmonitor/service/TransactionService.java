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

import static bankmonitor.model.TransactionMapper.convertFromDTO;
import static bankmonitor.model.TransactionMapper.parseDTO;

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
        JSONObject updateJson = new JSONObject(transactionJsonData);
        Optional<Transaction> optionalTransaction = transactionRepository.findById(id);

        if (!optionalTransaction.isPresent()) {
            return Optional.empty();
        }

        Transaction transaction = optionalTransaction.get();
        JSONObject trdata = new JSONObject(transaction.getData());

        if (updateJson.has(TransactionConstants.AMOUNT_KEY)) {
            trdata.put(TransactionConstants.AMOUNT_KEY, updateJson.getInt(TransactionConstants.AMOUNT_KEY));
        }

        if (updateJson.has(TransactionConstants.REFERENCE_KEY)) {
            trdata.put(TransactionConstants.REFERENCE_KEY, updateJson.getString(TransactionConstants.REFERENCE_KEY));
        }
        transaction.setData(trdata.toString());

        Transaction updatedTransaction = transactionRepository.save(transaction);

        return Optional.of(TransactionMapper.convertToDTO(updatedTransaction));
    }
}

