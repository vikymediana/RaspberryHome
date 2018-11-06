package rbhbehaviour;

import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.util.Logger;

import java.util.logging.Level;

public class RegisterInDFBehaviour extends OneShotBehaviour {

    private String type;
    private String owner;
    private Logger logger = Logger.getMyLogger(getClass().getName());

    public RegisterInDFBehaviour(Agent a, String type, String owner, Logger logger) {
        super(a);
        setType(type);
        setOwner(owner);
        setLogger(logger);
    }

    @Override
    public void action() {
        getLogger().log(Level.INFO,"RegisterInDFBehaviour - start");
        ServiceDescription sd = new ServiceDescription();
        sd.setType(getType());
        sd.setName(getAgent().getName());
        sd.setOwnership(getOwner());
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAgent().getAID());
        dfd.addServices(sd);
        try {
            DFAgentDescription[] dfds = DFService.search(getAgent(), dfd);
            if (dfds.length > 0) {
                DFService.deregister(getAgent(), dfd);
            }
            DFService.register(getAgent(), dfd);
            getLogger().log(Level.INFO,getAgent().getLocalName() + " is ready.");
        } catch (Exception ex) {
            getLogger().log(Level.SEVERE,"Failed registering with DF! Shutting down...");
            ex.printStackTrace();
            getAgent().doDelete();
        }
        getLogger().log(Level.INFO,"RegisterInDFBehaviour - end");
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Logger getLogger() {
        return logger;
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }

}
