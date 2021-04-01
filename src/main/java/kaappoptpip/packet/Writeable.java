package kaappoptpip.packet;

import java.io.OutputStream;

public interface Writeable {
    void writeTo(OutputStream stream);
}
