package utils;

import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.RaspiPin;

import java.util.HashMap;
import java.util.Map;

public class GpioUtils {

    public static Pin getPinByName(String pinName) {
        Map<String, Pin> pins = new HashMap<>();
        pins.put("0", RaspiPin.GPIO_00);
        pins.put("1", RaspiPin.GPIO_01);
        pins.put("2", RaspiPin.GPIO_02);
        System.out.println("pinName " + pinName);
        Pin pin = pins.get(pinName);
        System.out.println(pins.containsKey(pinName));
        return pin;
    }

}
