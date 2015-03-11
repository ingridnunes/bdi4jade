package bdi4jade.extension.planselection.learningbased;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import bdi4jade.extension.planselection.learningbased.util.Utils;
import bdi4jade.goal.Softgoal;
import bdi4jade.plan.Plan;

/**
 * It represents the algorithm used by the {@link}
 * LearningBasedPlanSelectionStrategy. It uses an algorithm, specified in
 * {@link} PlanMetadata, to predict an expected contribution of a plan's
 * outcome.
 * 
 * @author João Faccin
 */
public class LearningAlgorithm {

	private Instances trainingInstances;

	public LearningAlgorithm() {
	}

	/**
	 * Predicts an expected plan's contribution for a given softgoal based on
	 * plan metadata and current context conditions.
	 * 
	 * @param plan
	 *            A plan which expected contribution we want to predict.
	 * @param softgoal
	 *            A softgoal which plan's contribution is related.
	 * @return An expected plan contribution value.
	 */
	@SuppressWarnings("unchecked")
	public double predictExpectedContribution(Plan plan, Softgoal softgoal) {

		double prediction = 1;
		PlanMetadata planMetadata = ((Map<Softgoal, PlanMetadata>) plan
				.getMetadata(PlanMetadata.METADATA_NAME)).get(softgoal);

		if (planMetadata.getPlanExecutionsCounter() < planMetadata
				.getMinPlanExecutions()) {
			String filePath = planMetadata.getFilePath()
					+ (plan.getId() + "_" + plan.getClass().getSimpleName()
							+ "_" + softgoal).toLowerCase() + ".arff";
			if (!new File(filePath).exists()) {
				try {
					Utils.writeToFile(filePath,
							planMetadata.getArffFileHeader());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else {

			if (planMetadata.getPlanExecutionsCounter() == planMetadata
					.getMinPlanExecutions()
					|| planMetadata.getPlanExecutionsCounter()
							% planMetadata.getLearningGap() == 0) {
				learnFromTrainingSet(plan, softgoal);
			}

			int numOfFactors = planMetadata.getInfluenceFactors().size();

			Instance instance = new DenseInstance(numOfFactors);

			for (int i = 0; i < numOfFactors; i++) {
				instance.setValue(trainingInstances.attribute(i),
						(Double) planMetadata.getInfluenceFactors().get(i)
								.getBeliefValue());
			}

			try {
				prediction = planMetadata.getModel().classifyInstance(instance);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		switch (planMetadata.getOptimizationFunction()) {
		case MINIMIZE:
			return 1 - prediction;
		default:
			return prediction;
		}
	}

	/**
	 * Generates a prediction model from a training set of a given plan and
	 * softgoal.
	 * 
	 * @param plan
	 *            A plan which prediction model we want to generate.
	 * @param softgoal
	 *            A softgoal that the generated prediction model will relate.
	 */
	@SuppressWarnings("unchecked")
	private void learnFromTrainingSet(Plan plan, Softgoal softgoal) {

		PlanMetadata planMetadata = ((Map<Softgoal, PlanMetadata>) plan
				.getMetadata(PlanMetadata.METADATA_NAME)).get(softgoal);

		try {
			trainingInstances = new Instances(new BufferedReader(
					new FileReader(
							planMetadata.getFilePath()
									+ (plan.getId() + "_"
											+ plan.getClass().getSimpleName()
											+ "_" + softgoal).toLowerCase()
									+ ".arff")));

			trainingInstances
					.setClassIndex(trainingInstances.numAttributes() - 1);

			planMetadata.getModel().buildClassifier(trainingInstances);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}