package bdi4jade.examples;

import jade.core.Agent;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import bdi4jade.core.MultipleCapabilityAgent;
import bdi4jade.core.SingleCapabilityAgent;
import bdi4jade.event.GoalEvent;
import bdi4jade.event.GoalListener;
import bdi4jade.examples.bdicycle.CompositeGoalCapability;
import bdi4jade.examples.bdicycle.CompositeGoalCapability.MyGoal1;
import bdi4jade.examples.bdicycle.CompositeGoalCapability.MyGoal2;
import bdi4jade.examples.bdicycle.CompositeGoalCapability.MyGoal3;
import bdi4jade.examples.bdicycle.PlanFailureCapability;
import bdi4jade.examples.bdicycle.PlanFailureCapability.MyGoal;
import bdi4jade.examples.bdicycle.SubgoalCapability;
import bdi4jade.examples.blocksworld.BlocksWorldCapability;
import bdi4jade.examples.blocksworld.BlocksWorldView;
import bdi4jade.examples.blocksworld.domain.On;
import bdi4jade.examples.blocksworld.domain.Thing;
import bdi4jade.examples.capabilities.Middle1Capability;
import bdi4jade.examples.capabilities.TopCapability;
import bdi4jade.examples.helloworld.HelloWorldAgent;
import bdi4jade.examples.helloworld.HelloWorldAnnotatedCapability;
import bdi4jade.examples.ping.PingPongCapability;
import bdi4jade.goal.BeliefValueGoal;
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

	private class BlocksWorldAction extends BDI4JADEExamplesAction implements
			GoalListener {

		private static final long serialVersionUID = 2100583035268414082L;

		private final SingleCapabilityAgent blocksWorldAgent;

		public BlocksWorldAction() {
			super.putValue(Action.NAME, "Blocks World");
			this.blocksWorldAgent = new SingleCapabilityAgent(
					new BlocksWorldCapability());
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			final JFrame frame = new JFrame();
			frame.setTitle((String) this.getValue(Action.NAME));
			frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			frame.setVisible(false);
			frame.setContentPane(new BlocksWorldView(blocksWorldAgent
					.getCapability().getBeliefBase()));

			frame.pack();
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					frame.setVisible(true);
				}
			});

			Set<On> target = new HashSet<>();
			target.add(new On(Thing.BLOCK_5, Thing.TABLE));
			target.add(new On(Thing.BLOCK_4, Thing.BLOCK_5));
			target.add(new On(Thing.BLOCK_3, Thing.BLOCK_4));
			target.add(new On(Thing.BLOCK_2, Thing.BLOCK_3));
			target.add(new On(Thing.BLOCK_1, Thing.BLOCK_2));

			blocksWorldAgent.addGoal(new BeliefValueGoal<String, Set<On>>(
					BlocksWorldCapability.BELIEF_ON, target), this);
		}

		@Override
		public Set<Agent> getAgents() {
			Set<Agent> agents = new HashSet<>();
			agents.add(blocksWorldAgent);
			return agents;
		}

		@Override
		public void goalPerformed(GoalEvent event) {
			log.info("Goal achieved!!");
		}
	}

	private class CompositeGoalAction extends BDI4JADEExamplesAction implements
			GoalListener {
		private static final long serialVersionUID = 2100583035268414082L;

		private final SingleCapabilityAgent compositeGoalAgent;

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
		public Set<Agent> getAgents() {
			Set<Agent> agents = new HashSet<>();
			agents.add(compositeGoalAgent);
			return agents;
		}

		@Override
		public void goalPerformed(GoalEvent event) {
			if (event.getStatus().isFinished()
					&& event.getGoal() instanceof CompositeGoal) {
				log.info("Goal finished!");
				log.info(event.getGoal() + " Status: " + event.getStatus());
			}
		}
	}

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
		public Set<Agent> getAgents() {
			Set<Agent> agents = new HashSet<>();
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
		public Set<Agent> getAgents() {
			Set<Agent> agents = new HashSet<>();
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

	private class MultiCapabilityAgentAction extends BDI4JADEExamplesAction {
		private static final long serialVersionUID = 2100583035268414082L;

		private final MultipleCapabilityAgent multiCapabilityAgent;

		public MultiCapabilityAgentAction() {
			super.putValue(Action.NAME, "Multi-capability Agent");
			this.multiCapabilityAgent = new MultipleCapabilityAgent(
					new TopCapability());
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			multiCapabilityAgent.addGoal(new Middle1Capability.TestGoal());
		}

		@Override
		public Set<Agent> getAgents() {
			Set<Agent> agents = new HashSet<>();
			agents.add(multiCapabilityAgent);
			return agents;
		}
	}

	private class PingPongAction extends BDI4JADEExamplesAction {

		public static final String AGENT_1 = "Alice";
		public static final String AGENT_2 = "Bob";
		private static final long serialVersionUID = 2100583035268414082L;

		private final SingleCapabilityAgent agent1;
		private final SingleCapabilityAgent agent2;

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

		public Map<String, Agent> getAgentMap() {
			Map<String, Agent> agentMap = new HashMap<>();
			agentMap.put(AGENT_1, agent1);
			agentMap.put(AGENT_2, agent2);
			return agentMap;
		}

		@Override
		public Set<Agent> getAgents() {
			return new HashSet<>(getAgentMap().values());
		}
	}

	private class PlanFailureAction extends BDI4JADEExamplesAction implements
			GoalListener {
		private static final int GOALS = 10;
		private static final long serialVersionUID = 2100583035268414082L;

		private int counter;
		private final SingleCapabilityAgent planFailureAgent;

		public PlanFailureAction() {
			super.putValue(Action.NAME, "Plan Failure Agent");
			this.planFailureAgent = new SingleCapabilityAgent(
					new PlanFailureCapability());
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			int goalsNum = GOALS;
			try {
				goalsNum = new Integer(JOptionPane.showInputDialog(
						BDI4JADEExamplesPanel.this,
						"Please, inform the number of goals to be dispatched:"));
			} catch (Exception exc) {
				log.warn(exc);
				log.warn("Using default number of goals: " + GOALS);
			}
			this.counter = 0;
			for (int i = 0; i < goalsNum; i++) {
				planFailureAgent.addGoal(new PlanFailureCapability.MyGoal(i),
						this);
			}
		}

		@Override
		public Set<Agent> getAgents() {
			Set<Agent> agents = new HashSet<>();
			agents.add(planFailureAgent);
			return agents;
		}

		@Override
		public void goalPerformed(GoalEvent event) {
			if (event.getStatus().isFinished()
					&& event.getGoal() instanceof MyGoal) {
				log.info(event.getGoal() + " Status: " + event.getStatus());
				counter++;
				if (counter >= GOALS) {
					log.info("Goal finished!!");
				}
			}
		}
	}

	private class SubgoalCapabilityAction extends BDI4JADEExamplesAction {
		private static final long serialVersionUID = 2100583035268414082L;

		private final SingleCapabilityAgent subgoalCapability;

		public SubgoalCapabilityAction() {
			super.putValue(Action.NAME, "Subgoal Goal Agent");
			this.subgoalCapability = new SingleCapabilityAgent(
					new SubgoalCapability());
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			subgoalCapability.addGoal(new SubgoalCapability.ParentGoal());
		}

		@Override
		public Set<Agent> getAgents() {
			Set<Agent> agents = new HashSet<>();
			agents.add(subgoalCapability);
			return agents;
		}
	}

	private static final long serialVersionUID = -1080267169700651610L;

	private final BDI4JADEExamplesAction[] actions;

	public BDI4JADEExamplesPanel() {
		this.actions = new BDI4JADEExamplesAction[] { new HelloWorldAction(),
				new HelloWorldAnnotatedAction(), new PingPongAction(),
				new CompositeGoalAction(), new PlanFailureAction(),
				new SubgoalCapabilityAction(),
				new MultiCapabilityAgentAction(), new BlocksWorldAction() };
		this.setLayout(new GridLayout(actions.length, 1));
		for (BDI4JADEExamplesAction action : actions) {
			this.add(new JButton(action));
		}
	}

	public Map<String, Agent> getAgents() {
		Map<String, Agent> agents = new HashMap<>();
		for (BDI4JADEExamplesAction action : actions) {
			agents.putAll(action.getAgentMap());
		}
		return agents;
	}

}
