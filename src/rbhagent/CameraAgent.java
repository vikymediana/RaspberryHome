package rbhagent;

import jade.core.Agent;

import jade.util.Logger;
import rbhbehaviour.RegisterInDFBehaviour;
import rbhbehaviour.ShootBehaviour;

public class CameraAgent extends Agent {

    private Logger myLogger = Logger.getMyLogger(getClass().getName());

    protected void setup() {
        addBehaviour(new RegisterInDFBehaviour(this, "CAM", "TILAB", myLogger));
        addBehaviour(new ShootBehaviour());
    }


}
