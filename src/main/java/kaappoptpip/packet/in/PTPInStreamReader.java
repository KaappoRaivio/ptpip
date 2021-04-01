package kaappoptpip.packet.in;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

public class PTPInStreamReader extends Thread {
    private PTPInStream inputStream;
    private boolean run = true;

    public final Object lock = new Object();

    private ByteArrayOutputStream buffer;
    private volatile List<PTPInStream> packets;

    public PTPInStreamReader(InputStream inputStream) {
        this.inputStream = new PTPInStream(inputStream);
        buffer = new ByteArrayOutputStream();
        packets = new ArrayList<>();
    }

    @Override
    public void run() {
        boolean inPacket = false;

        int packetLength = 0;
        while (run) {
            if (!inPacket) {
                    packetLength = inputStream.readUInt32();
                    System.out.println("<== INCOMING PACKET, " + packetLength + " bytes long!");
                    inPacket = true;
            } else {
                System.out.println("Reading content now");
                ByteBuffer buffer = ByteBuffer.allocate(packetLength - 4);
                buffer.order(ByteOrder.LITTLE_ENDIAN);

                buffer.put(inputStream.readBytes(packetLength - 4));
                packets.add(new PTPInStream(new ByteArrayInputStream(buffer.array())));

                synchronized (lock) {
                    lock.notifyAll();
                }
                System.out.println("read packet!");

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
