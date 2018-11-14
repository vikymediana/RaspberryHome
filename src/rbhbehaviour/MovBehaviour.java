package rbhbehaviour;

import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import jade.core.behaviours.SimpleBehaviour;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.lang.acl.ACLMessage;
import jade.util.Logger;
import utils.DFUtils;

import java.util.Date;
import java.util.List;

public class MovBehaviour extends SimpleBehaviour {

    final GpioController gpioController = GpioFactory.getInstance();
    Pin pin;
    GpioPinDigitalInput input;
    private boolean actualStatus;
    private long timeout;
    private final List<String> dstTypes;
    private long lastNotification;
    private Logger myLogger = Logger.getMyLogger(getClass().getName());

    public MovBehaviour(Pin pin, boolean initValue, List<String> dstTypes, long timeout) {
        this.pin = pin;
        this.dstTypes = dstTypes;
        this.actualStatus = initValue;
        this.timeout = timeout;
    }

    @Override
    public void onStart() {
        super.onStart();
        this.input = gpioController.provisionDigitalInputPin(pin, actualStatus ? PinPullResistance.PULL_UP : PinPullResistance.PULL_DOWN);
        this.lastNotification = 0;
    }

    @Override
    public void action() {

        input.addListener(new GpioPinListenerDigital() {
            @Override
            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
                try {
                    if (event.getState().isHigh() && isNotificationTimeout()) {
                        System.out.println("SE MUEVEEEE");
                        List<DFAgentDescription> dstAgents = DFUtils.findAgentsByServiceTypes(getAgent(), dstTypes);
                        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
                        msg.setContent(String.valueOf(true));
                        for (DFAgentDescription dstAgent : dstAgents) {
                            msg.addReceiver(dstAgent.getName());
                        }
                        getAgent().send(msg);
                        lastNotification = new Date().getTime();
                    }
                } catch (Exception e) {
                    System.out.println("ERROR MOV");
                }
            }

        });
        block();

    }

    private boolean isNotificationTimeout() {
        return (new Date().getTime()) - lastNotification > timeout;
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
