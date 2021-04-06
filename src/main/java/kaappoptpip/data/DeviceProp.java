package kaappoptpip.data;

import kaappoptpip.packet.in.PTPInStream;

import java.util.Map;
import java.util.Optional;

public class DeviceProp<T> {
    private final int propCode;
    private final PTPDataType<T> dataType;
    private final String name;
    private final boolean readOnly;
    private final Map<T, String> valueMapping;

    public DeviceProp (int propCode, PTPDataType<T> dataType, String name) {
        this(propCode, dataType, name, false);
    }

    public DeviceProp (int propCode, PTPDataType<T> dataType, String name, boolean readOnly) {
        this(propCode, dataType, name, readOnly, Map.of());
    }

    public DeviceProp (int propCode, PTPDataType<T> dataType, String name, boolean readOnly, Map<T, String> valueMapping) {
        this.propCode = propCode;
        this.dataType = dataType;
        this.name = name;
        this.readOnly = readOnly;
        this.valueMapping = valueMapping;
    }

    public int getPropCode () {
        return propCode;
    }

    public PTPDataType<T> getDataType () {
        return dataType;
    }

    public boolean isReadOnly () {
        return readOnly;
    }

    public String getName () {
        return name;
    }

    public Map<T, String> getValueMapping () {
        return valueMapping;
    }

    public String parseDataString (PTPInStream transactionData) {
        T rawValue = dataType.parseData(transactionData);

        String possibleValueMapping = valueMapping.get(rawValue);

        return Optional.ofNullable(possibleValueMapping).orElse(String.valueOf(rawValue));
    }

    @Override
    public String toString () {
        return "DeviceProp{" +
                "propCode=0x" + Integer.toHexString(propCode) +
                ", dataType=" + dataType +
                ", name='" + name + '\'' +
                ", readOnly=" + readOnly +
                ", valueMapping=" + valueMapping +
                '}';
    }
}
