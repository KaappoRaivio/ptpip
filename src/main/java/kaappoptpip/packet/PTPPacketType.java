package kaappoptpip.packet;

import kaappoptpip.packet.in.PTPPacketIn;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

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

    public static Set<PTPPacketType> getPossibleResponseTypes (PTPPacketType requestType) {
        switch (requestType) {
            case INIT_COMMAND_REQUEST:
                return Set.of(INIT_COMMAND_ACKNOWLEDGEMENT, INIT_FAIL);
            case INIT_EVENT_REQUEST:
                return Set.of(INIT_EVENT_ACKNOWLEDGEMENT, INIT_FAIL);
            case CMD_REQUEST:
                return Set.of(CMD_RESPONSE, CANCEL_TRANSACTION);
            case PING:
                return Collections.singleton(PONG);
            default:
                throw new RuntimeException("Not applicable to " + requestType + "!");
        }
    }

    private Class<? extends PTPPacketIn> packetClass;
    public int getPayload () {
        return ordinal() + 1;
    }

    public static PTPPacketType getFromPayload (int payload) {
        return Arrays.stream(values()).filter(a -> a.getPayload() == payload).findFirst().orElseThrow();
    }
}
