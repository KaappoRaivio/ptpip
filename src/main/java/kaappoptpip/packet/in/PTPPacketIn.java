package kaappoptpip.packet.in;

import kaappoptpip.packet.PTPPacketType;

abstract public class PTPPacketIn {
    protected PTPPacketType packetType;
    protected PTPInStream packetContent;

    protected PTPPacketIn (PTPPacketType packetType, PTPInStream packetContent) {
        this.packetType = packetType;
        this.packetContent = packetContent;
    }
    public PTPPacketType getPacketType () {
        return packetType;
    }

    public static PTPPacketIn getPacket (int packetType, PTPInStream packetContent) {
        PTPPacketType type = PTPPacketType.getFromPayload(packetType);

        switch (type) {
            case INIT_COMMAND_ACKNOWLEDGEMENT:
                return new PTPTPacketInitAcknowledgement(packetContent);
            case INIT_EVENT_ACKNOWLEDGEMENT:
                return new PTPPacketInitEventAcknowledgement(packetContent);
            case PONG:
                return new PTPPacketPong(packetContent);
            case CMD_RESPONSE:
                return new PTPPacketCmdResponse(packetContent, packetContent.getStreamLength());
            case START_DATA_PACKET:
                return new PTPPacketInStartData(packetContent);
            case DATA_PACKET:
                return new PTPPacketInData(packetContent);
            case END_DATA_PACKET:
                return new PTPPacketInEndData(packetContent);
            default:
                return new PTPDebugPacket(type, packetContent);
        }
    }

    public boolean isSuitableForRepresenting (int packetType) {
        return this.packetType.getPayload() == packetType;
    }

    public boolean startsTransactionResponse() {
        return false;
    }
    public boolean endsTransactionResponse() {
        return false;
    }

    public boolean isPartOfTransaction () {
        return false;
    };

    abstract public int size ();
}
