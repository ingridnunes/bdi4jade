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
// http://inf.ufrgs.br/~ingridnunes/bdi4jade/
//
//----------------------------------------------------------------------------

package br.ufrgs.inf.bdi4jade.message;

import jade.lang.acl.ACLMessage;
import br.ufrgs.inf.bdi4jade.goal.Goal;

/**
 * This class represents the goal of processing a message.
 * 
 * @author ingrid
 */
public class MessageGoal implements Goal {

	private static final long serialVersionUID = -5960866880528268312L;

	private ACLMessage message;

	/**
	 * Constructor.
	 * 
	 * @param message
	 *            the message to be processed.
	 */
	public MessageGoal(ACLMessage message) {
		this.message = message;
	}

	/**
	 * @return the message
	 */
	public ACLMessage getMessage() {
		return message;
	}

}
