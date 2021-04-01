package kaappoptpip.packet.in;

import kaappoptpip.packet.PTPPacketType;

public class PTPPacketInEndData extends PTPDataPacketIn{
    private int transactionID;
    private byte[] data;
    private int dataLength;

    PTPPacketInEndData(PTPInStream packetContent) {
        super(PTPPacketType.END_DATA_PACKET, packetContent);

        transactionID = packetContent.readUInt32();
        data = packetContent.readAllBytes();
        dataLength = data.length;
    }

    @Override
    public boolean endsTransactionResponse() {
        return true;
    }

    @Override
    public long getAmountOfDataBytesInPacket() {
        return dataLength;
    }

    @Override
    public int getTransactionID() {
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
