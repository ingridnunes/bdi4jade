//----------------------------------------------------------------------------
// Copyright (C) 2013  Ingrid Nunes
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

package bdi4jade.extension.planselection.utilitybased;

import bdi4jade.plan.Plan;

/**
 * This abstract class has the common properties and operations of all kinds of
 * plan-goal dependency.
 * 
 * @author Ingrid Nunes
 */
public abstract class PlanGoalDependency {

	public static final String METADATA_NAME = PlanGoalDependency.class
			.getSimpleName();

	protected Plan root;

	public PlanGoalDependency(Plan root) {
		this.root = root;
	}

	/**
	 * Returns the plan that is the root of the plan-goal dependency, i.e. the
	 * plan that depends on one or more goals.
	 * 
	 * @return the root the plan that is the root of this dependency.
	 */
	public Plan getRoot() {
		return root;
	}

}
