package kaappoptpip;

import kaappoptpip.packet._out.PTPDataPacketOut;
import kaappoptpip.packet._out.PTPPacketOut;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PTPRequestOut {
    private PTPPacketOut mainPacket;
    private List<PTPDataPacketOut> dataPackets;

    public PTPRequestOut (PTPPacketOut mainPacket) {
        this(mainPacket, Collections.emptyList());
    }

    public PTPRequestOut (PTPPacketOut mainPacket, List<PTPDataPacketOut> dataPackets) {
        this.mainPacket = mainPacket;
        this.dataPackets = dataPackets;
    }

    public PTPPacketOut getMainPacket () {
        return mainPacket;
    }

    public List<PTPPacketOut> getPacketsToWrite () {
        List<PTPPacketOut> result = new ArrayList<>();
        result.add(mainPacket);
        result.addAll(dataPackets);
        return result;
    }
}
