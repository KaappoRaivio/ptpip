package kaappoptpip.packet;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.stream.IntStream;

public class PTPDataType {
    public static class UInt32t implements Writeable {
        private final int value;

        public UInt32t(int value) {
            this.value = value;
        }

        @Override
        public void writeTo(OutputStream stream) {
            try {
                stream.write(toLittleEndian(value));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static class UInt16t implements Writeable {
        private final int value;

        public UInt16t(int value) {
            if (value > 65536) throw new RuntimeException(value + " is too big to fit in UInt8t!");
            this.value = value;
        }

        @Override
        public void writeTo(OutputStream stream) {
            var bytes = toLittleEndian(value);
            try {
                stream.write(Arrays.copyOfRange(bytes, 0, 2));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static class UInt8t implements Writeable {
        private final int value;

        public UInt8t(int value) {
            if (value > 256) throw new RuntimeException(value + " is too big to fit in UInt8t!");
            this.value = value;
        }

        @Override
        public void writeTo(OutputStream stream) {
            var bytes = toLittleEndian(value);
            try {
                stream.write(Arrays.copyOfRange(bytes, 0, 1));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static class WChar implements Writeable {
        private final String value;

        public WChar(String value) {
            this.value = value;
        }

        @Override
        public void writeTo(OutputStream stream) {

            char[] chars = value.toCharArray();
            IntStream.range(0, chars.length).mapToObj(i -> chars[i]).forEach(character -> new UInt16t(character).writeTo(stream));

            new UInt16t(0).writeTo(stream);
        }
    }

    private static byte[] toLittleEndian (int data) {
        byte byte1 = (byte) (data >> 0 & 0xff);
        byte byte2 = (byte) (data >> 8 & 0xff);
        byte byte3 = (byte) (data >> 16 & 0xff);
        byte byte4 = (byte) (data >> 24 & 0xff);

        return new byte[]{byte1, byte2, byte3, byte4};
    }
}
