/**
 * 
 */
package br.ufrgs.inf.bdi4jade.examples.planselection;

/**
 * @author ingrid
 * 
 */
public interface Plans {

	public static final TransportationPlan BIKE_PLAN = new TransportationPlan(
			"Bike", 0.05, 0.20, false, 0.10, 0.20, 86, 90);
	public static final TransportationPlan BUS_PLAN = new TransportationPlan(
			"Bus", 0.10, 0.18, true, 0.35, 0.40, 50, 60);
	public static final TransportationPlan CAR_PLAN = new TransportationPlan(
			"Car", 0.15, 0.15, false, 0.90, 0.80, 20, 35);
	public static final TransportationPlan MOTORCYCLE_PLAN = new TransportationPlan(
			"Moto", 0.30, 0.20, false, 0.55, 0.50, 10, 20);

	public static final TransportationPlan PLANS[] = { BIKE_PLAN, BUS_PLAN,
			CAR_PLAN, MOTORCYCLE_PLAN };

}
