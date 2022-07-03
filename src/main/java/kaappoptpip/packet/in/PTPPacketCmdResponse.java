package kaappoptpip.packet.in;

import kaappoptpip.packet.PTPPacketType;

public class PTPPacketCmdResponse extends PTPPacketIn {
    private int streamLength;

    public int getResponseCode() {
        return responseCode;
    }

    private int responseCode;
    private int transactionID;

    public PTPPacketCmdResponse(PTPInStream packetContent, int streamLength) {
        super(PTPPacketType.CMD_RESPONSE, packetContent);
        this.streamLength = streamLength;

        this.responseCode = packetContent.readUInt16();
        this.transactionID = packetContent.readUInt32();
    }

    @Override
    public String toString() {
        return "PTPPacketCmdResponse{" +
                "streamLength=" + streamLength +
                ", responseCode=0x" + Integer.toHexString(responseCode) +
                ", transactionID=" + transactionID +
                '}';
    }

    @Override
    public int size () {
        return streamLength;
    }

    @Override
    public int getTransactionID () {
        return transactionID;
    }
}
