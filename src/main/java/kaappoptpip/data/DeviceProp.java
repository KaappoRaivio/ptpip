package kaappoptpip.data;

public class DeviceProp {
    private final int propCode;
    private final PTPDataType dataType;
    private final String name;
    private final boolean readOnly;

    public DeviceProp(int propCode, PTPDataType dataType, String name, boolean readOnly) {
        this.propCode = propCode;
        this.dataType = dataType;
        this.name = name;
        this.readOnly = readOnly;
    }

    public int getPropCode () {
        return propCode;
    }

    public PTPDataType getDataType () {
        return dataType;
    }

    public boolean isReadOnly () {
        return readOnly;
    }

    public String getName () {
        return name;
    }
}
