//----------------------------------------------------------------------------
// Copyright (C) 2011  Ingrid Nunes, et al.
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
// http://www.inf.puc-rio.br/~ionunes/
//
//----------------------------------------------------------------------------

package br.pucrio.inf.les.bdi4jade.event;

import java.util.EventListener;

/**
 * This interface defined the method that a goal listener should implement. A
 * goal listener can be notified about changes in goal, when it subscribed to a
 * class that can notify updates.
 * 
 * @author ingrid
 */
public interface GoalListener extends EventListener {

	/**
	 * Notifies the listener that the goal was performed.
	 * 
	 * @param event
	 *            the performed goal event.
	 */
	public void goalPerformed(GoalEvent event);

}
