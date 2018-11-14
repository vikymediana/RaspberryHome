package rbhagent;

import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.RaspiPin;
import jade.core.Agent;
import jade.util.Logger;
import rbhbehaviour.ConfigureBehaviour;
import rbhbehaviour.MovBehaviour;
import rbhbehaviour.RegisterInDFBehaviour;

import java.util.ArrayList;
import java.util.List;

public class MovAgent extends Agent {

    private Logger myLogger = Logger.getMyLogger(getClass().getName());
    private List<String> dstTypes = new ArrayList<>();

    protected void setup() {
        Object[] args = getArguments();
        Pin pin = RaspiPin.getPinByName(args[0].toString());
        dstTypes.add(args[1].toString());

        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        addBehaviour(new RegisterInDFBehaviour(this, "MOV", "TILAB", myLogger));
        //addBehaviour(new ConfigureBehaviour(this));
        addBehaviour(new MovBehaviour(pin, false, dstTypes, Long.valueOf(args[2].toString())));

    }

}
