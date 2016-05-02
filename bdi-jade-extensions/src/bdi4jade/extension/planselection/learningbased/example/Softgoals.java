package bdi4jade.extension.planselection.learningbased.example;

import bdi4jade.goal.NamedSoftgoal;
import bdi4jade.goal.Softgoal;

public class Softgoals {

	public static final Softgoal MINCOST = new NamedSoftgoal("COST");
	public static final Softgoal MAXPERFORMANCE = new NamedSoftgoal("PERFORMANCE");

	public static final Softgoal SOFTGOALS[] = { MINCOST, MAXPERFORMANCE };

}
