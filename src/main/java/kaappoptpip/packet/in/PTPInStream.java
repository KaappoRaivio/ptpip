package kaappoptpip.packet.in;

import kaappoptpip.misc.ByteUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.List;

public class PTPInStream {
    private InputStream realInputStream;
    private int streamLength;
    private ByteBuffer buffer;

    public PTPInStream (InputStream inputStream) {
        this.realInputStream = inputStream;
        try {
            this.streamLength = inputStream.available();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        buffer = ByteBuffer.allocate(8);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
    }

    public byte[] readAllBytes () {
        try {
            return realInputStream.readAllBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public long readUInt64() {
        try {
            buffer.position(0);
            buffer.put(realInputStream.readNBytes(8));
            return buffer.getLong(0);
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

    public String readWChar() {
        return readWChar(false);
    }

    public String readWChar (boolean hasSizeHeader) {
        try {
            int expectedLength = 1024;
            if (hasSizeHeader) {
                expectedLength = readUInt8();
            }
            CharBuffer textBuffer = CharBuffer.allocate(expectedLength);
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
            ByteBuffer buffer = ByteBuffer.allocate(amount);

            int bytesRead = 0;
            int timesIterated = 0;
            while (bytesRead < amount) {
                byte[] bytes = realInputStream.readNBytes(amount);
                bytesRead += bytes.length;
                buffer.put(bytes);

                if (timesIterated++ > 10) {
                    throw new RuntimeException("Stream closed!");
                }
            }
            return buffer.array();
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

    public int getStreamLength() {
        return streamLength;
    }

    public int left () {
        try {
            return realInputStream.available();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Integer> readArrayOfUInt16() {
        List<Integer> list = new ArrayList<>();

        int arrayLength = readUInt32();

        for (int i = 0; i < arrayLength; i++) {
            list.add(readUInt16());
        }

        return list;
    }
}
