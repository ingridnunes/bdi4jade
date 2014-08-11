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

package bdi4jade.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import bdi4jade.core.Capability;
import bdi4jade.goal.Goal;

/**
 * This annotation allows to specify that a {@link Goal} belongs to a
 * capability and, if so, whether is is internal or external. If a goal belongs to a capability, it is
 * 
 * @author ingridnunes
 * 
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface GoalOwner {

	/**
	 * This attribute indicates the {@link Capability} that owns the
	 * {@link Goal}.
	 * 
	 * @return the class of capability that owns the goal
	 */
	Class<? extends Capability> capability();

	/**
	 * This attribute indicates whether the {@link Goal} is internal. If so, the
	 * goal can be dispatched only by the capability that owns the goal, or its
	 * children, and be achieved by plans of this capability (or its children).
	 * The default value is false.
	 * 
	 * @return true is the goal is internal, false otherwise
	 */
	boolean internal() default false;

}
