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

package bdi4jade.extension.softgoal.plan;

import jade.lang.acl.MessageTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import bdi4jade.extension.softgoal.core.Softgoal;
import bdi4jade.goal.Goal;
import bdi4jade.plan.SimplePlan;

/**
 * @author Ingrid Nunes
 *
 */
public class AnnotatedPlan extends SimplePlan {

	public enum DefaultMetadata {

		CONTRIBUTIONS, DEPENDENCIES;

	}
	
	/**
	 * Constructs a new Plan. It sets the plan library and plan body class of
	 * this plan, and initializes the goals that it can achieve and message
	 * templates of messages it can process.
	 * 
	 * @param id
	 *            plan identifier
	 */
	public AnnotatedPlan(String id) {
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
	public AnnotatedPlan(String id, Class<? extends Goal> goalClass) {
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
	public AnnotatedPlan(String id, Class<? extends Goal> goalClass,
			MessageTemplate messageTemplate) {		
		super(id, goalClass, messageTemplate);

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
	public AnnotatedPlan(String id, MessageTemplate messageTemplate) {
		this(id, null, messageTemplate);

	}

}
