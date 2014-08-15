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

package bdi4jade.message;

import jade.lang.acl.ACLMessage;
import bdi4jade.annotation.Parameter;
import bdi4jade.annotation.Parameter.Direction;
import bdi4jade.goal.Goal;

/**
 * This class represents the goal of processing a message received by the agent.
 * 
 * @author Ingrid Nunes
 */
public class MessageGoal implements Goal {

	private static final long serialVersionUID = -5960866880528268312L;

	private ACLMessage message;

	/**
	 * The default constructor. It should be only used if persistence frameworks
	 * are used.
	 */
	protected MessageGoal() {

	}

	/**
	 * Initializes a message goal with the given message.
	 * 
	 * @param message
	 *            the message to be processed.
	 */
	public MessageGoal(ACLMessage message) {
		this.message = message;
	}

	/**
	 * Returns the message associated with this message goal.
	 * 
	 * @return the message
	 */
	@Parameter(direction = Direction.IN)
	public ACLMessage getMessage() {
		return message;
	}

	/**
	 * Sets the message of this goal. Ideally, the message should be final and
	 * initialized in the constructor. This method should be only used if
	 * persistence frameworks are used.
	 * 
	 * @param message
	 *            the message to set
	 */
	protected void setMessage(ACLMessage message) {
		this.message = message;
	}

	/**
	 * Returns a string representation of this goal, in the form
	 * "MessageGoal: message".
	 * 
	 * @return the string representation of this message goal.
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return new StringBuffer(getClass().getName()).append(": ")
				.append(message).toString();
	}

}
