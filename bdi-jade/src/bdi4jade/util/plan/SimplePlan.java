//----------------------------------------------------------------------------
// Copyright (C) 2011  Ingrid Nunes
// 
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
// 
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
// 
// You should have received a copy of the GNU Lesser General Public
// License along with this library; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
// 
// To contact the authors:
// http://inf.ufrgs.br/~ingridnunes/bdi4jade/
//
//----------------------------------------------------------------------------

package bdi4jade.util.plan;

import jade.core.behaviours.Behaviour;
import jade.lang.acl.MessageTemplate;
import bdi4jade.exception.PlanInstantiationException;
import bdi4jade.goal.Goal;
import bdi4jade.plan.AbstractPlan;
import bdi4jade.plan.PlanBodyInterface;

/**
 * This class represents a plan whose plan body is a class that can be
 * instantiated by invoking the @likn {@link Class#newInstance()} method. A
 * class that has the {@link Behaviour} class as superclass is provides and it
 * is instantiates in the {@link SimplePlan#createPlanBody()} method.
 * 
 * @author ingrid
 */
public class SimplePlan extends AbstractPlan {

	protected final Class<? extends PlanBodyInterface> planBodyClass;

	/**
	 * Creates a new Simple Plan. It is a plan whose body is the specified class
	 * and its id is the plan body class name. The class must also implement the
	 * {@link PlanBodyInterface} interface, otherwise an exception is going to
	 * be thrown during the instantiation process. It sets that this plan can
	 * achieve the specified goal class, but more goals can be specified by
	 * overriding the initGoals() method.
	 * 
	 * @param goalClass
	 *            the goal that this plan can achieve.
	 * @param planBodyClass
	 *            the class of the plan body. It must have the Behavior as super
	 *            class and implement the {@link PlanBodyInterface} interface.
	 */
	public SimplePlan(Class<? extends Goal> goalClass,
			Class<? extends PlanBodyInterface> planBodyClass) {
		super(planBodyClass.getSimpleName(), goalClass);
		this.planBodyClass = planBodyClass;
	}

	/**
	 * Creates a new Simple Plan. It is a plan whose body is the specified class
	 * and its id is the plan body class name. The class must also implement the
	 * {@link PlanBodyInterface} interface, otherwise an exception is going to
	 * be thrown during the instantiation process. It sets that this plan can
	 * achieve the specified goal class, but more goals can be specified by
	 * overriding the initGoals() method. The message templates is initialized
	 * with the provided template.
	 * 
	 * @param goalClass
	 *            the goal that this plan can achieve.
	 * @param messageTemplate
	 *            the template of messages that this plan can process.
	 * @param planBodyClass
	 *            the class of the plan body. It must have the Behavior as super
	 *            class and implement the {@link PlanBodyInterface} interface.
	 */
	public SimplePlan(Class<? extends Goal> goalClass,
			MessageTemplate messageTemplate,
			Class<? extends PlanBodyInterface> planBodyClass) {
		super(planBodyClass.getSimpleName(), goalClass, messageTemplate);
		this.planBodyClass = planBodyClass;
	}

	/**
	 * Creates a new Simple Plan. It is a plan whose body is the specified class
	 * and its id is the plan body class name. The class must also implement the
	 * {@link PlanBodyInterface} interface, otherwise an exception is going to
	 * be thrown during the instantiation process.
	 * 
	 * @param planBodyClass
	 *            the class of the plan body. It must have the Behavior as super
	 *            class and implement the {@link PlanBodyInterface} interface.
	 */
	public SimplePlan(Class<? extends PlanBodyInterface> planBodyClass) {
		super(planBodyClass.getSimpleName());
		this.planBodyClass = planBodyClass;
	}

	/**
	 * Creates a new Simple Plan. It is a plan whose body is the specified class
	 * and its id is the plan body class name. The class must also implement the
	 * {@link PlanBodyInterface} interface, otherwise an exception is going to
	 * be thrown during the instantiation process. The message templates is
	 * initialized with the provided template.
	 * 
	 * @param messageTemplate
	 *            the template of messages that this plan can process.
	 * @param planBodyClass
	 *            the class of the plan body. It must have the Behavior as super
	 *            class and implement the {@link PlanBodyInterface} interface.
	 */
	public SimplePlan(MessageTemplate messageTemplate,
			Class<? extends PlanBodyInterface> planBodyClass) {
		super(planBodyClass.getSimpleName(), messageTemplate);
		this.planBodyClass = planBodyClass;
	}

	/**
	 * Creates a new Simple Plan. It is a plan that has the provided id and
	 * whose body is the specified class. The class must also implement the
	 * {@link PlanBodyInterface} interface, otherwise an exception is going to
	 * be thrown during the instantiation process. It sets that this plan can
	 * achieve the specified goal class, but more goals can be specified by
	 * overriding the initGoals() method.
	 * 
	 * @param id
	 *            the id of this plan.
	 * @param goalClass
	 *            the goal that this plan can achieve.
	 * @param planBodyClass
	 *            the class of the plan body. It must have the Behavior as super
	 *            class and implement the {@link PlanBodyInterface} interface.
	 */
	public SimplePlan(String id, Class<? extends Goal> goalClass,
			Class<? extends PlanBodyInterface> planBodyClass) {
		super(id, goalClass);
		this.planBodyClass = planBodyClass;
	}

	/**
	 * Creates a new Simple Plan. It is a plan that has the provided id and
	 * whose body is the specified class. The class must also implement the
	 * {@link PlanBodyInterface} interface, otherwise an exception is going to
	 * be thrown during the instantiation process. It sets that this plan can
	 * achieve the specified goal class, but more goals can be specified by
	 * overriding the initGoals() method. The message templates is initialized
	 * with the provided template.
	 * 
	 * @param id
	 *            the id of this plan.
	 * @param messageTemplate
	 *            the template of messages that this plan can process.
	 * @param goalClass
	 *            the goal that this plan can achieve.
	 * @param planBodyClass
	 *            the class of the plan body. It must have the Behavior as super
	 *            class and implement the {@link PlanBodyInterface} interface.
	 */
	public SimplePlan(String id, Class<? extends Goal> goalClass,
			MessageTemplate messageTemplate,
			Class<? extends PlanBodyInterface> planBodyClass) {
		super(id, goalClass, messageTemplate);
		this.planBodyClass = planBodyClass;
	}

	/**
	 * Creates a new Simple Plan. It is a plan that has the provided id and
	 * whose body is the specified class. The class must also implement the
	 * {@link PlanBodyInterface} interface, otherwise an exception is going to
	 * be thrown during the instantiation process.
	 * 
	 * @param id
	 *            the id of this plan.
	 * @param planBodyClass
	 *            the class of the plan body. It must have the Behavior as super
	 *            class and implement the {@link PlanBodyInterface} interface.
	 */
	public SimplePlan(String id,
			Class<? extends PlanBodyInterface> planBodyClass) {
		super(id);
		this.planBodyClass = planBodyClass;
	}

	/**
	 * Creates a new Simple Plan. It is a plan that has the provided id and
	 * whose body is the specified class. The class must also implement the
	 * {@link PlanBodyInterface} interface, otherwise an exception is going to
	 * be thrown during the instantiation process. The message templates is
	 * initialized with the provided template.
	 * 
	 * @param id
	 *            the id of this plan.
	 * @param messageTemplate
	 *            the template of messages that this plan can process.
	 * @param planBodyClass
	 *            the class of the plan body. It must have the Behavior as super
	 *            class and implement the {@link PlanBodyInterface} interface.
	 */
	public SimplePlan(String id, MessageTemplate messageTemplate,
			Class<? extends PlanBodyInterface> planBodyClass) {
		super(id, messageTemplate);
		this.planBodyClass = planBodyClass;
	}

	/**
	 * Creates a new instance of the plan body. It invokes the method
	 * newInstance() from the plan body class.
	 * 
	 * @see bdi4jade.plan.Plan#createPlanBody()
	 */
	@Override
	public PlanBodyInterface createPlanBody() throws PlanInstantiationException {
		try {
			return this.planBodyClass.newInstance();
		} catch (Exception e) {
			throw new PlanInstantiationException(e);
		}
	}

	/**
	 * @return the planBodyClass
	 */
	public Class<? extends PlanBodyInterface> getPlanBodyClass() {
		return planBodyClass;
	}

}
