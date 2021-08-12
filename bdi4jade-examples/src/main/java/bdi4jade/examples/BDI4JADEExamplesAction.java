package bdi4jade.examples;

import jade.core.Agent;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.Action;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import bdi4jade.core.SingleCapabilityAgent;

/**
 * This class is an abstract action used as a base for actions to test BDI4JADE
 * agents. Each action has an agent and a name, and subclasses should implement
 * the {@link #actionPerformed(java.awt.event.ActionEvent)} method to add goals
 * to agents, for example.
 * 
 * @author Ingrid Nunes
 */
public abstract class BDI4JADEExamplesAction extends AbstractAction {

	private static final long serialVersionUID = 6251170147656457707L;

	protected final Log log;

	protected BDI4JADEExamplesAction() {
		super();
		this.log = LogFactory.getLog(this.getClass());

		super.putValue(Action.ACCELERATOR_KEY, null);
		super.putValue(Action.ACTION_COMMAND_KEY, this.getClass()
				.getSimpleName());
		super.putValue(Action.LONG_DESCRIPTION, null);
		super.putValue(Action.MNEMONIC_KEY, null);
		super.putValue(Action.NAME, null);
		super.putValue(Action.SHORT_DESCRIPTION, null);
		super.putValue(Action.SMALL_ICON, null);
		super.setEnabled(true);
	}

	public Map<String, Agent> getAgentMap() {
		Map<String, Agent> agentMap = new HashMap<>();
		for (Agent agent : getAgents()) {
			if (SingleCapabilityAgent.class.equals(agent.getClass())) {
				SingleCapabilityAgent singleCapAgent = (SingleCapabilityAgent) agent;
				agentMap.put(singleCapAgent.getCapability().getId() + "Agent",
						agent);
			} else {
				agentMap.put(agent.getClass().getSimpleName(), agent);
			}
		}
		return agentMap;
	}

	public abstract Set<Agent> getAgents();

}
