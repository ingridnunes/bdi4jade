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

package bdi4jade.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import bdi4jade.annotation.Parameter;
import bdi4jade.annotation.Parameter.Direction;
import bdi4jade.belief.Belief;
import bdi4jade.core.Capability;
import bdi4jade.exception.ParameterException;
import bdi4jade.goal.Goal;
import bdi4jade.plan.planbody.PlanBody;

/**
 * This is a utility class that provides many methods that use reflection for
 * different purposes.
 * 
 * @author Ingrid Nunes
 * 
 */
public abstract class ReflectionUtils {

	private static final String GETTER_PREFIX = "get";
	private static final Log log = LogFactory.getLog(ReflectionUtils.class);
	private static final String SETTER_PREFIX = "set";

	/**
	 * Adds to the goal owners map the capability classes that owns a goal to
	 * the capability instance passed as parameter.
	 * 
	 * @param goalOwnersMap
	 *            the goal owners map to which the owners of the given
	 *            capability should be added.
	 * @param capability
	 *            the capability that should be added to the map.
	 */
	public static void addGoalOwner(
			Map<Class<? extends Capability>, Set<Capability>> goalOwnersMap,
			Capability capability) {
		if (!Capability.class.equals(capability.getClass())) {
			addGoalOwner(goalOwnersMap, capability.getClass(), capability);
			for (Class<? extends Capability> parentCapability : capability
					.getParentCapabilities()) {
				addGoalOwner(goalOwnersMap, parentCapability, capability);
			}
		} else {
			assert capability.getParentCapabilities().isEmpty();
		}
	}

	private static void addGoalOwner(
			Map<Class<? extends Capability>, Set<Capability>> goalOwnersMap,
			Class<? extends Capability> cababilityClass, Capability capability) {
		Set<Capability> owners = goalOwnersMap.get(cababilityClass);
		if (owners == null) {
			owners = new HashSet<>();
			goalOwnersMap.put(cababilityClass, owners);
		}
		owners.add(capability);
	}

	private static void checkSkipIsOK(Parameter parameter, String msg,
			Exception cause) throws ParameterException {
		if (parameter.mandatory()) {
			ParameterException exc = new ParameterException(msg, cause);
			log.warn(exc);
			throw exc;
		}
	}

	private static boolean isGetter(Method method) {
		if (!method.getName().startsWith(GETTER_PREFIX))
			return false;
		if (method.getParameterTypes().length != 0)
			return false;
		if (void.class.equals(method.getReturnType()))
			return false;
		return true;
	}

	private static boolean isParameterIn(Parameter param, Direction[] directions) {
		for (Direction dir : directions) {
			if (dir.equals(param.direction())) {
				return true;
			}
		}
		return false;
	}

	private static boolean isSetter(Method method) {
		if (!method.getName().startsWith(SETTER_PREFIX))
			return false;
		if (method.getParameterTypes().length != 1)
			return false;
		if (!void.class.equals(method.getReturnType()))
			return false;
		return true;
	}

	private static String methodToPropertyName(String prefix, Method method) {
		String property = method.getName().substring(prefix.length());
		return property.substring(0, 1).toLowerCase() + property.substring(1);
	}

	private static String propertyToMethodName(String prefix, String property) {
		return prefix + property.substring(0, 1).toUpperCase()
				+ property.substring(1);
	}

	/**
	 * Sets the input parameters of a plan body based on the parameters passed
	 * in the goal that triggered its execution.
	 * 
	 * @param planBody
	 *            the plan body to have its input parameters set.
	 * @param goal
	 *            the goal that has input parameters.
	 * @throws ParameterException
	 *             if an exception occurs in this setting process.
	 */
	public static void setPlanBodyInput(PlanBody planBody, Goal goal)
			throws ParameterException {
		setupParameters(planBody, new Direction[] { Direction.IN,
				Direction.INOUT }, goal, new Direction[] { Direction.IN,
				Direction.INOUT });
	}

	/**
	 * Sets the output parameters of a goal based on the output generated by the
	 * plan body whose execution was triggered by this goal.
	 * 
	 * @param planBody
	 *            the plan body generated the output parameters.
	 * @param goal
	 *            the goal to have its output parameters set.
	 * @throws ParameterException
	 *             if an exception occurs in this setting process.
	 */
	public static void setPlanBodyOutput(PlanBody planBody, Goal goal)
			throws ParameterException {
		setupParameters(goal,
				new Direction[] { Direction.OUT, Direction.INOUT }, planBody,
				new Direction[] { Direction.OUT, Direction.INOUT });
	}

	/**
	 * Sets plan body fields annotated with {@link bdi4jade.annotation.Belief}.
	 * 
	 * @param planBody
	 *            the plan body to be setup with beliefs.
	 */
	public static void setupBeliefs(PlanBody planBody) {
		Capability capability = planBody.getPlan().getPlanLibrary()
				.getCapability();
		Class<?> currentClass = planBody.getClass();
		while (PlanBody.class.isAssignableFrom(currentClass)) {
			for (Field field : currentClass.getDeclaredFields()) {
				boolean b = field.isAccessible();
				field.setAccessible(true);
				try {
					if (field
							.isAnnotationPresent(bdi4jade.annotation.Belief.class)) {
						if (Belief.class.isAssignableFrom(field.getType())) {
							bdi4jade.annotation.Belief beliefAnnotation = field
									.getAnnotation(bdi4jade.annotation.Belief.class);
							String beliefName = beliefAnnotation.name();
							if (beliefName == null || "".equals(beliefName)) {
								beliefName = field.getName();
							}
							Belief<?, ?> belief = capability.getBeliefBase()
									.getBelief(beliefName);
							field.set(planBody, belief);
						} else {
							throw new ClassCastException("Field "
									+ field.getName() + " should be a Belief");
						}
					}
				} catch (Exception exc) {
					log.warn(exc);
				}
				field.setAccessible(b);
			}
			currentClass = currentClass.getSuperclass();
		}
	}

	/**
	 * Sets the input parameters of goal based on the output parameters of
	 * another goal. This is useful when goals are executed sequentially, and
	 * the input of a goal is the output of another.
	 * 
	 * @param goalOut
	 *            the goal that has output parameters that are input of the
	 *            goalIn.
	 * @param goalIn
	 *            the goal to have its input parameters set.
	 * @throws ParameterException
	 *             if an exception occurs in this setting process.
	 */
	public static void setupParameters(Goal goalOut, Goal goalIn)
			throws ParameterException {
		setupParameters(goalIn,
				new Direction[] { Direction.IN, Direction.INOUT }, goalOut,
				new Direction[] { Direction.OUT, Direction.INOUT });
	}

	private static void setupParameters(Object obj1, Direction[] dir1,
			Object obj2, Direction[] dir2) throws ParameterException {
		for (Method method : obj1.getClass().getMethods()) {
			if (method.isAnnotationPresent(Parameter.class)) {
				Parameter parameter = method.getAnnotation(Parameter.class);

				if (!isParameterIn(parameter, dir1)) {
					continue;
				}

				if (!(isGetter(method) || isSetter(method))) {
					checkSkipIsOK(parameter, "Method " + method
							+ " should be a getter or setter.", null);
					continue;
				}

				String property = null;
				Method setter = null;

				if (isGetter(method)) {
					property = methodToPropertyName(GETTER_PREFIX, method);
					try {
						setter = obj1.getClass().getMethod(
								propertyToMethodName(SETTER_PREFIX, property),
								method.getReturnType());
					} catch (NoSuchMethodException nsme) {
						checkSkipIsOK(parameter,
								"There is no setter method associated with property "
										+ property, nsme);
						continue;
					}
				} else {
					property = methodToPropertyName(SETTER_PREFIX, method);
					setter = method;
				}

				Method getter = null;
				try {
					getter = obj2.getClass().getMethod(
							propertyToMethodName(GETTER_PREFIX, property));
					if (!getter.isAnnotationPresent(Parameter.class)
							|| !isParameterIn(
									getter.getAnnotation(Parameter.class), dir2)) {
						checkSkipIsOK(parameter,
								"There is no parameter associated with method "
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
					setter.invoke(obj1, getter.invoke(obj2));
				} catch (Exception exc) {
					checkSkipIsOK(parameter, "An unknown error occurrred.", exc);
				}
			}
		}
	}

}
