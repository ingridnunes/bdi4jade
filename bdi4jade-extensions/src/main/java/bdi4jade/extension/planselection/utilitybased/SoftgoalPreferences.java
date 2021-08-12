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

import java.util.HashMap;
import java.util.Map;

import bdi4jade.belief.TransientBelief;
import bdi4jade.goal.Softgoal;

/**
 * This is an agent transient belief (@see {@link TransientBelief}) that stores
 * the preferences for softgoals.
 * 
 * @author Ingrid Nunes
 */
public class SoftgoalPreferences extends
		TransientBelief<String, Map<Softgoal, Double>> {

	public static final String NAME = "SoftgoalPreferences";

	private static final long serialVersionUID = 1802540697397519283L;

	public SoftgoalPreferences() {
		super(NAME, new HashMap<Softgoal, Double>());
	}

	/**
	 * Returns the preference for a softgoal.
	 * 
	 * @param softgoal
	 *            the softgoal
	 * @return the preference for the softgoal
	 */
	public Double getPreferenceForSoftgoal(Softgoal softgoal) {
		return this.value.get(softgoal);
	}

	/**
	 * Sets the preference for a softgoal.
	 * 
	 * @param softgoal
	 *            the softgoal to which the preference is set.
	 * @param preference
	 *            the preference value.
	 */
	public void setPreferenceForSoftgoal(Softgoal softgoal, Double preference) {
		this.value.put(softgoal, preference);
	}

}
