package rbhagent;

import jade.core.Agent;
import jade.util.Logger;
import rbhbehaviour.ManageRaspberryPiBehaviour;
import rbhbehaviour.RegisterInDFBehaviour;

import java.util.ArrayList;
import java.util.List;

public class RaspberryPiAgent extends Agent {

    private Logger myLogger = Logger.getMyLogger(getClass().getName());
    private List<String> dstTypes = new ArrayList<>();

    protected void setup() {
        addBehaviour(new RegisterInDFBehaviour(this, "RaspberryPiManager", "TILAB", myLogger));
        addBehaviour(new ManageRaspberryPiBehaviour());
    }

}
