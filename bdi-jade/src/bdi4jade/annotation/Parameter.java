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

/**
 * This annotations is used to specify properties that are input, output or both
 * of goals and plan bodies. When a plan body has an annotated input, it is set
 * automatically by the platform by obtaining it from a goal input. When a plan
 * body has an output, it is automatically used to set a goal output.
 * 
 * @author Ingrid Nunes
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Parameter {

	/**
	 * This enumeration is used to indicate the parameter direction (input,
	 * output, or both).
	 * 
	 * @author Ingrid Nunes
	 */
	public enum Direction {
		IN, INOUT, OUT
	};

	/**
	 * This attribute indicates the direction of the parameter. The default
	 * value is {@link Direction#IN}.
	 * 
	 * @return the parameter direction.
	 */
	Direction direction() default Direction.IN;

	/**
	 * This attribute indicates if the parameter is mandatory. The default is
	 * false.
	 * 
	 * @return true if the parameter is mandatory, false otherwise.
	 */
	boolean mandatory() default false;

}
