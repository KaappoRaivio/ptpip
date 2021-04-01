package kaappoptpip.packet.in;

import kaappoptpip.packet.PTPPacketType;

public class PTPPacketInStartData extends PTPDataPacketIn {
    private int transactionID;
    private long totalDataLengthInTrasaction;

    protected PTPPacketInStartData(PTPInStream packetContent) {
        super(PTPPacketType.START_DATA_PACKET, packetContent);

        transactionID = packetContent.readUInt32();
        totalDataLengthInTrasaction = packetContent.readUInt64();
    }

    @Override
    public boolean startsTransactionResponse() {
        return true;
    }

    @Override
    public int size() {
        return 8 + (int) totalDataLengthInTrasaction;
    }

    @Override
    public int getTransactionID() {
        return transactionID;
    }

    @Override
    public byte[] getData() {
        return new byte[0];
    }

    public long getTotalDataLengthInTransaction() {
        return totalDataLengthInTrasaction;
    }

    @Override
    public long getAmountOfDataBytesInPacket() {
        return getTotalDataLengthInTransaction();
    }
}
