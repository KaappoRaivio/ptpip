package kaappoptpip.packet.in;

import kaappoptpip.packet.PTPPacketType;

import java.util.Arrays;

public class PTPTPacketInitCommandAcknowledgement extends PTPPacketIn {
    private int connectionNumber;
    private byte[] guid;
    private String hostName;
    private String version;

    protected PTPTPacketInitCommandAcknowledgement(PTPInStream content) {
        super(PTPPacketType.INIT_COMMAND_ACKNOWLEDGEMENT, content);
        connectionNumber = content.readUInt32();
        guid = content.readBytes(16);
        hostName = content.readWChar();

        int versionMinor = content.readUInt16();
        int versionMajor = content.readUInt16();

        version = versionMajor + "." + versionMinor;
    }

    public int getConnectionNumber() {
        return connectionNumber;
    }

    public byte[] getGuid() {
        return guid;
    }

    public String getHostName() {
        return hostName;
    }

    public String getVersion() {
        return version;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public String toString() {
        return "kaappoptpip.packet.in.PTPTPacketInitAcknowledgement{" +
                "connectionNumber=" + connectionNumber +
                ", guid=" + Arrays.toString(guid) +
                ", hostName='" + hostName + '\'' +
                ", version='" + version + '\'' +
                '}';
    }
}
