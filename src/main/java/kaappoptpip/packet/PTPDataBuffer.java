package kaappoptpip.packet;

import kaappoptpip.misc.ByteUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.stream.IntStream;

public class PTPDataBuffer implements PTPOutStream {
    private ByteArrayOutputStream outputStream;

    public PTPDataBuffer() {
        this(0);
    }

    public PTPDataBuffer (int size) {
        this.outputStream = new ByteArrayOutputStream(size);
    }


    @Override
    public void writeUInt64(long data) {
        new PTPDataTypes.UInt64t(data).writeTo(outputStream);
    }

    @Override
    public void writeUInt32(int data) {
        new PTPDataTypes.UInt32t(data).writeTo(outputStream);
    }

    @Override
    public void writeUInt16(int data) {
        new PTPDataTypes.UInt16t(data).writeTo(outputStream);
    }

    @Override
    public void writeUInt8(int data) {
        new PTPDataTypes.UInt8t(data).writeTo(outputStream);
    }

    @Override
    public void writeWChar(String data) {
        new PTPDataTypes.WChar(data).writeTo(outputStream);
    }

    @Override
    public void writeBytes(byte[] bytes) {
        try {
            outputStream.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void writeShortsAsBytes(short[] shorts) {
        IntStream.range(0, shorts.length).map(i -> shorts[i]).forEach(this::writeUInt8);
    }

    @Override
    public int getSize() {
        return outputStream.size();
    }

    @Override
    public void writeTo(OutputStream stream) {
        try {
            outputStream.writeTo(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        System.out.println(ByteUtils.bytesToHex(ByteUtils.toLittleEndian(65534)));
    }
}
