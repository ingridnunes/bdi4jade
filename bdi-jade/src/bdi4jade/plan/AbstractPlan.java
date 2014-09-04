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

import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.util.HashSet;
import java.util.Set;

import bdi4jade.core.MetadataElementImpl;
import bdi4jade.goal.Goal;
import bdi4jade.goal.GoalTemplate;
import bdi4jade.message.MessageGoal;

/**
 * This class represents the plan abstraction, being an abstract implementation
 * of the {@link Plan} interface.
 * 
 * @author Ingrid Nunes
 */
public abstract class AbstractPlan extends MetadataElementImpl implements Plan {

	private Set<GoalTemplate> goalTemplates;
	private String id;
	private Set<MessageTemplate> messageTemplates;
	private PlanLibrary planLibrary;

	/**
	 * The default constructor. It should be only used if persistence frameworks
	 * are used.
	 */
	protected AbstractPlan() {
		this.goalTemplates = new HashSet<>();
		this.messageTemplates = new HashSet<>();
	}

	/**
	 * Creates a new plan with an identifier.
	 * 
	 * @param id
	 *            the plan identifier.
	 */
	public AbstractPlan(String id) {
		this(id, null, null);
	}

	/**
	 * Creates a new plan with an identifier and a template of goals it can
	 * achieve.
	 * 
	 * @param id
	 *            the plan identifier.
	 * @param goalTemplate
	 *            the template of goals that this plan can achieve.
	 */
	public AbstractPlan(String id, GoalTemplate goalTemplate) {
		this(id, goalTemplate, null);
	}

	/**
	 * Creates a new plan with an identifier, a template of goals it can
	 * achieve, and a template of messages it can process.
	 * 
	 * @param id
	 *            the plan identifier.
	 * @param goalTemplate
	 *            the template of goals that this plan can achieve.
	 * @param messageTemplate
	 *            the template of messages that this plan can process.
	 */
	public AbstractPlan(String id, GoalTemplate goalTemplate,
			MessageTemplate messageTemplate) {
		this();
		if (id == null) {
			throw new RuntimeException("Plan id cannot be null.");
		}
		this.id = id;
		if (goalTemplate != null) {
			this.goalTemplates.add(goalTemplate);
		}
		initGoalTemplates();
		if (messageTemplate != null) {
			this.messageTemplates.add(messageTemplate);
		}
		initMessageTemplates();
	}

	/**
	 * Creates a new plan with an identifier and a template of messages it can
	 * process.
	 * 
	 * @param id
	 *            the plan identifier.
	 * @param messageTemplate
	 *            the template of messages that this plan can process.
	 */
	public AbstractPlan(String id, MessageTemplate messageTemplate) {
		this(id, null, messageTemplate);

	}

	/**
	 * Adds template of goals that this plan can achieve.
	 * 
	 * @param goalTemplate
	 *            the template of goals that this plan can achieve.
	 */
	public void addGoalTemplate(GoalTemplate goalTemplate) {
		this.goalTemplates.add(goalTemplate);
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
	 * Verifies if a given goal can be achieved by this plan. It first checks if
	 * the current context is applicable by invoking the
	 * {@link #isContextApplicable(Goal)} method. If so, when the goal is a
	 * {@link MessageGoal}, it invokes the method
	 * {@link #canProcess(ACLMessage)}. Otherwise, it checks if the class of
	 * this goal is in the goal set of this plan.
	 * 
	 * @param goal
	 *            the goal to be verified.
	 * @return true if the given goal can be achieved by this plan, false
	 *         otherwise.
	 * 
	 * @see Plan#canAchieve(Goal)
	 */
	@Override
	public boolean canAchieve(Goal goal) {
		boolean canAchieve = false;
		if (goal instanceof MessageGoal) {
			canAchieve = canProcess(((MessageGoal) goal).getMessage());
		} else {
			for (GoalTemplate template : goalTemplates) {
				if (template.match(goal)) {
					canAchieve = true;
					break;
				}
			}
		}
		return canAchieve ? isContextApplicable(goal) : false;
	}

	/**
	 * Verifies if the message matches with any of the message templates of this
	 * plan.
	 * 
	 * @param message
	 *            the message to be checked.
	 * @return true if this plan can process the message, false otherwise.
	 * 
	 * @see Plan#canProcess(ACLMessage)
	 */
	@Override
	public boolean canProcess(ACLMessage message) {
		for (MessageTemplate template : messageTemplates) {
			if (template.match(message))
				return true;
		}
		return false;
	}

	/**
	 * Returns true if the object given as parameter is a plan and has the same
	 * id of this plan.
	 * 
	 * @param obj
	 *            the object to be tested as equals to this plan.
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Plan))
			return false;
		return this.id.equals(((Plan) obj).getId());
	}

	/**
	 * Returns the set of goal templates of the goals that can be achieved by
	 * this plan.
	 * 
	 * @return the goal templates
	 */
	public Set<GoalTemplate> getGoalTemplates() {
		return goalTemplates;
	}

	/**
	 * Returns the identifier of this plan.
	 * 
	 * @return the id.
	 * 
	 * @see Plan#getId()
	 */
	public String getId() {
		return id;
	}

	/**
	 * Returns the set of message templates of the messages that can be
	 * processed by this plan.
	 * 
	 * @return the message templates
	 */
	public Set<MessageTemplate> getMessageTemplates() {
		return messageTemplates;
	}

	/**
	 * Returns the plan library with which this plan is associated.
	 * 
	 * @return the planLibrary.
	 * 
	 * @see Plan#getPlanLibrary()
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
	 * initializing the goal templates that this plan can achieve. The method
	 * should be overridden by subclasses.
	 */
	protected void initGoalTemplates() {

	}

	/**
	 * This method is invoked in the Plan constructor. It is responsible for
	 * initializing the message templates that this plan can process. The method
	 * should be overridden by subclasses.
	 */
	protected void initMessageTemplates() {

	}

	/**
	 * Verifies that this plan can be executed in the current context and a
	 * given goal. The method should be overridden by subclasses, this
	 * implementation returns always true.
	 * 
	 * @param goal
	 *            the goal to be achieved whose conditions may be tested to
	 *            verify the applicability of this plan.
	 * 
	 * @return true.
	 * 
	 * @see bdi4jade.plan.Plan#isContextApplicable(Goal)
	 */
	@Override
	public boolean isContextApplicable(Goal goal) {
		return true;
	}

	/**
	 * Sets the plan library with which this plan is associated.
	 * 
	 * @param planLibrary
	 *            the planLibrary to set
	 */
	public void setPlanLibrary(PlanLibrary planLibrary) {
		this.planLibrary = planLibrary;
	}

	/**
	 * Returns the string representation of this plan, which is its id.
	 * 
	 * @return the id of the plan.
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return id;
	}

}
