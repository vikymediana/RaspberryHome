package utils;

import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DFUtils {

    /**
     * Find and returns agents by a service type.
     *
     * @param serviceType the service type
     * @return the dF agent description[]
     */
    public static DFAgentDescription[] findAgentsByServiceType(Agent agent, String serviceType) {

        DFAgentDescription[] dfAgentDescriptions = null;

        try {
            DFAgentDescription agentDescription = new DFAgentDescription();
            ServiceDescription serviceDescription = new ServiceDescription();
            serviceDescription.setType(serviceType);
            agentDescription.addServices(serviceDescription);

            dfAgentDescriptions = DFService.search(agent, agentDescription);

        } catch (FIPAException fe) {
            System.err.println("DFService: Error while requesting the DFService!");
            fe.printStackTrace();
        }
        return dfAgentDescriptions;
    }

    public static List<DFAgentDescription> findAgentsByServiceTypes(Agent agent, List<String> serviceTypes) {
        List<DFAgentDescription> dfAgentDescriptionList = new ArrayList<>();
        for (String serviceType : serviceTypes) {
            DFAgentDescription[] dfAgentDescriptions = findAgentsByServiceType(agent, serviceType);
            dfAgentDescriptionList.addAll(Arrays.asList(dfAgentDescriptions));
        }
        return dfAgentDescriptionList;
    }

}
