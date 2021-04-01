package kaappoptpip.packet;

import java.io.OutputStream;

public interface PTPOutStream extends Writeable {
    void writeUInt64(long uploadSize);
    void writeUInt32 (int data);
    void writeUInt16 (int data);
    void writeUInt8 (int data);

    void writeWChar(String data);
    default void writeWChar(char data) {
        writeWChar(String.valueOf(data));
    }

    void writeBytes(byte[] bytes);
    void writeShorts(short[] shorts);

    int getSize ();

}
