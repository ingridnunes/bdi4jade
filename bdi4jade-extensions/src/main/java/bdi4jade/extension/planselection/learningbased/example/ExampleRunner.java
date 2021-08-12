package bdi4jade.extension.planselection.learningbased.example;

import jade.BootProfileImpl;
import jade.core.ProfileImpl;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.PlatformController;

import java.util.ArrayList;
import java.util.List;

import bdi4jade.event.GoalEvent;
import bdi4jade.event.GoalListener;

public class ExampleRunner implements GoalListener {

	public static final int ITERATIONS = 500;

	public static void main(String[] args) {
		ExampleRunner runner = new ExampleRunner();
		runner.run();
	}

	private ProfileImpl bootProfile;
	private int iteration;
	private jade.core.Runtime runtime;

	private final ExampleAgent exampleAgent;

	public ExampleRunner() {
		List<String> params = new ArrayList<String>();
		params.add("-gui");
		params.add("-detect-main:false");

		this.bootProfile = new BootProfileImpl(params.toArray(new String[0]));
		this.runtime = jade.core.Runtime.instance();
		PlatformController controller = runtime
				.createMainContainer(bootProfile);

		this.exampleAgent = new ExampleAgent();

		try {
			AgentController ac = ((AgentContainer) controller).acceptNewAgent(
					exampleAgent.getClass().getSimpleName(),
					exampleAgent);
			ac.start();
		} catch (Exception e) {
			System.out.println(e);
		}

		this.iteration = 0;
	}

	@Override
	public void goalPerformed(GoalEvent event) {
		if (event.getStatus().isFinished()
				&& event.getGoal() instanceof ExampleGoal) {
			if (iteration < ITERATIONS) {
				run();
			} else {
				System.out.println("Iterations finished!!");
			}
		}
	}
	
	public void run() {
		exampleAgent.addGoal(new ExampleGoal(), this);
		iteration++;
	}
}
