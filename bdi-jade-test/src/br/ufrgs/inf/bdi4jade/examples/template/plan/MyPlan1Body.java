/**
 * 
 */
package br.ufrgs.inf.bdi4jade.examples.template.plan;

import jade.core.behaviours.OneShotBehaviour;
import br.ufrgs.inf.bdi4jade.plan.PlanBody;
import br.ufrgs.inf.bdi4jade.plan.PlanInstance;
import br.ufrgs.inf.bdi4jade.plan.PlanInstance.EndState;

/**
 * @author ingrid
 * 
 */
public class MyPlan1Body extends OneShotBehaviour implements PlanBody {

	private static final long serialVersionUID = -3947024373151941681L;

	private EndState endState;

	@Override
	public void action() {
		// TODO Auto-generated method stub
		this.endState = EndState.SUCCESSFUL;
	}

	@Override
	public EndState getEndState() {
		// TODO Auto-generated method stub
		return endState;
	}

	@Override
	public void init(PlanInstance planInstance) {
		// TODO Auto-generated method stub
		this.endState = null;
	}

}
