//----------------------------------------------------------------------------
// Copyright (C) 2013 Ingrid Nunes
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

package bdi4jade.examples.planselection;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;

import bdi4jade.event.GoalEvent;
import bdi4jade.event.GoalListener;
import bdi4jade.examples.BDI4JADEExamplesApp;
import bdi4jade.examples.planselection.TransportationAgent.TransportationGoal;
import jade.BootProfileImpl;
import jade.core.ProfileImpl;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.PlatformController;

/**
 * @author Ingrid Nunes
 */
public class ExperimentRunner implements GoalListener {

	public static final int ITERATIONS = 5000;

	public static void main(String[] args) throws Exception {
		LoggerContext context = (org.apache.logging.log4j.core.LoggerContext) 
				LogManager.getContext(false);
		context.setConfigLocation(BDI4JADEExamplesApp.class
				.getResource("log4j2.configurationFile").toURI());
		
		ExperimentRunner runner = new ExperimentRunner();
		runner.run();
	}

	private ProfileImpl bootProfile;
	private int iteration;
	private final Log log;
	private jade.core.Runtime runtime;

	private final TransportationAgent transportationAgent;

	public ExperimentRunner() {
		this.log = LogFactory.getLog(this.getClass());

		List<String> params = new ArrayList<String>();
		params.add("-gui");
		params.add("-detect-main:false");

		log.info("Plataform parameters: " + params);

		this.bootProfile = new BootProfileImpl(params.toArray(new String[0]));

		this.runtime = jade.core.Runtime.instance();
		PlatformController controller = runtime
				.createMainContainer(bootProfile);

		this.transportationAgent = new TransportationAgent();
		try {
			AgentController ac = ((AgentContainer) controller).acceptNewAgent(
					transportationAgent.getClass().getSimpleName(),
					transportationAgent);
			ac.start();
		} catch (Exception e) {
			log.error(e);
		}

		this.iteration = 0;
	}

	@Override
	public void goalPerformed(GoalEvent event) {
		if (event.getStatus().isFinished()
				&& event.getGoal() instanceof TransportationGoal) {
			if (iteration < ITERATIONS) {
				run();
			} else {
				log.info("Iterations finished!!");
				log.info(((GenericValueFunction<?>) transportationAgent
						.getCapability().getBeliefBase()
						.getBelief(TransportationAgent.SATISFACTION).getValue())
						.stats());
			}
		}
	}

	public void run() {
		transportationAgent.updatePreferences();
		transportationAgent.addGoal(new TransportationGoal(), this);
		iteration++;
	}

}
