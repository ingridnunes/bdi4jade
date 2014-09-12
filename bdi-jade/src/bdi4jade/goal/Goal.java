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

package bdi4jade.goal;

import jade.content.Concept;
import jade.content.ContentElement;

import java.io.Serializable;

import bdi4jade.annotation.GoalOwner;

/**
 * This interface defines the abstraction of an agent goal. A class that
 * implements this class can be used as a goal from a capability and can be
 * achieved by plans.
 * 
 * A class implementing this interface may be annotated with {@link GoalOwner}
 * to specify the capability that owns this goal.
 * 
 * @author Ingrid Nunes
 */
public interface Goal extends Serializable, Concept, ContentElement {

}
