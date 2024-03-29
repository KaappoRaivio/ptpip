package kaappoptpip.packet.in;

import kaappoptpip.misc.ByteUtils;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

public class PTPInStreamReader extends Thread {
    private PTPInStream inputStream;
    private String name;
    private boolean debug;
    private boolean run = true;

    public final Object lock = new Object();

    private ByteArrayOutputStream buffer;
    private volatile List<PTPInStream> packets;

    public PTPInStreamReader (InputStream inputStream) {
        this(inputStream, "");
    }

    public PTPInStreamReader (InputStream inputStream, String name) {
        this.inputStream = new PTPInStream(inputStream);
        this.name = name;
        buffer = new ByteArrayOutputStream();
        packets = new ArrayList<>();
    }

    @Override
    public void run () {
        boolean inPacket = false;

        int packetLength = 0;
        while (run) {
            if (!inPacket) {
                    packetLength = inputStream.readUInt32();
                    inPacket = true;
            } else {
                ByteBuffer buffer = ByteBuffer.allocate(packetLength - 4);
                buffer.order(ByteOrder.LITTLE_ENDIAN);

                byte[] data = inputStream.readBytes(packetLength - 4);
                String s = ByteUtils.bytesToHex(ByteUtils.toLittleEndian(packetLength)) + ByteUtils.bytesToHex(data);
                if (s.length() > 500) {
                    s = "Truncated";
                }
                System.out.println("<== INCOMING PACKET " + s);
                packets.add(new PTPInStream(new ByteArrayInputStream(data)));

                synchronized (lock) {
                    lock.notifyAll();
                }

                inPacket = false;
                packetLength = 0;
            }
        }
    }

    public List<PTPInStream> getPackets () {
        var toReturn = packets;
        packets = new ArrayList<>();
        return toReturn;
    }

    public void close () {
        run = false;
    }
}
