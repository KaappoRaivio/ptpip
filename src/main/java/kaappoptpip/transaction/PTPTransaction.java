package kaappoptpip.transaction;

import kaappoptpip.packet.in.PTPDataPacketIn;
import kaappoptpip.packet.in.PTPInStream;
import kaappoptpip.packet.in.PTPPacketInEndData;
import kaappoptpip.packet.in.PTPPacketInStartData;
import kaappoptpip.packet._out.PTPPacketCmdRequest;

import java.io.ByteArrayInputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class PTPTransaction {
    private int transactionID;
    private PTPPacketCmdRequest initiatingPacket;
    private List<PTPDataPacketIn> packets;

    private long amountOfBytesLeft;
    private long dataSize;

    public PTPTransaction(int transactionID, PTPPacketCmdRequest initiatingPacket) {
        this.transactionID = transactionID;
        this.initiatingPacket = initiatingPacket;
        packets = new ArrayList<>();
//        packets.add(initialPacket);

//        amountOfBytesLeft = initialPacket.getTotalDataLengthInTransaction();
//        dataSize = amountOfBytesLeft;
    }

    public void addData (PTPPacketInStartData data) {
        packets.add(data);

        amountOfBytesLeft = data.getTotalDataLengthInTransaction();
        dataSize = amountOfBytesLeft;
    }

    public void addData (PTPDataPacketIn data) {
        packets.add(data);
        amountOfBytesLeft -= data.getAmountOfDataBytesInPacket();

        if (data instanceof PTPPacketInEndData && amountOfBytesLeft != 0) {
            throw new RuntimeException("Camera ended transaction but not all bytes received!");
        }
    }

    public boolean isComplete() {
        if (amountOfBytesLeft < 0) {
            throw new RuntimeException("too much data received!");
        }
        return amountOfBytesLeft == 0;
    }

    public PTPCompletedDataTransfer getTransactionData () {
        if (!isComplete()) throw new RuntimeException("Cannot get data of an incomplete transaction!");

        ByteBuffer data = ByteBuffer.allocate(Math.toIntExact(dataSize));

        for (PTPDataPacketIn packet : packets) {
            data.put(packet.getData());
        }

        return new PTPCompletedDataTransfer(transactionID, new PTPInStream(new ByteArrayInputStream(data.array())), initiatingPacket);
    }
}
