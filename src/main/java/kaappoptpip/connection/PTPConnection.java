package kaappoptpip.connection;

import kaappoptpip.misc.ByteUtils;
import kaappoptpip.packet.in.PTPInStreamReader;
import kaappoptpip.packet.in.PTPPacketIn;
import kaappoptpip.packet.out.PTPPacketOut;

import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.stream.Collectors;

public class PTPConnection {
    private Socket socket;
    private OutputStream connectionOut;
    private PTPInStreamReader connectionIn;

    public PTPConnection(String cameraAddress) throws IOException {
        socket = new Socket(cameraAddress, 15740);
        connectionOut = socket.getOutputStream();
        connectionIn = new PTPInStreamReader(socket.getInputStream());
        connectionIn.start();
    }

    public void writePacket (PTPPacketOut packet) {
        ByteArrayOutputStream proxy = new ByteArrayOutputStream();
        packet.writeTo(proxy);
        System.out.println("==> OUTGOING PACKET " + ByteUtils.bytesToHex(proxy.toByteArray()));
        try {
            connectionOut.write(proxy.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public List<PTPPacketIn> getPackets () {
        synchronized (connectionIn.lock) {
            try {
                connectionIn.lock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return connectionIn.getPackets().stream().map(packet -> PTPPacketIn.getPacket(packet.readUInt32(), packet)).collect(Collectors.toList());
        }
    }
}
