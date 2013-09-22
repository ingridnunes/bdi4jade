/*
 * Created on 19 Oct 2011 14:42:24 
 */
package br.ufrgs.inf.bdi4jade.examples.helloworld;

import br.ufrgs.inf.bdi4jade.core.BDIAgent;
import br.ufrgs.inf.bdi4jade.core.Capability;
import br.ufrgs.inf.bdi4jade.goal.Goal;
import br.ufrgs.inf.bdi4jade.util.plan.SimplePlan;

/**
 * @author ingridn
 * 
 */
class HelloWorldGoal implements Goal {

	private static final long serialVersionUID = -9039447524062487795L;

	private String name;

	public HelloWorldGoal(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}

public class HelloWorldAgent extends BDIAgent {

	private static final long serialVersionUID = 2712019445290687786L;

	protected void init() {
		addCapability(new Capability() {

			private static final long serialVersionUID = 5271907167861942067L;

			protected void setup() {
				getPlanLibrary().addPlan(
						new SimplePlan(HelloWorldGoal.class,
								HelloWorldPlan.class));
			}

		});

		addGoal(new HelloWorldGoal("reader"));
	}
}
