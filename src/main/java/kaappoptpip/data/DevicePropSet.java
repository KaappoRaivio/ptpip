package kaappoptpip.data;

import java.util.Map;
import java.util.Optional;

public class DevicePropSet {
    private Map<Integer, DeviceProp> propsSet;

    public DevicePropSet(Map<Integer, DeviceProp> propsSet) {
        this.propsSet = propsSet;
    }

    public DeviceProp getProp (int propCode) {
        return Optional.ofNullable(propsSet.get(propCode)).orElseThrow();
    }
}
