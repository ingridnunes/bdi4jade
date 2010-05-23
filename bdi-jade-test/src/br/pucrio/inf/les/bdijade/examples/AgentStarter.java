package br.pucrio.inf.les.bdijade.examples;

import jade.BootProfileImpl;
import jade.core.Agent;
import jade.core.ProfileImpl;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.PlatformController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/*
 * Created on 29/01/2010 11:18:23 
 */

/**
 * @author ingrid
 * 
 */
public class AgentStarter {

	private static final Map<String, Agent> agents;

	static {
		agents = new HashMap<String, Agent>();
		agents.put(BDIAgent1.MY_NAME, new BDIAgent1());
//		agents.put(BDIAgent2.MY_NAME, new BDIAgent2());
	};

	public static void main(String[] args) {
		new AgentStarter();
	}

	private ProfileImpl bootProfile;
	private final Log log;

	private jade.core.Runtime runtime;

	public AgentStarter() {
		log = LogFactory.getLog(this.getClass());

		List<String> params = new ArrayList<String>();
		params.add("-gui");
		params.add("-detect-main:false");

		log.info("Plataform parameters: " + params);

		this.bootProfile = new BootProfileImpl(params.toArray(new String[0]));

		this.runtime = jade.core.Runtime.instance();
		PlatformController controller = runtime
				.createMainContainer(bootProfile);

		for (String agentName : agents.keySet()) {
			try {
				AgentController ac = ((AgentContainer) controller)
						.acceptNewAgent(agentName, agents.get(agentName));
				ac.start();
			} catch (Exception e) {
				log.error(e);
			}
		}
	}

}
