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

package bdi4jade.plan;

import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import bdi4jade.core.PlanLibrary;
import bdi4jade.exception.PlanInstantiationException;
import bdi4jade.goal.Goal;
import bdi4jade.message.MessageGoal;
import bdi4jade.softgoal.PlanContribution;
import bdi4jade.softgoal.PlanGoalDependency;
import bdi4jade.softgoal.Softgoal;
import bdi4jade.util.MetadataElementImpl;

/**
 * This class represents the plan abstraction. It defines the goals that the
 * plan can achieve, in which context, and knows which is its plan body.
 * 
 * @author ingrid
 */
public abstract class Plan extends MetadataElementImpl {

	public enum DefaultMetadata {

		CONTRIBUTIONS, DEPENDENCIES;

	}

	/**
	 * This enumuration represents the possible end states of a plan execution.
	 * 
	 * @author ingrid
	 */
	public enum EndState {
		FAILED, SUCCESSFUL;
	}

	private final Set<Class<? extends Goal>> goals;
	protected final String id;
	private final Set<MessageTemplate> messageTemplates;
	private PlanLibrary planLibrary;

	/**
	 * Constructs a new Plan. It sets the plan library and plan body class of
	 * this plan, and initializes the goals that it can achieve and message
	 * templates of messages it can process.
	 * 
	 * @param id
	 *            plan identifier
	 */
	public Plan(String id) {
		this(id, null, null);
	}

	/**
	 * Constructs a new Plan. It sets the plan library and plan body class of
	 * this plan, and initializes the goals that it can achieve and messages it
	 * can process. The goals are initialized with the provided goal class.
	 * 
	 * @param id
	 *            plan identifier
	 * @param goalClass
	 *            the goal that this plan can achieve
	 */
	public Plan(String id, Class<? extends Goal> goalClass) {
		this(id, goalClass, null);
	}

	/**
	 * Constructs a new Plan. It sets the plan library and plan body class of
	 * this plan, and initializes the goals that it can achieve and messages it
	 * can process. The goals are initialized with the provided goal class. The
	 * message templates is initialized with the provided template.
	 * 
	 * @param id
	 *            plan identifier
	 * @param goalClass
	 *            the goal that this plan can achieve
	 * @param messageTemplate
	 *            the template of messages that this plan can process.
	 */
	public Plan(String id, Class<? extends Goal> goalClass,
			MessageTemplate messageTemplate) {
		if (id == null) {
			throw new RuntimeException("Plan id cannot be null.");
		}
		this.id = id;
		this.goals = new HashSet<Class<? extends Goal>>();
		if (goalClass != null) {
			this.goals.add(goalClass);
		}
		initGoals();
		this.messageTemplates = new HashSet<MessageTemplate>();
		if (messageTemplate != null) {
			this.messageTemplates.add(messageTemplate);
		}
		initMessageTemplates();

		// Metadata
		putMetadata(DefaultMetadata.CONTRIBUTIONS,
				new HashMap<Softgoal, List<PlanContribution>>());
		putMetadata(DefaultMetadata.DEPENDENCIES,
				new ArrayList<PlanGoalDependency>());
	}

	/**
	 * Constructs a new Plan. It sets the plan library and plan body class of
	 * this plan, and initializes the goals that it can achieve and message
	 * templates of messages it can process. The message templates is
	 * initialized with the provided template.
	 * 
	 * @param id
	 *            the plan identifier
	 * @param messageTemplate
	 *            the template of messages that this plan can process.
	 */
	public Plan(String id, MessageTemplate messageTemplate) {
		this(id, null, messageTemplate);

	}

	/**
	 * Adds a goal class that this plan may achieve.
	 * 
	 * @param goalClass
	 *            the goal class that can be achieved by this plan.
	 */
	public void addGoal(Class<? extends Goal> goalClass) {
		this.goals.add(goalClass);
	}

	/**
	 * Adds a message template of messages that this plan can process.
	 * 
	 * @param messageTemplate
	 *            the message template of messages that can be processed.
	 */
	public void addMessageTemplate(MessageTemplate messageTemplate) {
		this.messageTemplates.add(messageTemplate);
	}

	/**
	 * Verifies if a given goal can be achieved by this plan. When the goal is a
	 * {@link MessageGoal}, it invokes the method
	 * {@link Plan#canProcess(ACLMessage)}. Otherwise, it checks if the class of
	 * this goal is contained in the goal set of this plan.
	 * 
	 * @param goal
	 *            the goal to be verified.
	 * @return true if the given goal can be achieved by this plan, false
	 *         otherwise.
	 */
	public boolean canAchieve(Goal goal) {
		if (goal instanceof MessageGoal) {
			return canProcess(((MessageGoal) goal).getMessage());
		} else {
			return goals.contains(goal.getClass()) ? matchesContext(goal)
					: false;
		}
	}

	/**
	 * Verifies if the message received matches with any of the message
	 * templates of this plan.
	 * 
	 * @param message
	 *            the message to be checked.
	 * @return true if this plan can process the message.
	 */
	public boolean canProcess(ACLMessage message) {
		for (MessageTemplate template : messageTemplates) {
			if (template.match(message))
				return true;
		}
		return false;
	}

	/**
	 * Instantiate the plan body of this plan. It must implement the
	 * {@link PlanBodyInterface} interface.
	 * 
	 * @return the instantiated plan body.
	 * @throws PlanInstantiationException
	 *             if an error occurred during the instantiation process.
	 */
	public abstract PlanBodyInterface createPlanBody()
			throws PlanInstantiationException;

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Plan))
			return false;
		return this.id.equals(((Plan) obj).id);
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @return the planLibrary
	 */
	public PlanLibrary getPlanLibrary() {
		return planLibrary;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return this.id.hashCode();
	}

	/**
	 * This method is invoked in the Plan constructor. It is responsible for
	 * initializing the goals that this plan can achieve. The method should be
	 * overridden by subclasses.
	 */
	protected void initGoals() {

	}

	/**
	 * This method is invoked in the Plan constructor. It is responsible for
	 * initializing the message templates that this plan can process. The method
	 * should be overridden by subclasses.
	 */
	protected void initMessageTemplates() {

	}

	/**
	 * Verifies that this plan can be executed in the current context. The
	 * method should be overridden by subclasses.
	 * 
	 * @param goal
	 *            the goal to be achieved
	 * 
	 * @return true if the context matches with the conditions needed for this
	 *         plan execution.
	 */
	protected boolean matchesContext(Goal goal) {
		return true;
	}

	/**
	 * @param planLibrary
	 *            the planLibrary to set
	 */
	public void setPlanLibrary(PlanLibrary planLibrary) {
		this.planLibrary = planLibrary;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return id;
	}

}
