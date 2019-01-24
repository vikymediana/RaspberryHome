package rbhagent;

import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.RaspiPin;
import jade.core.Agent;
import jade.domain.DFService;
import jade.util.Logger;
import rbhbehaviour.CreateBehaviour;
import rbhbehaviour.RegisterInDFBehaviour;
import rbhmessage.Item;

public class LedAgent extends Agent {

    private Logger myLogger = Logger.getMyLogger(getClass().getName());

    @Override
    protected void setup() {

        Object[] args = getArguments();

        Pin pin = RaspiPin.getPinByName(args[0].toString());

        addBehaviour(new RegisterInDFBehaviour(this, "LED", "TILAB", myLogger));
        addBehaviour(new CreateBehaviour(Item.LED, args[0].toString()));

    }

    @Override
    protected void takeDown() {
        try {
            DFService.deregister(this);
        } catch (Exception e) {
        }
    }

}
