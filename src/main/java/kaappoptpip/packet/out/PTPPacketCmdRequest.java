package kaappoptpip.packet.out;

import kaappoptpip.packet.PTPPacketType;

public class PTPPacketCmdRequest extends PTPPacketOut {
    private int dataPhaseInfo = 1;
    public PTPPacketCmdRequest(int operationCode, int sessionID, int transactionID) {
        super(PTPPacketType.CMD_REQUEST);
        payload.writeUInt32(dataPhaseInfo);
        payload.writeUInt16(operationCode);
        payload.writeUInt32(transactionID);
        payload.writeUInt32(sessionID);
    }
}
