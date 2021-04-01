package kaappoptpip.transaction;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ParsedTransactionData {
    private Map<String, Object> fields;

    public ParsedTransactionData(Map<String, Object> fields) {
        this.fields = fields;
    }

    public Object getField (String key) {
        return Optional.ofNullable(fields.get(key)).orElseThrow();
    }
}
