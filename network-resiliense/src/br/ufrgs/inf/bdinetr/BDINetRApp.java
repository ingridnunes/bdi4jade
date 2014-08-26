//----------------------------------------------------------------------------
// Copyright (C) 2011  Ingrid Nunes
// 
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
// 
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
// 
// You should have received a copy of the GNU Lesser General Public
// License along with this library; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
// 
// To contact the authors:
// http://inf.ufrgs.br/prosoft/bdi4jade/
//
//----------------------------------------------------------------------------
package br.ufrgs.inf.bdinetr;

import jade.BootProfileImpl;
import jade.core.ProfileImpl;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.PlatformController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.PropertyConfigurator;

import bdi4jade.core.AbstractBDIAgent;
import bdi4jade.examples.BDI4JADEExamplesPanel;
import br.ufrgs.inf.bdinetr.capability.LinkMonitorCapability;
import br.ufrgs.inf.bdinetr.capability.RateLimiterCapability;
import br.ufrgs.inf.bdinetr.domain.Device;
import br.ufrgs.inf.bdinetr.domain.Link;
import br.ufrgs.inf.bdinetr.domain.Network;

/**
 * @author Ingrid Nunes
 */
public class BDINetRApp {

	class LinkUsageUpdater extends TimerTask {
		@Override
		public void run() {
			Random random = new Random(System.currentTimeMillis());
			log.info("Updating link usage");
			for (Link link : NETWORK.getLinks()) {
				link.setUsedBandwidth(random.nextDouble() * link.getBandwidth());
			}
			log.info("Restarting agents");
			for (AbstractBDIAgent agent : AGENTS.values()) {
				agent.restart();
			}
		}
	}

	private static final Map<String, AbstractBDIAgent> AGENTS;

	private static final Network NETWORK;

	static {
		PropertyConfigurator.configure(BDINetRApp.class
				.getResource("log4j.properties"));

		NETWORK = new Network();
		Device firewall1 = new Device("Firewall 1");
		NETWORK.addDevice(firewall1);
		Device firewall2 = new Device("Firewall 2");
		NETWORK.addDevice(firewall2);
		Device firewall3 = new Device("Firewall 3");
		NETWORK.addDevice(firewall3);
		Device rateLimiter1 = new Device("Rate Limiter 1");
		NETWORK.addDevice(rateLimiter1);
		Device rateLimiter2 = new Device("Rate Limiter 2");
		NETWORK.addDevice(rateLimiter2);

		NETWORK.addLink(new Link("F1_RL1", 10.0, firewall1, rateLimiter1));
		NETWORK.addLink(new Link("F2_RL2", 8.0, firewall2, rateLimiter2));
		NETWORK.addLink(new Link("F3_RL1", 7.0, firewall3, rateLimiter1));
		NETWORK.addLink(new Link("F1_RL2", 7.0, firewall1, rateLimiter2));
		NETWORK.addLink(new Link("F2_RL1", 8.0, firewall2, rateLimiter1));
		NETWORK.addLink(new Link("F3_RL2", 10.0, firewall3, rateLimiter2));

		AGENTS = new HashMap<>();
		AGENTS.put(firewall1.getId(), new BDINetRAgent(firewall1,
				new LinkMonitorCapability()));
		AGENTS.put(firewall2.getId(), new BDINetRAgent(firewall2,
				new LinkMonitorCapability()));
		AGENTS.put(firewall3.getId(), new BDINetRAgent(firewall3,
				new LinkMonitorCapability()));
		AGENTS.put(rateLimiter1.getId(), new BDINetRAgent(rateLimiter1,
				new RateLimiterCapability()));
		AGENTS.put(rateLimiter2.getId(), new BDINetRAgent(rateLimiter2,
				new RateLimiterCapability()));

	}

	public static void main(String[] args) {
		new BDINetRApp().run();
	}

	private ProfileImpl bootProfile;
	private final Log log;
	private jade.core.Runtime runtime;

	private Timer timer;

	public BDINetRApp() {
		this.log = LogFactory.getLog(this.getClass());
		this.timer = new Timer();

		List<String> params = new ArrayList<String>();
		params.add("-gui");
		params.add("-detect-main:false");

		log.info("Plataform parameters: " + params);

		this.bootProfile = new BootProfileImpl(params.toArray(new String[0]));

		this.runtime = jade.core.Runtime.instance();
		PlatformController controller = runtime
				.createMainContainer(bootProfile);

		for (String agentName : AGENTS.keySet()) {
			try {
				AgentController ac = ((AgentContainer) controller)
						.acceptNewAgent(agentName, AGENTS.get(agentName));
				ac.start();
			} catch (Exception e) {
				log.error(e);
			}
		}
	}

	/**
	 * Creates and shows a GUI whose content pane is an
	 * {@link BDI4JADEExamplesPanel}.
	 */
	public void run() {
		int interval = 10 * 1000;
		this.timer.schedule(new LinkUsageUpdater(), interval, interval);
	}

}
