package kaappoptpip.packet.in;

import kaappoptpip.packet.PTPPacketType;

public class PTPPacketInData extends PTPDataPacketIn {
    private int transactionID;
    private int dataLength;
    private byte[] data;

    PTPPacketInData(PTPInStream packetContent) {
        super(PTPPacketType.DATA_PACKET, packetContent);

        transactionID = packetContent.readUInt32();
        data = packetContent.readAllBytes();
        dataLength = data.length;
    }

    @Override
    public long getAmountOfDataBytesInPacket() {
        return dataLength;
    }

    @Override
    public int getTransactionID () {
        return transactionID;
    }

    @Override
    public byte[] getData() {
        return data;
    }

    @Override
    public int size() {
        throw new RuntimeException("Not implemented!");
    }

}
