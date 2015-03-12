package bdi4jade.extension.planselection.learningbased.example;

import bdi4jade.goal.NamedSoftgoal;
import bdi4jade.goal.Softgoal;

public class Softgoals {

	public static final Softgoal COST = new NamedSoftgoal("COST");
	public static final Softgoal PERFORMANCE = new NamedSoftgoal("PERFORMANCE");

	public static final Softgoal SOFTGOALS[] = { COST, PERFORMANCE };

}
