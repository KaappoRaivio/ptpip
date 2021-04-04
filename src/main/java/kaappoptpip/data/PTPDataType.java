package kaappoptpip.data;

import kaappoptpip.packet.in.PTPInStream;

public interface PTPDataType<T> {
    T parseData (PTPInStream inStream);
    Class<T> getTypeClass ();
}
