package rbhbehaviour;

import com.pi4j.io.gpio.*;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.util.Logger;

import java.util.logging.Level;

public class BooleanBehaviour extends CyclicBehaviour {

    GpioController gpioController = GpioFactory.getInstance();
    Pin gpioPin;
    GpioPinDigitalOutput output;
    private boolean initValue;
    private Logger myLogger = Logger.getMyLogger(getClass().getName());

    public BooleanBehaviour(Pin gpioPin, boolean initValue) {
        this.gpioPin = gpioPin;
        this.initValue = initValue;
    }

    @Override
    public void action() {
        output = gpioController.provisionDigitalOutputPin(gpioPin, "MyLED" + getAgent().getAID(), initValue ? PinState.HIGH : PinState.LOW);

        ACLMessage msg = getAgent().receive();
        if (msg!=null) {
            myLogger.log(Level.INFO, " - " + getAgent().getLocalName() + " <- " + msg.getContent() );
            if (msg.getPerformative() == ACLMessage.INFORM) {
                String content = msg.getContent();
                if ((content != null) && (content.indexOf("ON") != -1)) {
                    this.output.high();
                } else if ((content != null) && (content.indexOf("OFF") != -1)) {
                    this.output.low();
                }
            }
        } else {
            block();
        }
    }

}
