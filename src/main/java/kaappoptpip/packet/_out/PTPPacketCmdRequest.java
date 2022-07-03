package kaappoptpip.packet._out;

import kaappoptpip.packet.PTPPacketType;

import java.util.List;

public class PTPPacketCmdRequest extends PTPPacketOut {
    private final int operationCode;
    private int transactionID;
    private List<Integer> parameters;

    public static class OpCodes {
        public static final int OPEN_SESSION = 0x1002;
        public static final int GET_DEVICE_INFO = 0x1001;
        public static final int START_LIVE_VIEW = 0x9201;
        public static final int AF_DRIVE = 0x90C1;
        public static final int INITIATE_CAPTURE = 0x100E;
        public static final int GET_DEVICE_PROP_VALUE = 0x1015;
        public static final int SET_DEVICE_PROP_VALUE = 0x1016;
        public static final int CLOSE_SESSION = 0x1003;
        public static final int DEVICE_READY = 0x90C8;
        public static final int GET_LIVE_VIEW_IMAGE = 0x9203;
    }

    private int dataPhaseInfo = 1;

    public PTPPacketCmdRequest(int operationCode, int transactionID) {
        this(operationCode, transactionID, List.of());
    }

    public PTPPacketCmdRequest(int operationCode, int transactionID, List<Integer> parameters) {
        super(PTPPacketType.CMD_REQUEST);
        this.operationCode = operationCode;
        this.transactionID = transactionID;
        this.parameters = parameters;

        rewrite();
    }

    private void rewrite () {
        payload.clear();

        payload.writeUInt32(dataPhaseInfo);
        payload.writeUInt16(operationCode);
        payload.writeUInt32(transactionID);


        for (int parameter : parameters) {
            payload.writeUInt32(parameter);
        }
    }

    @Override
    public boolean startsTransaction() {
        return true;
    }

    @Override
    public int getTransactionID () {
        return transactionID;
    }

    @Override
    public void setTransactionId (int transactionID) {
        this.transactionID = transactionID;
        rewrite();
    }

    @Override
    public boolean hasTransactionId () {
        return true;
    }

    public int getOperationCode () {
        return operationCode;
    }

    public List<Integer> getParameters() {
        return parameters;
    }

    @Override
    public String toString() {
        return "PTPPacketCmdRequest{" +
                "operationCode=0x" + Integer.toHexString(operationCode) +
                ", transactionID=" + transactionID +
                ", dataPhaseInfo=" + dataPhaseInfo +
                '}';
    }
}
