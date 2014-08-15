package bdi4jade.examples;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import bdi4jade.core.AbstractBDIAgent;
import bdi4jade.core.SingleCapabilityAgent;
import bdi4jade.event.GoalEvent;
import bdi4jade.event.GoalListener;
import bdi4jade.examples.helloworld.HelloWorldAgent;
import bdi4jade.examples.helloworld.HelloWorldAnnotatedCapability;

/**
 * This class is a panel that is used as content pane of the application with
 * examples of BDI4JADE. It has a set of {@link BDI4JADEExamplesAction}, and
 * creates a button to perform each of them.
 * 
 * @author Ingrid Nunes
 */
public class BDI4JADEExamplesPanel extends JPanel {

	private class HelloWorldAction extends BDI4JADEExamplesAction {

		private static final long serialVersionUID = 2100583035268414082L;

		private final HelloWorldAgent helloWorldAgent;

		public HelloWorldAction() {
			super.putValue(Action.NAME, "Hello World Agent");
			this.helloWorldAgent = new HelloWorldAgent();
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			String name = JOptionPane.showInputDialog(
					BDI4JADEExamplesPanel.this, "Please, inform your name:");
			helloWorldAgent.addGoal(new HelloWorldAgent.HelloWorldGoal(name));
		}

		@Override
		public Set<AbstractBDIAgent> getAgents() {
			Set<AbstractBDIAgent> agents = new HashSet<>();
			agents.add(helloWorldAgent);
			return agents;
		}
	}

	private class HelloWorldAnnotatedAction extends BDI4JADEExamplesAction
			implements GoalListener {

		private static final long serialVersionUID = 2100583035268414082L;

		private final SingleCapabilityAgent helloWorldAnnotatedAgent;

		public HelloWorldAnnotatedAction() {
			super.putValue(Action.NAME, "Hello World Annotated Capability");
			this.helloWorldAnnotatedAgent = new SingleCapabilityAgent(
					new HelloWorldAnnotatedCapability());
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			String name = JOptionPane.showInputDialog(
					BDI4JADEExamplesPanel.this, "Please, inform your name:");
			helloWorldAnnotatedAgent.addGoal(
					new HelloWorldAnnotatedCapability.HelloWorldGoal(name),
					this);
		}

		@Override
		public Set<AbstractBDIAgent> getAgents() {
			Set<AbstractBDIAgent> agents = new HashSet<>();
			agents.add(helloWorldAnnotatedAgent);
			return agents;
		}

		@Override
		public void goalPerformed(GoalEvent event) {
			if (event.getStatus().isFinished()) {
				System.out.println("Hello World Goal Finished! Time: "
						+ event.getGoal());
			}
		}

	}

	private static final long serialVersionUID = -1080267169700651610L;

	private final BDI4JADEExamplesAction[] actions;

	// agents.put(BDIAgent1.MY_NAME, new BDIAgent1());
	// agents.put(BDIAgent2.MY_NAME, new BDIAgent2());
	// agents.put(MyAgent.class.getSimpleName(), new MyAgent());
	// agents.put(NestedCapabilitiesAgent.class.getSimpleName(),
	// new NestedCapabilitiesAgent());

	public BDI4JADEExamplesPanel() {
		this.actions = new BDI4JADEExamplesAction[] { new HelloWorldAction(),
				new HelloWorldAnnotatedAction() };
		this.setLayout(new GridLayout(actions.length, 1));
		for (BDI4JADEExamplesAction action : actions) {
			this.add(new JButton(action));
		}
	}

	public Map<String, AbstractBDIAgent> getAgents() {
		Map<String, AbstractBDIAgent> agents = new HashMap<>();
		for (BDI4JADEExamplesAction action : actions) {
			agents.putAll(action.getAgentMap());
		}
		return agents;
	}

}
