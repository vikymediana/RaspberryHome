package rbhbehaviour;

import com.pi4j.io.gpio.*;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.lang.acl.ACLMessage;
import jade.util.Logger;

import java.util.Date;
import java.util.logging.Level;

public class BooleanBehaviour extends CyclicBehaviour {

    GpioController gpioController = GpioFactory.getInstance();
    Pin pin;
    GpioPinDigitalOutput output;
    private boolean actualStatus;
    private long timeout;
    private long lastTrue;
    private Logger myLogger = Logger.getMyLogger(getClass().getName());

    public BooleanBehaviour(Pin pin, boolean actualStatus, long timeout) {
        this.pin = pin;
        this.actualStatus = actualStatus;
        this.timeout = timeout;
    }

    @Override
    public void onStart() {
        super.onStart();
        this.output = gpioController.provisionDigitalOutputPin(pin, "MyLED" + getAgent().getAID(), actualStatus ? PinState.HIGH : PinState.LOW);
        getAgent().addBehaviour(new TickerBehaviour(getAgent(), timeout) {
                                    protected void onTick() {
                                        if (actualStatus) {
                                            ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
                                            msg.setContent(String.valueOf(false));
                                            msg.addReceiver(getAgent().getAID());
                                            getAgent().send(msg);
                                        }
                                    }
                                }
        );
    }

    @Override
    public void action() {

        ACLMessage msg = getAgent().receive();
        if (msg != null) {
            myLogger.log(Level.INFO, " - " + getAgent().getLocalName() + " <- " + msg.getContent());
            if (msg.getPerformative() == ACLMessage.INFORM) {
                String content = msg.getContent();
                if ((content != null) && (content.indexOf("true") != -1)) {
                    this.output.high();
                    actualStatus = true;
                    this.lastTrue = new Date().getTime();
                } else if ((content != null) && (content.indexOf("false") != -1)) {
                    this.output.low();
                    actualStatus = false;
                }
            }
        } else {
            block();
        }
    }

    @Override
    public int onEnd() {
        gpioController.shutdown();
        gpioController.unprovisionPin(output);
        return super.onEnd();
    }
}
