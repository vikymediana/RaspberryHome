package rbhbehaviour;

import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.lang.acl.ACLMessage;
import rbhmessage.Item;
import rbhmessage.RbhMessage;
import utils.DFUtils;

import java.io.IOException;


public class CreateBehaviour extends CyclicBehaviour {

    Item item;
    String itemId;

    public CreateBehaviour(Item item, String itemId) {
        this.item = item;
        this.itemId = itemId;
    }

    @Override
    public void onStart() {
        super.onStart();
        RbhMessage rbhMessage = new RbhMessage();
        rbhMessage.setItem(item);
        rbhMessage.setItemId(itemId);
        rbhMessage.setMessageType(RbhMessage.MessageType.CREATE);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        DFAgentDescription[] dstAgents = DFUtils.findAgentsByServiceType(getAgent(), "RaspberryPiManager");
        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        try {
            msg.setContentObject(rbhMessage);
            for (DFAgentDescription dstAgent : dstAgents) {
                msg.addReceiver(dstAgent.getName());
            }
            getAgent().send(msg);

            System.out.println("PETICION ENVIADA ");
        } catch (IOException e) {
            e.printStackTrace();
            getAgent().doDelete();
        }
        ACLMessage msgReceived = getAgent().receive();
        if (msgReceived != null) {
            String content = msgReceived.getContent();
            System.out.println("RECIBO: " + content);
        } else {
            block();
        }

    }

    @Override
    public void action() {

    }

}
