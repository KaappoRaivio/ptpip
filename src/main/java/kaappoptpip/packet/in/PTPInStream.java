package kaappoptpip.packet.in;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.CharBuffer;

public class PTPInStream {
    private InputStream realInputStream;
    private ByteBuffer buffer;

    public PTPInStream (InputStream inputStream) {
        this.realInputStream = inputStream;
        buffer = ByteBuffer.allocate(4);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
    }

    public byte[] readAllBytes () {
        try {
            return realInputStream.readAllBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public int readUInt32 () {
        try {
            buffer.position(0);
            buffer.put(realInputStream.readNBytes(4));
            return buffer.getInt(0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public int readUInt16 () {
        try {
            buffer.position(0);
            buffer.put(realInputStream.readNBytes(2));
            return buffer.getShort(0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public int readUInt8 () {
        try {
            buffer.position(0);
            buffer.put(realInputStream.readNBytes(1));
            return buffer.get(0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String readWChar () {
        try {
            CharBuffer textBuffer = CharBuffer.allocate(1024);
            int length = 0;
            while (true) {
                int value = readUInt16();
                if (value == 0) {
                    break;
                } else {
                    length += 1;
                    textBuffer.put((char) value);
                }
            }

            return String.valueOf(textBuffer.array()).substring(0, length);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] readBytes (int amount) {
        try {
            return realInputStream.readNBytes(amount);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{0x4c, 0x00, 0x42, 0x00, 0x57, 0x00, 0x41, 0x00, 0x31, 0x00, 0x55, 0x00, 0x35, 0x00, 0x59, 0x00, 0x52, 0x00, 0x31, 0x00, 0x00, 0x00});
        PTPInStream stream = new PTPInStream(inputStream);
        System.out.println(stream.readWChar());
    }

    @Override
    public String toString() {
        try {
            return "PTPTInStream, length " + realInputStream.available();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
