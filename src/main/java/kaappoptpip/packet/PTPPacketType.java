package kaappoptpip.packet;

import kaappoptpip.packet.in.PTPPacketIn;

import java.util.Arrays;

public enum PTPPacketType {

    INIT_COMMAND_REQUEST,
    INIT_COMMAND_ACKNOWLEDGEMENT,
    INIT_EVENT_REQUEST,
    INIT_EVENT_ACKNOWLEDGEMENT,
    INIT_FAIL,
    CMD_REQUEST,
    CMD_RESPONSE,
    EVENT,
    START_DATA_PACKET,
    DATA_PACKET,
    CANCEL_TRANSACTION,
    END_DATA_PACKET,
    PING,
    PONG;

    private Class<? extends PTPPacketIn> packetClass;
    public int getPayload () {
        return ordinal() + 1;
    }

    public static PTPPacketType getFromPayload (int payload) {
        return Arrays.stream(values()).filter(a -> a.getPayload() == payload).findFirst().orElseThrow();
    }
}
