/*
 * Created on 27/01/2010 22:28:26 
 */
package br.pucrio.inf.les.bdijade.util.plan;

import jade.core.behaviours.Behaviour;
import jade.lang.acl.MessageTemplate;
import br.pucrio.inf.les.bdijade.exception.PlanInstantiationException;
import br.pucrio.inf.les.bdijade.goal.Goal;
import br.pucrio.inf.les.bdijade.plan.Plan;
import br.pucrio.inf.les.bdijade.plan.PlanBody;

/**
 * This class represents a plan whose plan body is a class that can be
 * instantiated by invoking the @likn {@link Class#newInstance()} method. A
 * class that has the {@link Behaviour} class as superclass is provides and it
 * is instantiates in the {@link SimplePlan#createPlanBody()} method.
 * 
 * @author ingrid
 */
public class SimplePlan extends Plan {

	protected final Class<? extends Behaviour> planBodyClass;

	/**
	 * Creates a new Simple Plan. It is a plan whose body is the specified class
	 * and its id is the plan body class name. The class must also implement the
	 * {@link PlanBody} interface, otherwise an exception is going to be thrown
	 * during the instantiation process.
	 * 
	 * @param planBodyClass
	 *            the class of the plan body. It must have the Behavior as super
	 *            class and implement the {@link PlanBody} interface.
	 */
	public SimplePlan(Class<? extends Behaviour> planBodyClass) {
		super(planBodyClass.getSimpleName());
		this.planBodyClass = planBodyClass;
	}

	/**
	 * Creates a new Simple Plan. It is a plan whose body is the specified class
	 * and its id is the plan body class name. The class must also implement the
	 * {@link PlanBody} interface, otherwise an exception is going to be thrown
	 * during the instantiation process. It sets that this plan can achieve the
	 * specified goal class, but more goals can be specified by overriding the
	 * initGoals() method.
	 * 
	 * @param goalClass
	 *            the goal that this plan can achieve.
	 * @param planBodyClass
	 *            the class of the plan body. It must have the Behavior as super
	 *            class and implement the {@link PlanBody} interface.
	 */
	public SimplePlan(Class<? extends Goal> goalClass,
			Class<? extends Behaviour> planBodyClass) {
		super(planBodyClass.getSimpleName(), goalClass);
		this.planBodyClass = planBodyClass;
	}

	/**
	 * Creates a new Simple Plan. It is a plan whose body is the specified class
	 * and its id is the plan body class name. The class must also implement the
	 * {@link PlanBody} interface, otherwise an exception is going to be thrown
	 * during the instantiation process. It sets that this plan can achieve the
	 * specified goal class, but more goals can be specified by overriding the
	 * initGoals() method. The message templates is initialized with the
	 * provided template.
	 * 
	 * @param goalClass
	 *            the goal that this plan can achieve.
	 * @param messageTemplate
	 *            the template of messages that this plan can process.
	 * @param planBodyClass
	 *            the class of the plan body. It must have the Behavior as super
	 *            class and implement the {@link PlanBody} interface.
	 */
	public SimplePlan(Class<? extends Goal> goalClass,
			MessageTemplate messageTemplate,
			Class<? extends Behaviour> planBodyClass) {
		super(planBodyClass.getSimpleName(), goalClass, messageTemplate);
		this.planBodyClass = planBodyClass;
	}

	/**
	 * Creates a new Simple Plan. It is a plan whose body is the specified class
	 * and its id is the plan body class name. The class must also implement the
	 * {@link PlanBody} interface, otherwise an exception is going to be thrown
	 * during the instantiation process. The message templates is initialized
	 * with the provided template.
	 * 
	 * @param messageTemplate
	 *            the template of messages that this plan can process.
	 * @param planBodyClass
	 *            the class of the plan body. It must have the Behavior as super
	 *            class and implement the {@link PlanBody} interface.
	 */
	public SimplePlan(MessageTemplate messageTemplate,
			Class<? extends Behaviour> planBodyClass) {
		super(planBodyClass.getSimpleName(), messageTemplate);
		this.planBodyClass = planBodyClass;
	}

	/**
	 * Creates a new Simple Plan. It is a plan that has the provided id and
	 * whose body is the specified class. The class must also implement the
	 * {@link PlanBody} interface, otherwise an exception is going to be thrown
	 * during the instantiation process.
	 * 
	 * @param id
	 *            the id of this plan.
	 * @param planBodyClass
	 *            the class of the plan body. It must have the Behavior as super
	 *            class and implement the {@link PlanBody} interface.
	 */
	public SimplePlan(String id, Class<? extends Behaviour> planBodyClass) {
		super(id);
		this.planBodyClass = planBodyClass;
	}

	/**
	 * Creates a new Simple Plan. It is a plan that has the provided id and
	 * whose body is the specified class. The class must also implement the
	 * {@link PlanBody} interface, otherwise an exception is going to be thrown
	 * during the instantiation process. It sets that this plan can achieve the
	 * specified goal class, but more goals can be specified by overriding the
	 * initGoals() method.
	 * 
	 * @param id
	 *            the id of this plan.
	 * @param goalClass
	 *            the goal that this plan can achieve.
	 * @param planBodyClass
	 *            the class of the plan body. It must have the Behavior as super
	 *            class and implement the {@link PlanBody} interface.
	 */
	public SimplePlan(String id, Class<? extends Goal> goalClass,
			Class<? extends Behaviour> planBodyClass) {
		super(id, goalClass);
		this.planBodyClass = planBodyClass;
	}

	/**
	 * Creates a new Simple Plan. It is a plan that has the provided id and
	 * whose body is the specified class. The class must also implement the
	 * {@link PlanBody} interface, otherwise an exception is going to be thrown
	 * during the instantiation process. It sets that this plan can achieve the
	 * specified goal class, but more goals can be specified by overriding the
	 * initGoals() method. The message templates is initialized with the
	 * provided template.
	 * 
	 * @param id
	 *            the id of this plan.
	 * @param messageTemplate
	 *            the template of messages that this plan can process.
	 * @param goalClass
	 *            the goal that this plan can achieve.
	 * @param planBodyClass
	 *            the class of the plan body. It must have the Behavior as super
	 *            class and implement the {@link PlanBody} interface.
	 */
	public SimplePlan(String id, Class<? extends Goal> goalClass,
			MessageTemplate messageTemplate,
			Class<? extends Behaviour> planBodyClass) {
		super(id, goalClass, messageTemplate);
		this.planBodyClass = planBodyClass;
	}

	/**
	 * Creates a new Simple Plan. It is a plan that has the provided id and
	 * whose body is the specified class. The class must also implement the
	 * {@link PlanBody} interface, otherwise an exception is going to be thrown
	 * during the instantiation process. The message templates is initialized
	 * with the provided template.
	 * 
	 * @param id
	 *            the id of this plan.
	 * @param messageTemplate
	 *            the template of messages that this plan can process.
	 * @param planBodyClass
	 *            the class of the plan body. It must have the Behavior as super
	 *            class and implement the {@link PlanBody} interface.
	 */
	public SimplePlan(String id, MessageTemplate messageTemplate,
			Class<? extends Behaviour> planBodyClass) {
		super(id, messageTemplate);
		this.planBodyClass = planBodyClass;
	}

	/**
	 * Creates a new instance of the plan body. It invokes the method
	 * newInstance() from the plan body class.
	 * 
	 * @see br.pucrio.inf.les.bdijade.plan.Plan#createPlanBody()
	 */
	@Override
	public Behaviour createPlanBody() throws PlanInstantiationException {
		try {
			return this.planBodyClass.newInstance();
		} catch (Exception e) {
			throw new PlanInstantiationException(e);
		}
	}

	/**
	 * @return the planBodyClass
	 */
	public Class<? extends Behaviour> getPlanBodyClass() {
		return planBodyClass;
	}

}
