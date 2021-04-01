package kaappoptpip.connection;

import kaappoptpip.packet.in.PTPInStream;
import kaappoptpip.packet.out.PTPPacketCmdRequest;

public class CompletedPTPTransaction {
    private int transactionID;
    private PTPInStream transactionData;
    private PTPPacketCmdRequest initiatingPacket;

    public CompletedPTPTransaction(int transactionID, PTPInStream transactionData, PTPPacketCmdRequest initiatingPacket) {
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
