package bdi4jade.extension.planselection.learningbased.learningalgorithm;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

import weka.classifiers.functions.LinearRegression;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import bdi4jade.extension.planselection.learningbased.LearningAlgorithm;
import bdi4jade.extension.planselection.learningbased.PlanMetadata;
import bdi4jade.extension.planselection.learningbased.util.Utils;
import bdi4jade.goal.Softgoal;
import bdi4jade.plan.Plan;

public class LinearRegressionAlgorithm implements LearningAlgorithm {

	private Instances trainingInstances;
	private LinearRegression model;

	@Override
	@SuppressWarnings("unchecked")
	public double predictExpectedContribution(Plan plan, Softgoal softgoal) {

		double prediction = 1;
		PlanMetadata planMetadata = ((Map<Softgoal, PlanMetadata>) plan
				.getMetadata(PlanMetadata.METADATA_NAME)).get(softgoal);

		if (planMetadata.getPlanExecutionsCounter() < PlanMetadata.MIN_PLAN_EXECUTIONS) {
			String filePath = "instances/"
					+ plan.getClass().getSimpleName().toLowerCase() + "_"
					+ softgoal + ".arff";
			if (!new File(filePath).exists()) {
				try {
					Utils.writeToFile(filePath,
							planMetadata.getArffFileHeader());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			planMetadata.increasePlanExecutionsCounter();
		} else {
			learnFromTrainingSet(plan, softgoal);

			int numOfFactors = planMetadata.getInfluenceFactors().size();

			Instance instance = new DenseInstance(numOfFactors);

			for (int i = 0; i < numOfFactors; i++) {
				instance.setValue(trainingInstances.attribute(i),
						(double) planMetadata.getInfluenceFactors().get(i)
								.getBeliefValue());
			}

			try {
				prediction = model.classifyInstance(instance);
				// System.out.println("Current Instance (" + instance + "): " +
				// prediction);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return prediction;
	}

	private void learnFromTrainingSet(Plan plan, Softgoal softgoal) {
		try {
			trainingInstances = new Instances(new BufferedReader(
					new FileReader("instances/"
							+ plan.getClass().getSimpleName().toLowerCase()
							+ "_" + softgoal + ".arff")));

			trainingInstances
					.setClassIndex(trainingInstances.numAttributes() - 1);

			model = new LinearRegression();
			model.buildClassifier(trainingInstances);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}