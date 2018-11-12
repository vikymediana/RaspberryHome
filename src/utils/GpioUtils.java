package utils;

import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.RaspiPin;

import java.util.HashMap;
import java.util.Map;

public class GpioUtils {

    public static Pin getPinByName(String pinName) {
        Map<String, Pin> pins = new HashMap<>();
        pins.put("GPIO 0", RaspiPin.GPIO_00);
        pins.put("GPIO 1", RaspiPin.GPIO_01);
        pins.put("GPIO 2", RaspiPin.GPIO_02);
        return pins.get(pinName);
    }

}
