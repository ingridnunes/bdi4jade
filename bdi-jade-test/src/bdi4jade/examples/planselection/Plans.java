//----------------------------------------------------------------------------
// Copyright (C) 2013 Ingrid Nunes
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

package bdi4jade.examples.planselection;

/**
 * @author Ingrid Nunes
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
