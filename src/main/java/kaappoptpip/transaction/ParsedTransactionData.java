package kaappoptpip.transaction;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ParsedTransactionData {
    private Map<String, Object> fields;

    public ParsedTransactionData () {
        this.fields = new HashMap<>();
    }

    public void add (String key, Object value) {
        fields.put(key, value);
    }

    public Object getField (String key) {
        return Optional.ofNullable(fields.get(key)).orElseThrow();
    }

    @Override
    public String toString () {
        StringBuilder builder = new StringBuilder("ParsedTransactionData {\n");
        for (String key : fields.keySet()) {
            builder.append("\t[").append(key).append("]: ").append(fields.get(key)).append("\n");
        }
        builder.append("}");

        return builder.toString();
    }
}
