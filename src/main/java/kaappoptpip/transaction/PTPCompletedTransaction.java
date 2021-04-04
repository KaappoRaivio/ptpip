package kaappoptpip.transaction;

import kaappoptpip.packet.in.PTPPacketIn;

import java.util.List;

public class PTPCompletedTransaction {
    private final PTPCompletedDataTransfer transactionData;
    private final PTPPacketIn responsePacket;
    private final List<PTPPacketIn> allPackets;

    public PTPCompletedTransaction (PTPPacketIn responsePacket, PTPCompletedDataTransfer transactionData, List<PTPPacketIn> allPackets) {
        this.responsePacket = responsePacket;
        this.transactionData = transactionData;
        this.allPackets = allPackets;
    }

    public boolean hasTransactionData () {
        return transactionData != null;
    }

    public PTPCompletedDataTransfer getTransactionData () {
        return transactionData;
    }

    public PTPPacketIn getResponsePacket () {
        return responsePacket;
    }


    public List<PTPPacketIn> getAllPackets () {
        return allPackets;
    }
}
