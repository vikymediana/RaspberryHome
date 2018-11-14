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
    Pin pin;
    GpioPinDigitalInput input;
    private boolean actualStatus;
    private final List<String> dstTypes;
    private Logger myLogger = Logger.getMyLogger(getClass().getName());

    public MovBehaviour(Pin pin, boolean initValue, List<String> dstTypes) {
        this.pin = pin;
        this.dstTypes = dstTypes;
        this.actualStatus = initValue;
    }

    @Override
    public void onStart() {
        super.onStart();
        this.input = gpioController.provisionDigitalInputPin(pin, actualStatus ? PinPullResistance.PULL_UP : PinPullResistance.PULL_DOWN);
    }

    @Override
    public void action() {

        input.addListener(new GpioPinListenerDigital() {
            @Override
            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
                try {
                    if (event.getState().isHigh()) {
                        List<DFAgentDescription> dstAgents = DFUtils.findAgentsByServiceTypes(getAgent(), dstTypes);
                        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
                        msg.setContent(String.valueOf(true));
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
