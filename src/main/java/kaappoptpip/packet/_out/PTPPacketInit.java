package kaappoptpip.packet._out;

import kaappoptpip.packet.PTPPacketType;

public class PTPPacketInit extends PTPPacketOut {
    public PTPPacketInit(short[] guid, String hostName, String version) {
        super(PTPPacketType.INIT_COMMAND_REQUEST);
        payload.writeShortsAsBytes(guid);
        payload.writeWChar(hostName);
        int versionMajor = Integer.parseInt(version.split("\\.")[0]);
        int versionMinor = Integer.parseInt(version.split("\\.")[1]);
        payload.writeUInt16(versionMajor);
        payload.writeUInt16(versionMinor);
    }
}
