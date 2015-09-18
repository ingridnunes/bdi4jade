package bdi4jade.extension.planselection.learningbased.example;

import java.util.HashMap;
import java.util.Map;

import weka.classifiers.functions.LinearRegression;
import weka.classifiers.functions.MultilayerPerceptron;
import bdi4jade.annotation.Belief;
import bdi4jade.annotation.Plan;
import bdi4jade.belief.TransientBelief;
import bdi4jade.extension.planselection.learningbased.LearningBasedCapability;
import bdi4jade.extension.planselection.learningbased.NumericInfluenceFactor;
import bdi4jade.extension.planselection.learningbased.OptimizationFunction;
import bdi4jade.extension.planselection.learningbased.PlanMetadataElement;
import bdi4jade.goal.Softgoal;

public class ExampleCapability extends LearningBasedCapability {

	private static final long serialVersionUID = -7400340155463062034L;

	@Belief
	private TransientBelief<String, Double> belief01 = new TransientBelief<String, Double>(
			"BELIEF01", 0.0);
	@Belief
	private TransientBelief<String, Double> belief02 = new TransientBelief<String, Double>(
			"BELIEF02", 0.0);
	@Belief
	private TransientBelief<String, Double> belief03 = new TransientBelief<String, Double>(
			"BELIEF03", 0.0);

	@Plan
	ExamplePlan plan01 = new ExamplePlan("PLAN01");
	@Plan
	ExamplePlan plan02 = new ExamplePlan("PLAN02");

	public ExampleCapability() {
		init();
	}

	private void init() {

		Map<Softgoal, PlanMetadataElement> metadata = new HashMap<Softgoal, PlanMetadataElement>();

		PlanMetadataElement planMetadataElement = new PlanMetadataElement(plan01, Softgoals.COST,
				new Outcome01(), OptimizationFunction.MINIMIZE,
				LinearRegression.class, 50, 100);
		planMetadataElement.addInfluenceFactor(new NumericInfluenceFactor(belief01));
		planMetadataElement.addInfluenceFactor(new NumericInfluenceFactor(belief02));
		metadata.put(Softgoals.COST, planMetadataElement);

		planMetadataElement = new PlanMetadataElement(plan01, Softgoals.PERFORMANCE,
				new Outcome02(), OptimizationFunction.MAXIMIZE,
				MultilayerPerceptron.class, 50, 100);
		planMetadataElement.addInfluenceFactor(new NumericInfluenceFactor(belief02));
		planMetadataElement.addInfluenceFactor(new NumericInfluenceFactor(belief03));
		metadata.put(Softgoals.PERFORMANCE, planMetadataElement);

		plan01.putMetadata(PlanMetadataElement.METADATA_NAME, metadata);

		metadata = new HashMap<Softgoal, PlanMetadataElement>();

		planMetadataElement = new PlanMetadataElement(plan02, Softgoals.COST,
				new Outcome01(), OptimizationFunction.MINIMIZE,
				LinearRegression.class, 50, 100);
		planMetadataElement.addInfluenceFactor(new NumericInfluenceFactor(belief01));
		planMetadataElement.addInfluenceFactor(new NumericInfluenceFactor(belief02));
		metadata.put(Softgoals.COST, planMetadataElement);

		planMetadataElement = new PlanMetadataElement(plan02, Softgoals.PERFORMANCE,
				new Outcome02(), OptimizationFunction.MAXIMIZE,
				MultilayerPerceptron.class, 50, 100);
		planMetadataElement.addInfluenceFactor(new NumericInfluenceFactor(belief02));
		planMetadataElement.addInfluenceFactor(new NumericInfluenceFactor(belief03));
		metadata.put(Softgoals.PERFORMANCE, planMetadataElement);

		plan02.putMetadata(PlanMetadataElement.METADATA_NAME, metadata);
	}
}
