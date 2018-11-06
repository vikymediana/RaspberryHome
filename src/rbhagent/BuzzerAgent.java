package rbhagent;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.util.Logger;

public class BuzzerAgent extends Agent {

    private Logger myLogger = Logger.getMyLogger(getClass().getName());
    private boolean myAlarm = false;

    protected void setup() {
        DFAgentDescription dfd = new DFAgentDescription();
        ServiceDescription sd = new ServiceDescription();
        sd.setType("buzzer");
        sd.setName(getName());
        sd.setOwnership("TILAB");
        dfd.setName(getAID());
        dfd.addServices(sd);
        try {
            DFService.register(this, dfd);
            ReceiveAlarmBehaviour receiveMessagesBehaviour = new ReceiveAlarmBehaviour(this);
            addBehaviour(receiveMessagesBehaviour);
        } catch (FIPAException e) {
            myLogger.log(Logger.SEVERE, "Agent " + getLocalName() + " - Cannot register with DF", e);
            doDelete();
        }
    }

    class ReceiveAlarmBehaviour extends CyclicBehaviour {

        public ReceiveAlarmBehaviour(Agent a) {
            super(a);
        }

        public void action() {
            ACLMessage msg = myAgent.receive();
            if (msg != null) {
                if (msg.getPerformative() == ACLMessage.INFORM) {
                    String content = msg.getContent();
                    if ((content != null) && (content.indexOf("ALARM") != -1)) {
                        myLogger.log(Logger.INFO, "PIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIiIIIIIIIII ");
                    }
                }
            } else {
                block();
            }
        }

    }
}
