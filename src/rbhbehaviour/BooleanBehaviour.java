package rbhbehaviour;

import com.pi4j.io.gpio.*;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.util.Logger;

import java.util.logging.Level;

public class BooleanBehaviour extends CyclicBehaviour {

    final GpioController gpioController = GpioFactory.getInstance();
    private Pin gpioPin;
    private GpioPinDigitalOutput output;
    private Logger myLogger = Logger.getMyLogger(getClass().getName());

    public BooleanBehaviour(Pin gpioPin, boolean initValue) {
        this.gpioPin = gpioPin;
        this.output = gpioController.provisionDigitalOutputPin(gpioPin, "MyLED" + getAgent().getAID(), initValue ? PinState.HIGH : PinState.LOW);
    }

    @Override
    public void action() {
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
