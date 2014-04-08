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

package bdi4jade.util;

import java.lang.reflect.Method;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import bdi4jade.annotation.Parameter;
import bdi4jade.annotation.Parameter.Direction;
import bdi4jade.exception.GoalParameterException;
import bdi4jade.goal.Goal;

/**
 * @author Ingrid Nunes
 * 
 */
public abstract class ReflectionUtils {

	private static final String GETTER_PREFIX = "get";
	private static final Log log = LogFactory.getLog(ReflectionUtils.class);
	private static final String SETTER_PREFIX = "set";

	private static void checkSkipIsOK(Parameter parameter, String msg,
			Exception cause) throws GoalParameterException {
		if (parameter.mandatory()) {
			GoalParameterException exc = new GoalParameterException(msg, cause);
			log.warn(exc);
			throw exc;
		}
	}

	protected static boolean isGetter(Method method) {
		if (!method.getName().startsWith(GETTER_PREFIX))
			return false;
		if (method.getParameterTypes().length != 0)
			return false;
		if (void.class.equals(method.getReturnType()))
			return false;
		return true;
	}

	protected static boolean isSetter(Method method) {
		if (!method.getName().startsWith(SETTER_PREFIX))
			return false;
		if (method.getParameterTypes().length != 1)
			return false;
		if (!void.class.equals(method.getReturnType()))
			return false;
		return true;
	}

	protected static String methodToPropertyName(String prefix, Method method) {
		String property = method.getName().substring(prefix.length());
		return property.substring(0, 1).toLowerCase() + property.substring(1);
	}

	protected static String propertyToMethodName(String prefix, String property) {
		return prefix + property.substring(0, 1).toUpperCase()
				+ property.substring(1);
	}

	public static void setupParameters(Goal goalOut, Goal goalIn)
			throws GoalParameterException {
		for (Method method : goalIn.getClass().getMethods()) {
			if (method.isAnnotationPresent(Parameter.class)) {
				Parameter parameter = method.getAnnotation(Parameter.class);

				if (Direction.OUT.equals(parameter.direction())) {
					continue;
				}
				if (!isGetter(method)) {
					checkSkipIsOK(parameter, "Method " + method
							+ " should be a getter.", null);
					continue;
				}
				String property = methodToPropertyName(GETTER_PREFIX, method);

				Method setter = null;
				try {
					setter = goalIn.getClass().getMethod(
							propertyToMethodName(SETTER_PREFIX, property),
							method.getReturnType());
				} catch (NoSuchMethodException nsme) {
					checkSkipIsOK(parameter,
							"There is no setter method associated with property "
									+ property, nsme);
					continue;
				}

				Method getter = null;
				try {
					getter = goalOut.getClass().getMethod(
							propertyToMethodName(GETTER_PREFIX, property));
					if (!getter.isAnnotationPresent(Parameter.class)
							|| Direction.IN.equals(getter.getAnnotation(
									Parameter.class).direction())) {
						checkSkipIsOK(parameter,
								"There is no output parameter associated with method "
										+ method + " name " + property, null);
						continue;
					}
				} catch (NoSuchMethodException nsme) {
					checkSkipIsOK(parameter,
							"There is no getter method associated with property "
									+ property, nsme);
					continue;
				}

				try {
					setter.invoke(goalIn, getter.invoke(goalOut));
				} catch (Exception exc) {
					checkSkipIsOK(parameter, "An unknown error occurrred.", exc);
				}
			}
		}
	}

}
