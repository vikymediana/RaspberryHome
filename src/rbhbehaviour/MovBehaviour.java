package rbhbehaviour;

import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import jade.core.behaviours.SimpleBehaviour;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.lang.acl.ACLMessage;
import jade.util.Logger;
import utils.DFUtils;

import java.util.List;

public class MovBehaviour extends SimpleBehaviour {

    final GpioController gpioController = GpioFactory.getInstance();
    Pin gpioPin;
    GpioPinDigitalInput input;
    private boolean actualStatus;
    private final List<String> dstTypes;
    private Logger myLogger = Logger.getMyLogger(getClass().getName());

    public MovBehaviour(Pin gpioPin, boolean initValue, List<String> dstTypes) {
        this.gpioPin = gpioPin;
        this.dstTypes = dstTypes;
        this.actualStatus = initValue;
    }

    @Override
    public void onStart() {
        super.onStart();
        this.input = gpioController.provisionDigitalInputPin(gpioPin, actualStatus ? PinPullResistance.PULL_UP : PinPullResistance.PULL_DOWN);
    }

    @Override
    public void action() {

        input.addListener(new GpioPinListenerDigital() {
            @Override
            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {

                try {
                    if (event.getState().isHigh() && !actualStatus || event.getState().isLow() && actualStatus) {
                        List<DFAgentDescription> dstAgents = DFUtils.findAgentsByServiceTypes(getAgent(), dstTypes);
                        actualStatus = !actualStatus;
                        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
                        msg.setContent(String.valueOf(actualStatus));
                        for (DFAgentDescription dstAgent : dstAgents) {
                            msg.addReceiver(dstAgent.getName());
                        }
                        getAgent().send(msg);
                    }
                } catch (Exception e) {
                    System.out.println("ERROR MOV");
                }
            }

        });
        block();

    }

    @Override
    public boolean done() {
        return false;
    }

    @Override
    public int onEnd() {
        gpioController.shutdown();
        gpioController.unprovisionPin(input);
        return super.onEnd();
    }

}
