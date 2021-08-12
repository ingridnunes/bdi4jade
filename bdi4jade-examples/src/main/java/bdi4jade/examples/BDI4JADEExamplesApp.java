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

package bdi4jade.examples;

import jade.BootProfileImpl;
import jade.core.Agent;
import jade.core.ProfileImpl;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.PlatformController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.PropertyConfigurator;

/**
 * This class is responsible for initiating the BDI4JADE app. It bootstraps
 * JADE, runs agents of a {@link BDI4JADEExamplesPanel}, and makes a GUI visible
 * when it is created and has its {@link #createAndShowGUI()} executed.
 * 
 * @author Ingrid Nunes
 */
public class BDI4JADEExamplesApp {

	public static void main(String[] args) {
		PropertyConfigurator.configure(BDI4JADEExamplesApp.class
				.getResource("log4j.properties"));

		new BDI4JADEExamplesApp().createAndShowGUI();
	}

	private final BDI4JADEExamplesPanel agentTestPanel;
	private ProfileImpl bootProfile;
	private final Log log;
	private jade.core.Runtime runtime;

	public BDI4JADEExamplesApp() {
		this.log = LogFactory.getLog(this.getClass());
		this.agentTestPanel = new BDI4JADEExamplesPanel();

		List<String> params = new ArrayList<String>();
		params.add("-gui");
		params.add("-detect-main:false");

		log.info("Plataform parameters: " + params);

		this.bootProfile = new BootProfileImpl(params.toArray(new String[0]));

		this.runtime = jade.core.Runtime.instance();
		PlatformController controller = runtime
				.createMainContainer(bootProfile);

		Map<String, Agent> agents = agentTestPanel.getAgents();
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

	/**
	 * Creates and shows a GUI whose content pane is an
	 * {@link BDI4JADEExamplesPanel}.
	 */
	public void createAndShowGUI() {
		final JFrame frame = new JFrame();
		frame.setTitle("BDI4JADE Examples");
		frame.setContentPane(agentTestPanel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				frame.setVisible(true);
			}
		});
	}

}
