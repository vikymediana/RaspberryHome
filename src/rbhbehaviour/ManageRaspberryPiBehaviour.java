package rbhbehaviour;

import com.pi4j.io.gpio.*;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.util.Logger;
import rbhmessage.RbhMessage;

import java.io.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class ManageRaspberryPiBehaviour extends CyclicBehaviour {

    GpioController gpioController = GpioFactory.getInstance();
    Pin pin;
    GpioPinDigitalOutput output;
    Map<Pin, Object> items = new HashMap<>();
    private Logger myLogger = Logger.getMyLogger(getClass().getName());


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void action() {

        ACLMessage msgReceived = getAgent().receive();
        if (msgReceived != null) {
            myLogger.log(Level.INFO, " - " + getAgent().getLocalName() + " <- " + msgReceived.getContent() + (new Date()).getTime()/1000);
            if (msgReceived.getPerformative() == ACLMessage.REQUEST) {
                byte[] content = msgReceived.getByteSequenceContent();
                RbhMessage rbhMessage = getMessageFromBytes(content);
                if (rbhMessage != null) {
                    if (rbhMessage.getMessageType() == RbhMessage.MessageType.CREATE) {
                        Pin pin = RaspiPin.getPinByName(rbhMessage.getItemId());
                        GpioPinDigitalOutput output = gpioController.provisionDigitalOutputPin(pin, rbhMessage.getItem().name() + getAgent().getAID(), PinState.LOW);
                        items.put(pin, output);
                        System.out.println("creado");
                        ACLMessage reply = msgReceived.createReply();
                        reply.setContent("OK");
                        getAgent().send(reply);
                    }
                }
            }
        } else {
            block();
        }
    }

    @Override
    public int onEnd() {
        gpioController.shutdown();
        gpioController.unprovisionPin(output);
        return super.onEnd();
    }

    private RbhMessage getMessageFromBytes(byte[] bytes) {
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        ObjectInput in = null;
        RbhMessage o = null;
        try {
            in = new ObjectInputStream(bis);
            o = (RbhMessage) in.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                // ignore close exception
            }
        }
        return o;
    }

    private byte[] getBytesFromMessage(RbhMessage rbhMessage) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out = null;
        byte[] bytes = null;
        try {
            out = new ObjectOutputStream(bos);
            out.writeObject(rbhMessage);
            out.flush();
            bytes = bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bos.close();
            } catch (IOException ex) {
                // ignore close exception
            }
        }
        return bytes;
    }
}
