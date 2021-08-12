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
import bdi4jade.plan.planbody.PlanBody;

/**
 * This annotation allows to specify that an attribute of a {@link Capability}
 * is a belief that should be added to the capability belief base. It can also
 * be used in plan bodies ({@link PlanBody}) so that beliefs are injected in
 * plan body attributes and, in this case, a name may be provided to retrieve
 * the belief. If no name is provided, the attribute name is used. The annotated
 * field should be of the type {@link bdi4jade.belief.Belief}.
 * 
 * @author Ingrid Nunes
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Belief {

	/**
	 * Returns the name of the belief, if it is a string, to be retrieved from
	 * the belief base, in case this annotation is used in a {@link PlanBody}.
	 * If no name is provided, the attribute name is used.
	 * 
	 * @return the belief name.
	 */
	String name() default "";

}
