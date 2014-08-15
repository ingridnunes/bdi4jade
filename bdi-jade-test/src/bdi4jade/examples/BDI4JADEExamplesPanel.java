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
import bdi4jade.examples.compositegoal.CompositeGoalCapability;
import bdi4jade.examples.compositegoal.CompositeGoalCapability.MyGoal1;
import bdi4jade.examples.compositegoal.CompositeGoalCapability.MyGoal2;
import bdi4jade.examples.compositegoal.CompositeGoalCapability.MyGoal3;
import bdi4jade.examples.helloworld.HelloWorldAgent;
import bdi4jade.examples.helloworld.HelloWorldAnnotatedCapability;
import bdi4jade.examples.ping.PingPongCapability;
import bdi4jade.goal.CompositeGoal;
import bdi4jade.goal.Goal;
import bdi4jade.goal.ParallelGoal;
import bdi4jade.goal.SequentialGoal;

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

	private class PingPongAction extends BDI4JADEExamplesAction {

		public static final String AGENT_1 = "Alice";
		public static final String AGENT_2 = "Bob";
		private static final long serialVersionUID = 2100583035268414082L;

		private final AbstractBDIAgent agent1;
		private final AbstractBDIAgent agent2;

		public PingPongAction() {
			super.putValue(Action.NAME, "Ping Pong Agents");
			this.agent1 = new SingleCapabilityAgent(new PingPongCapability(
					AGENT_2, 2));
			this.agent2 = new SingleCapabilityAgent(new PingPongCapability(
					AGENT_1, 1));
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			this.agent1.addGoal(new PingPongCapability.PingGoal());
			this.agent2.addGoal(new PingPongCapability.PingGoal());
		}

		public Map<String, AbstractBDIAgent> getAgentMap() {
			Map<String, AbstractBDIAgent> agentMap = new HashMap<>();
			agentMap.put(AGENT_1, agent1);
			agentMap.put(AGENT_2, agent2);
			return agentMap;
		}

		@Override
		public Set<AbstractBDIAgent> getAgents() {
			return new HashSet<>(getAgentMap().values());
		}
	}

	private class CompositeGoalAction extends BDI4JADEExamplesAction implements
			GoalListener {
		private static final long serialVersionUID = 2100583035268414082L;

		private final AbstractBDIAgent compositeGoalAgent;

		public CompositeGoalAction() {
			super.putValue(Action.NAME, "Composite Goal Agent");
			this.compositeGoalAgent = new SingleCapabilityAgent(
					new CompositeGoalCapability());
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			int answer = JOptionPane.showConfirmDialog(
					BDI4JADEExamplesPanel.this,
					"Should goals be achieved sequentially?",
					"Sequential vs. Parallel Goals", JOptionPane.YES_NO_OPTION);
			Goal[] goals = { new MyGoal1("Hello World!"), new MyGoal2(),
					new MyGoal3() };
			CompositeGoal compositeGoal = null;
			if (JOptionPane.YES_OPTION == answer) {
				compositeGoal = new SequentialGoal(goals);
			} else {
				compositeGoal = new ParallelGoal(goals);
			}
			compositeGoalAgent.addGoal(compositeGoal, this);
		}

		@Override
		public void goalPerformed(GoalEvent event) {
			if (event.getStatus().isFinished()
					&& event.getGoal() instanceof CompositeGoal) {
				log.info("Goal finished!");
				log.info(event.getGoal() + " Status: " + event.getStatus());
			}
		}

		@Override
		public Set<AbstractBDIAgent> getAgents() {
			Set<AbstractBDIAgent> agents = new HashSet<>();
			agents.add(compositeGoalAgent);
			return agents;
		}
	}

	private static final long serialVersionUID = -1080267169700651610L;

	private final BDI4JADEExamplesAction[] actions;

	// agents.put(MyAgent.class.getSimpleName(), new MyAgent());
	// agents.put(NestedCapabilitiesAgent.class.getSimpleName(),
	// new NestedCapabilitiesAgent());
	// this.addCapability(new PlanFailedCapability());
	// this.addCapability(new SubgoalCapability());
	// this.addCapability(new CompositeGoalCapability(true));
	// this.addCapability(new CompositeGoalCapability(false));

	public BDI4JADEExamplesPanel() {
		this.actions = new BDI4JADEExamplesAction[] { new HelloWorldAction(),
				new HelloWorldAnnotatedAction(), new PingPongAction(),
				new CompositeGoalAction() };
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
