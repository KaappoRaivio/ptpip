package kaappoptpip.packet._out;

import kaappoptpip.packet.PTPPacketType;

import java.util.List;

public class PTPPacketCmdRequest extends PTPPacketOut {
    private final int operationCode;
    private final int transactionID;
    private List<Integer> parameters;

    public static class OpCodes {
        public static final int OPEN_SESSION = 0x1002;
        public static final int GET_DEVICE_INFO = 0x1001;
        public static final int START_LIVE_VIEW = 0x9201;
        public static final int AF_DRIVE = 0x90C1;
        public static final int INITIATE_CAPTURE = 0x100E;
        public static final int SET_DEVICE_PROP_VALUE = 0x1016;
        public static final int CLOSE_SESSION = 0x1003;
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
        payload.writeUInt32(dataPhaseInfo);
        payload.writeUInt16(operationCode);
        payload.writeUInt32(transactionID);


        System.out.println("Parameters size: " + parameters.size());
        for (int parameter : parameters) {
            payload.writeUInt32(parameter);
        }
    }

    @Override
    public boolean startsTransaction() {
        return true;
    }

    public int getTransactionID () {
        return transactionID;
    }

    public int getOperationCode () {
        return operationCode;
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
