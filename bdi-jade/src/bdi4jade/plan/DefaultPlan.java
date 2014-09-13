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
// http://inf.ufrgs.br/prosoft/bdi4jade/
//
//----------------------------------------------------------------------------

package bdi4jade.plan;

import jade.core.behaviours.Behaviour;
import jade.lang.acl.MessageTemplate;

import java.lang.reflect.Modifier;

import bdi4jade.core.BDIAgent;
import bdi4jade.core.Capability;
import bdi4jade.exception.PlanInstantiationException;
import bdi4jade.goal.Goal;
import bdi4jade.goal.GoalTemplate;
import bdi4jade.goal.GoalTemplateFactory;
import bdi4jade.plan.planbody.PlanBody;

/**
 * This class represents a plan whose plan body is a class that can be
 * instantiated by invoking the {@link Class#newInstance()} method. A class that
 * has the {@link Behaviour} class as superclass is provides and it is
 * instantiates in the {@link DefaultPlan#createPlanBody()} method.
 * 
 * @author Ingrid Nunes
 */
public class DefaultPlan extends AbstractPlan {

	private static String generateId(Class<? extends Goal> goalClass,
			GoalTemplate goalTemplate, MessageTemplate messageTemplate,
			Class<? extends PlanBody> planBodyClass) {
		StringBuffer sb = new StringBuffer("[");
		if (goalClass != null) {
			sb.append(" ").append(goalClass.getName());
		}
		if (goalTemplate != null) {
			sb.append(" ").append(goalTemplate);
		}
		if (messageTemplate != null) {
			sb.append(" ").append(messageTemplate);
		}
		sb.append(" ] :: ");
		sb.append(planBodyClass.getName());
		return sb.toString();
	}

	protected final Class<? extends PlanBody> planBodyClass;

	/**
	 * Creates a new simple plan, which is able to achieve goals of the given
	 * goal class, and its body should be instances of the provided plan body
	 * class. Its identifier is set of the class name of the plan body class.
	 * 
	 * @param goalClass
	 *            the class of goals that this plan is able to achieve.
	 * @param planBodyClass
	 *            the class of this plan body.
	 */
	public DefaultPlan(Class<? extends Goal> goalClass,
			Class<? extends PlanBody> planBodyClass) {
		super(generateId(goalClass, null, null, planBodyClass),
				GoalTemplateFactory.goalOfType(goalClass));
		this.planBodyClass = planBodyClass;
	}

	/**
	 * Creates a new simple plan. Its body should be instances of the provided
	 * plan body class. Its identifier is set of the class name of the plan body
	 * class.
	 * 
	 * @param planBodyClass
	 *            the class of this plan body.
	 */
	public DefaultPlan(Class<? extends PlanBody> planBodyClass) {
		super(generateId(null, null, null, planBodyClass));
		this.planBodyClass = planBodyClass;
	}

	/**
	 * Creates a new simple plan, which is able to achieve goals that match the
	 * provided template. It is a plan whose body is the specified class and its
	 * id is the plan body class name. It sets that this plan can achieve goals
	 * of the specified goal template, but more goal templates can be specified
	 * by overriding the {@link #initGoalTemplates()} method or invoking the
	 * {@link #addGoalTemplate(GoalTemplate)} method.
	 * 
	 * @param goalTemplate
	 *            the template of goals that this plan can achieve.
	 * @param planBodyClass
	 *            the class of this plan body.
	 */
	public DefaultPlan(GoalTemplate goalTemplate,
			Class<? extends PlanBody> planBodyClass) {
		super(generateId(null, goalTemplate, null, planBodyClass), goalTemplate);
		this.planBodyClass = planBodyClass;
	}

	/**
	 * Creates a new simple plan, which is able to achieve goals that match the
	 * provided goal template and process messages that match the provided
	 * message template. It is a plan whose body is the specified class and its
	 * id is the plan body class name. It sets that this plan can achieve goals
	 * of the specified goal template, but more goal templates can be specified
	 * by overriding the {@link #initGoalTemplates()} method or invoking the
	 * {@link #addGoalTemplate(GoalTemplate)} method, while more message
	 * templates can be specified by overriding the
	 * {@link #initMessageTemplates()} method or invoking the
	 * {@link #addMessageTemplate(MessageTemplate)} method.
	 * 
	 * 
	 * @param goalTemplate
	 *            the template of goals that this plan can achieve.
	 * @param messageTemplate
	 *            the template of messages that this plan can process.
	 * @param planBodyClass
	 *            the class of this plan body.
	 */
	public DefaultPlan(GoalTemplate goalTemplate,
			MessageTemplate messageTemplate,
			Class<? extends PlanBody> planBodyClass) {
		super(generateId(null, goalTemplate, messageTemplate, planBodyClass),
				goalTemplate, messageTemplate);
		this.planBodyClass = planBodyClass;
	}

	/**
	 * Creates a new simple plan, which is able to process messages that match
	 * the provided message template. It is a plan whose body is the specified
	 * class and its id is the plan body class name. It sets that this plan can
	 * process messages of the specified message template, but more message
	 * templates can be specified by overriding the
	 * {@link #initMessageTemplates()} method or invoking the
	 * {@link #addMessageTemplate(MessageTemplate)} method.
	 * 
	 * @param messageTemplate
	 *            the template of messages that this plan can process.
	 * @param planBodyClass
	 *            the class of this plan body.
	 */
	public DefaultPlan(MessageTemplate messageTemplate,
			Class<? extends PlanBody> planBodyClass) {
		super(generateId(null, null, messageTemplate, planBodyClass),
				messageTemplate);
		this.planBodyClass = planBodyClass;
	}

	/**
	 * Creates a new simple plan. It is a plan whose body is the specified class
	 * and its id is the given id. Goal templates can be specified by overriding
	 * the {@link #initGoalTemplates()} method or invoking the
	 * {@link #addGoalTemplate(GoalTemplate)} method, while more message
	 * templates can be specified by overriding the
	 * {@link #initMessageTemplates()} method or invoking the
	 * {@link #addMessageTemplate(MessageTemplate)} method.
	 * 
	 * @param id
	 *            the plan id.
	 * @param planBodyClass
	 *            the class of this plan body.
	 */
	public DefaultPlan(String id, Class<? extends PlanBody> planBodyClass) {
		super(id);
		this.planBodyClass = planBodyClass;
	}

	/**
	 * Creates a new simple plan, which is able to achieve goals that match the
	 * provided template. It is a plan whose body is the specified class and its
	 * id is the given id. It sets that this plan can achieve goals of the
	 * specified goal template, but more goal templates can be specified by
	 * overriding the {@link #initGoalTemplates()} method or invoking the
	 * {@link #addGoalTemplate(GoalTemplate)} method.
	 * 
	 * @param id
	 *            the plan id.
	 * @param goalTemplate
	 *            the template of goals that this plan can achieve.
	 * @param planBodyClass
	 *            the class of this plan body.
	 */
	public DefaultPlan(String id, GoalTemplate goalTemplate,
			Class<? extends PlanBody> planBodyClass) {
		super(id, goalTemplate);
		this.planBodyClass = planBodyClass;
	}

	/**
	 * Creates a new simple plan, which is able to achieve goals that match the
	 * provided goal template and process messages that match the provided
	 * message template. It is a plan whose body is the specified class and its
	 * id is the given id. It sets that this plan can achieve goals of the
	 * specified goal template, but more goal templates can be specified by
	 * overriding the {@link #initGoalTemplates()} method or invoking the
	 * {@link #addGoalTemplate(GoalTemplate)} method, while more message
	 * templates can be specified by overriding the
	 * {@link #initMessageTemplates()} method or invoking the
	 * {@link #addMessageTemplate(MessageTemplate)} method.
	 * 
	 * @param id
	 *            the plan id.
	 * @param goalTemplate
	 *            the template of goals that this plan can achieve.
	 * @param messageTemplate
	 *            the template of messages that this plan can process.
	 * @param planBodyClass
	 *            the class of this plan body.
	 */
	public DefaultPlan(String id, GoalTemplate goalTemplate,
			MessageTemplate messageTemplate,
			Class<? extends PlanBody> planBodyClass) {
		super(id, goalTemplate, messageTemplate);
		this.planBodyClass = planBodyClass;
	}

	/**
	 * Creates a new simple plan, which is able to process messages that match
	 * the provided message template. It is a plan whose body is the specified
	 * class and its id is the given id. It sets that this plan can process
	 * messages of the specified message template, but more message templates
	 * can be specified by overriding the {@link #initMessageTemplates()} method
	 * or invoking the {@link #addMessageTemplate(MessageTemplate)} method.
	 * 
	 * @param id
	 *            the plan id.
	 * @param messageTemplate
	 *            the template of messages that this plan can process.
	 * @param planBodyClass
	 *            the class of this plan body.
	 */
	public DefaultPlan(String id, MessageTemplate messageTemplate,
			Class<? extends PlanBody> planBodyClass) {
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
	public PlanBody createPlanBody() throws PlanInstantiationException {
		try {
			Class<?> enclosingClass = planBodyClass.getEnclosingClass();
			if (!Modifier.isStatic(planBodyClass.getModifiers())
					&& enclosingClass != null) {
				if (BDIAgent.class.isAssignableFrom(enclosingClass)) {
					return planBodyClass.getDeclaredConstructor(enclosingClass)
							.newInstance(
									getPlanLibrary().getCapability()
											.getMyAgent());
				} else if (Capability.class.isAssignableFrom(enclosingClass)) {
					return planBodyClass.getDeclaredConstructor(enclosingClass)
							.newInstance(getPlanLibrary().getCapability());
				} else {
					assert Plan.class.isAssignableFrom(enclosingClass);
					return planBodyClass.getDeclaredConstructor(enclosingClass)
							.newInstance(this);
				}
			} else {
				return this.planBodyClass.newInstance();
			}
		} catch (Exception e) {
			throw new PlanInstantiationException(e);
		}
	}

	/**
	 * Returns the class of plan body of this plan, to be instantiated and
	 * executed.
	 * 
	 * @return the planBodyClass
	 */
	public Class<? extends PlanBody> getPlanBodyClass() {
		return planBodyClass;
	}

}
