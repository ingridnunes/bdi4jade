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

package bdi4jade.reasoning;

/**
 * This class is the default implementation of the strategy
 * {@link BeliefRevisionStrategy}. It performs no action over beliefs.
 * 
 * @author Ingrid Nunes
 */
public class DefaultBeliefRevisionStrategy extends AbstractReasoningStrategy
		implements BeliefRevisionStrategy {

	/**
	 * This performs no action, that is, it is an empty implementation.
	 * Therefore, there is no change in the capability belief base.
	 * 
	 * @see BeliefRevisionStrategy#reviewBeliefs()
	 */
	@Override
	public void reviewBeliefs() {

	}

}
