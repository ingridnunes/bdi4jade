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

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Ingrid Nunes
 */
public class GenericValueFunction<T> {

	private Double average;
	private Pair<Double> minMax;
	private Double standardDeviation;
	private Double total;
	private Map<T, Double> values;

	public GenericValueFunction() {
		this.values = new HashMap<T, Double>();
		this.minMax = null;
		this.total = null;
		this.average = null;
		this.standardDeviation = null;
	}

	public synchronized void addValue(T key, Double value) {
		this.values.put(key, value);
		if (minMax == null) {
			this.minMax = new Pair<Double>(value, value);
		} else {
			if (minMax.getValue1() > value)
				minMax.setValue1(value);
			if (minMax.getValue2() < value)
				minMax.setValue2(value);
		}

		total = null;
		average = null;
		standardDeviation = null;
	}

	private synchronized void calculateAverage() {
		average = (values.size() == 0) ? null : getTotal() / values.size();
	}

	private synchronized void calculateStandardDeviation() {
		Double variance = getVariance();
		standardDeviation = (variance == null) ? null : Math.pow(variance, 0.5);
	}

	/**
	 * @return the average
	 */
	public synchronized Double getAverage() {
		if (average == null)
			calculateAverage();
		return average;
	}

	public int getCount() {
		return values.size();
	}

	public Double getMax() {
		return minMax == null ? null : minMax.getValue2();
	}

	public Double getMin() {
		return minMax == null ? null : minMax.getValue1();
	}

	/**
	 * @return the standardDeviation
	 */
	public synchronized Double getStandardDeviation() {
		if (standardDeviation == null)
			calculateStandardDeviation();
		return standardDeviation;
	}

	/**
	 * @return the total
	 */
	public synchronized Double getTotal() {
		if (total == null) {
			this.total = 0.0;
			for (Double value : values.values()) {
				total += value;
			}
		}
		return total;
	}

	public Double getValue(T key) {
		return values.get(key);
	}

	public synchronized Double getVariance() {
		long n = 0;
		double mean = 0;
		double s = 0.0;

		for (Double x : values.values()) {
			n++;
			double delta = x - mean;
			mean += delta / n;
			s += delta * (x - mean);
		}
		// if you want to calculate std deviation
		// of a sample change this to (s/(n-1))
		return n == 0 ? null : (s / n);
	}

	public Set<T> keySet() {
		return values.keySet();
	}

	public String stats() {
		StringBuffer sb = new StringBuffer();
		sb.append("Total = ").append(getTotal()).append("\n");
		sb.append("Count = ").append(getCount()).append("\n");
		sb.append("Min = ").append(getMin()).append("\n");
		sb.append("Max = ").append(getMax()).append("\n");
		sb.append("Average = ").append(getAverage()).append("\n");
		sb.append("Standard Deviation = ").append(getStandardDeviation());
		return sb.toString();
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		for (T key : values.keySet()) {
			sb.append("R(").append(key).append(") = ").append(values.get(key))
					.append("\n");
		}
		sb.append(stats());
		return sb.toString();
	}

	public String toStringTab() {
		StringBuffer sb = new StringBuffer();
		sb.append("Key\tValue\n");
		for (T key : values.keySet()) {
			sb.append(key).append("\t").append(values.get(key)).append("\n");
		}
		sb.append(stats());
		return sb.toString();
	}

}
