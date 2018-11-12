package rbhagent;

import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.RaspiPin;
import jade.core.Agent;
import jade.domain.DFService;
import jade.util.Logger;
import rbhbehaviour.BooleanBehaviour;
import rbhbehaviour.RegisterInDFBehaviour;

public class LedAgent extends Agent {

    private Logger myLogger = Logger.getMyLogger(getClass().getName());

    @Override
    protected void setup() {

        Object[] args = getArguments();
        Class raspiPin = RaspiPin.class;
        Pin pin = RaspiPin.GPIO_00;

        System.out.println(args[0].toString());
        System.out.println(pin == null ? "es nulo" : pin.toString());

        addBehaviour(new RegisterInDFBehaviour(this, "LED", "TILAB", myLogger));
        addBehaviour(new BooleanBehaviour(pin, false));

    }

    @Override
    protected void takeDown() {
        try {
            DFService.deregister(this);
        } catch (Exception e) {
        }
    }

}
