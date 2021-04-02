package kaappoptpip.packet;

import kaappoptpip.packet.in.PTPPacketIn;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public class PTPPacketInMatcher {
    private int transactionID = -1;
    private Set<PTPPacketType> packetTypes;

    public PTPPacketInMatcher () {}

    public PTPPacketInMatcher packetTypes (Set<PTPPacketType> packetTypes) {
        this.packetTypes = packetTypes;
        return this;
    }

    public PTPPacketInMatcher packetType (PTPPacketType packetType) {
        return packetTypes(Collections.singleton(packetType));
    }

    public PTPPacketInMatcher transactionID (int transactionID) {
        this.transactionID = transactionID;
        return this;
    }

    public boolean matches (PTPPacketIn packetIn) {
        if (packetTypes != null && !packetTypes.contains(packetIn.getPacketType())) {
            return false;
        }

        if (transactionID != -1 && transactionID != packetIn.getTransactionID()) {
            return false;
        }

        return true;
    }
}
