package bankmonitor.dto;

import org.json.JSONObject;

import java.time.LocalDateTime;

public record TransactionDTO(
        long id,
        LocalDateTime timestamp,
        String data
) {

    public static final String REFERENCE_KEY = "reference";

    public TransactionDTO {
        timestamp = LocalDateTime.now();
    }

    public Integer getAmount() {
        JSONObject jsonData = new JSONObject(this.data);
        if (jsonData.has("amount")) {
            return jsonData.getInt("amount");
        } else {
            return -1;
        }
    }

    public String getReference() {
        JSONObject jsonData = new JSONObject(this.data);
        if (jsonData.has(REFERENCE_KEY)) {
            return jsonData.getString(REFERENCE_KEY);
        } else {
            return "";
        }
    }
}