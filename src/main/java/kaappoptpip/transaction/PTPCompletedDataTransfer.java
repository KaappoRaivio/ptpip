package kaappoptpip.transaction;

import kaappoptpip.packet.in.PTPInStream;
import kaappoptpip.packet._out.PTPPacketCmdRequest;

public class PTPCompletedDataTransfer {
    private final int transactionID;
    private final PTPInStream transactionData;
    private final PTPPacketCmdRequest initiatingPacket;

    public PTPCompletedDataTransfer (int transactionID, PTPInStream transactionData, PTPPacketCmdRequest initiatingPacket) {
        this.transactionID = transactionID;
        this.transactionData = transactionData;
        this.initiatingPacket = initiatingPacket;
    }

    public int getTransactionID() {
        return transactionID;
    }

    public PTPInStream getTransactionData() {
        return transactionData;
    }

    public PTPPacketCmdRequest getInitiatingPacket() {
        return initiatingPacket;
    }

    @Override
    public String toString() {
        return "CompletedPTPTransaction{" +
                "transactionID=" + transactionID +
                ", transactionData=" + transactionData +
                ", initiatingPacket=" + initiatingPacket +
                '}';
    }
}
