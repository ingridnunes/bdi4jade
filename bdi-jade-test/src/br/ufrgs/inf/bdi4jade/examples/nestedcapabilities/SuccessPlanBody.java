package br.ufrgs.inf.bdi4jade.examples.nestedcapabilities;

import jade.core.behaviours.OneShotBehaviour;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import br.ufrgs.inf.bdi4jade.plan.PlanBody;
import br.ufrgs.inf.bdi4jade.plan.PlanInstance;
import br.ufrgs.inf.bdi4jade.plan.PlanInstance.EndState;

public class SuccessPlanBody extends OneShotBehaviour implements PlanBody {

	private static final long serialVersionUID = -9039447524062487795L;

	private EndState endState;
	private Log log;

	public void action() {
		log.info(this.getClass().getSimpleName() + " executed.");
		this.endState = EndState.SUCCESSFUL;
	}

	public EndState getEndState() {
		return endState;
	}

	public void init(PlanInstance planInstance) {
		this.log = LogFactory.getLog(this.getClass());
		this.endState = null;
	}
}
