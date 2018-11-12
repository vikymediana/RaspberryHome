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

    public GpioController getGpioController() {
        return gpioController;
    }

    public void setGpioController(GpioController gpioController) {
        this.gpioController = gpioController;
    }

    public Pin getGpioPin() {
        return gpioPin;
    }

    public void setGpioPin(Pin gpioPin) {
        this.gpioPin = gpioPin;
    }

    public GpioPinDigitalOutput getOutput() {
        return output;
    }

    public void setOutput(GpioPinDigitalOutput output) {
        this.output = output;
    }

    public Logger getMyLogger() {
        return myLogger;
    }

    public void setMyLogger(Logger myLogger) {
        this.myLogger = myLogger;
    }
}
