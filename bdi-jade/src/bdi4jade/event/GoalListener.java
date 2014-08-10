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

package bdi4jade.event;

import java.util.EventListener;

/**
 * This interface defines the method that a goal listener should implement. A
 * goal listener can be notified about changes in goals, when it subscribed to a
 * class that can notify events about goals.
 * 
 * @author Ingrid Nunes
 */
public interface GoalListener extends EventListener {

	/**
	 * Notifies the listener that a goal that was performed.
	 * 
	 * @param event
	 *            the goal event that occurred.
	 */
	public void goalPerformed(GoalEvent event);

}
