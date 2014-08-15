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

import bdi4jade.goal.Softgoal;

/**
 * This class stores the probability of a plan promoting a value with respect to
 * a softgoal.
 * 
 * @author Ingrid Nunes
 */
public class PlanContribution {

	public static final String METADATA_NAME = PlanContribution.class
			.getSimpleName();

	private Double probability;
	private final Softgoal softgoal;
	private Double value;

	public PlanContribution(Softgoal softgoal) {
		this(softgoal, 0.0, 0.0);
	}

	public PlanContribution(Softgoal softgoal, Double probability, Double value) {
		this.softgoal = softgoal;
		this.probability = probability;
		this.value = value;
	}

	/**
	 * @return the probability
	 */
	public Double getProbability() {
		return probability;
	}

	/**
	 * @return the softgoal
	 */
	public Softgoal getSoftgoal() {
		return softgoal;
	}

	/**
	 * @return the value
	 */
	public Double getValue() {
		return value;
	}

	/**
	 * @param probability
	 *            the probability to set
	 */
	public void setProbability(Double probability) {
		this.probability = probability;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(Double value) {
		this.value = value;
	}

}
