/*
 * Created on 19 Oct 2011 15:00:34 
 */
package br.ufrgs.inf.bdi4jade.examples.helloworld;

import jade.core.behaviours.OneShotBehaviour;
import br.ufrgs.inf.bdi4jade.plan.PlanBody;
import br.ufrgs.inf.bdi4jade.plan.PlanInstance;
import br.ufrgs.inf.bdi4jade.plan.PlanInstance.EndState;

/**
 * @author ingridn
 * 
 */
public class HelloWorldPlan extends OneShotBehaviour implements PlanBody {

	private static final long serialVersionUID = -9039447524062487795L;

	private String name;
	private EndState endState;

	public void action() {
		System.out.println("Hello, " + name + "!");
		this.endState = EndState.SUCCESSFUL;
	}

	public EndState getEndState() {
		return endState;
	}

	public void init(PlanInstance planInstance) {
		this.name = ((HelloWorldGoal) planInstance.getGoal()).getName();
	}
}
