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

package bdi4jade.event;

import java.util.EventListener;

/**
 * This interface defined the method that a belief listener should implement. A
 * belief listener can be notified about changes in beliefs, when it subscribed
 * to a class that can notify updates.
 * 
 * @author ingridnunes
 */
public interface BeliefListener extends EventListener {

	/**
	 * Updates the listener according to a change in a belief.
	 * 
	 * @param beliefEvent
	 */
	public void update(BeliefEvent beliefEvent);

}
